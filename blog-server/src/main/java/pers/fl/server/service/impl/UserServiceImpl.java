package pers.fl.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pers.fl.common.constant.CommonConst;
import pers.fl.common.constant.MessageConstant;
import pers.fl.common.dto.*;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.*;
import pers.fl.common.po.admin.UserRole;
import pers.fl.common.utils.JWTUtils;
import pers.fl.server.dao.BlogDao;
import pers.fl.server.dao.FavoritesDao;
import pers.fl.server.dao.ThumbsUpDao;
import pers.fl.server.dao.UserDao;
import pers.fl.server.dao.admin.RoleDao;
import pers.fl.server.dao.admin.TbUserRoleDao;
import pers.fl.server.dto.UserDetailDTO;
import pers.fl.server.exception.BizException;
import pers.fl.server.service.CommentService;
import pers.fl.server.service.UserRoleService;
import pers.fl.server.service.UserService;
import pers.fl.server.utils.BeanCopyUtils;
import pers.fl.server.utils.IpUtils;
import pers.fl.server.utils.RedisUtil;
import pers.fl.common.vo.ResetPasswordVO;
import pers.fl.common.vo.UpdateUserVO;
import pers.fl.common.vo.UserDisableVO;
import pers.fl.server.utils.CommonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static pers.fl.common.constant.MessageConstant.USER_ABLE;
import static pers.fl.common.constant.RabbitMQConst.EMAIL_EXCHANGE;
import static pers.fl.common.constant.RedisConst.*;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author fengliang
 * @since 2021-01-26
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private CommentService commentService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private ThumbsUpDao thumbsUpDao;
    @Resource
    private FavoritesDao favoritesDao;
    @Resource
    private UserDao userDao;
    @Resource
    private BlogDao blogDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private TbUserRoleDao tbUserRoleDao;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SessionRegistry sessionRegistry;
    @Resource
    private RabbitTemplate rabbitTemplate;


    public boolean UserExist(String username) {//搜索用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("1");
        wrapper.eq("username", username).last("limit 1");
        return userDao.selectCount(wrapper) != 0;
    }

    @Cacheable(value = {"UserMap"}, key = "#userId")
    public User findById(Long userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
        if (userDao.selectOne(wrapper) == null) {
            return null;
        }
        return userDao.selectOne(wrapper);
    }

    @Transactional
    @CacheEvict(value = {"UserListMap"})
    public boolean add(User user) {
        log.info("addUser.user.getUsername():[{}]", user.getUsername());
        log.info("addUser.user.getPassword():[{}]", user.getPassword());
        if (userService.UserExist(user.getUsername())) {
            return false;
        }
        Long uid = IdWorker.getId(User.class);
        user.setUid(uid);
        user.setStatus(MessageConstant.USER_ABLE);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAvatar(isImagesTrue(user.getAvatar()));
        userDao.insert(user);
        UserRole userRole = new UserRole();
        userRole.setRid(2);
        userRole.setUid(uid);
        tbUserRoleDao.insert(userRole);//赋予用户角色
        return true;
    }

    /**
     * 用户提供的图片链接无效就自动生成图片
     *
     * @param postUrl 用户传来的头像url
     * @return url
     */
    public String isImagesTrue(String postUrl) {
        if (postUrl.contains("tcefrep.oss-cn-beijing.aliyuncs.com")) {   //本人的oss地址，就无需检验图片有效性
            return postUrl;
        }
        int max = 1000;
        int min = 1;
        String picUrl = "https://unsplash.it/100/100?image=";
        try {
            URL url = new URL(postUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return postUrl;
            } else {
                Random random = new Random();
                int s = random.nextInt(max) % (max - min + 1) + min;
                return picUrl + s;
            }
        } catch (Exception e) {   // 代表图片链接无效
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            return picUrl + s;
        }
    }

    @Override
    public boolean verifyCode(String verKey, String code) {
        String realCode = (String) redisUtil.get(USER_CODE_KEY + verKey);
        redisUtil.del(USER_CODE_KEY + verKey);  // 验证码是否正确都删除，否则验证错误的验证码会存在redis中无法删除
        if (code == null || StringUtils.isEmpty(code)) {
            throw new AuthenticationServiceException("请输入验证码！");
        }
        if (realCode == null || StringUtils.isEmpty(realCode) || !code.equalsIgnoreCase(realCode)) {
            throw new AuthenticationServiceException("请输入正确的验证码！");
        }
        return true;
    }

    @CacheEvict(value = {"UserMap"}, key = "#updateUserVO.getUid()")
    public boolean updateUser(UpdateUserVO updateUserVO) {
        String realCode = (String) redisUtil.get(USER_CODE_KEY + updateUserVO.getEmail());    // 先验证邮箱验证码是否正确
        if (!realCode.equals(updateUserVO.getCode())) {
            throw new BizException("您输入的邮箱验证码不正确");
        }
        User userDB = userDao.selectById(updateUserVO.getUid());
        if (userService.UserExist(updateUserVO.getUsername()) && !userDB.getUsername().equals(updateUserVO.getUsername())) {
            return false;
        }
        if (updateUserVO.getPassword() != null && !updateUserVO.getPassword().equals("")) { // 用户更改了密码
            updateUserVO.setPassword(encoder.encode(updateUserVO.getPassword()));
        } else {
            updateUserVO.setPassword(null);
        }
        BeanCopyUtils.copyPropertiesIgnoreNull(updateUserVO, userDB);
        userDB.setUpdateTime(LocalDateTime.now());
        userDB.setStatus(MessageConstant.USER_ABLE);
        userDB.setLoginType(updateUserVO.getLoginType());
        userDB.setAvatar(isImagesTrue(updateUserVO.getAvatar()));
        userDao.updateById(userDB);
        redisUtil.del(USER_CODE_KEY + updateUserVO.getEmail());
        return true;
    }

    @Cacheable(value = {"UserListMap"})
    public List<User> getUserList() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "username", "nickname", "avatar")
                .orderByAsc("create_time");
        return userDao.selectList(wrapper);
    }

    @Override
    public User selectByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .select("username", "nickname", "avatar", "uid");
        return userDao.selectOne(wrapper);
    }

    @Override
    public Page<UserBackDTO> adminUser(QueryPageBean queryPageBean) {
//        //先把数据查出来封装到user的page中，然后再赋予到UserBackDTO的page中返回
//        Page<User> userPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "nickname", queryPageBean.getQueryString());
//        Page<User> userResultPage = userDao.selectPage(userPage, wrapper);
//        List<UserBackDTO> userBackDTOList = BeanCopyUtils.copyList(userResultPage.getRecords(), UserBackDTO.class);
        Page<UserBackDTO> userBackDTOPage = new Page<>();
        userBackDTOPage.setTotal(userDao.selectCount(wrapper));
        userBackDTOPage.setRecords(userDao.adminUser(queryPageBean));
        return userBackDTOPage;
    }

    public User login(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "username", "password", "status", "nickname", "avatar");
        wrapper.eq("username", user.getUsername());
        //登录的用户
        User login_user = userDao.selectOne(wrapper);
        log.debug("login_user:[{}]", login_user.toString());
        if (!encoder.matches(user.getPassword(), login_user.getPassword())) {
            throw new BizException("用户名或密码不正确，登录失败");
        }
        if (login_user.isStatus() == (MessageConstant.USER_DISABLE)) {
            throw new BizException("用户已被禁用,登录失败");
        }
        update(new UpdateWrapper<User>()
                .set("last_ip", user.getLastIp())
                .eq("username", login_user.getUsername()));

        return login_user;
    }

    /**
     * 统计用户地区
     */
    public void statisticalUserArea() {
        // 统计用户地域分布
        Map<String, Long> userAreaMap = userDao.selectList(new LambdaQueryWrapper<User>().select(User::getIpSource))
                .stream()
                .map(item -> {
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(CommonConst.PROVINCE, "")
                                .replaceAll(CommonConst.CITY, "");
                    }
                    return CommonConst.UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 转换格式
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisUtil.set(USER_AREA, JSON.toJSONString(userAreaList));
    }

    @Override
    public List<UserAreaDTO> listUserAreas() {
        userService.statisticalUserArea();
        List<UserAreaDTO> userAreaDTOList = new ArrayList<>();
        // 查询注册用户区域分布
        Object userArea = redisUtil.get(USER_AREA);
        if (Objects.nonNull(userArea)) {
            userAreaDTOList = JSON.parseObject(userArea.toString(), List.class);
        }
        return userAreaDTOList;
    }

    @Override
    public UserDetailDTO getUserDetail(String username, HttpServletRequest request, String ipAddress, String ipSource) {

        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (Objects.isNull(user)) {
            throw new AuthenticationServiceException(MessageConstant.USER_NOT_EXIST);
        }
        // 查询账号角色
        List<String> roleList = roleDao.listRolesByUid(user.getUid());
        UserAgent userAgent = IpUtils.getUserAgent(request);
        return UserDetailDTO.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleList(roleList)
                .loginType(user.getLoginType())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .lastIp(ipAddress)
                .ipSource(ipSource)
                .status(user.isStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }

    @CacheEvict(value = {"UserListMap"})
    @Override
    public void delete(List<Long> uidList) {
        // 先删除该用户发布的所有博客
        blogDao.delete(new LambdaQueryWrapper<Blog>().in(Blog::getUid, uidList));
        // 删除用户发表过的评论
        commentService.remove(new LambdaQueryWrapper<Comment>().in(Comment::getUid, uidList));
        // 删除用户的角色信息
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().in(UserRole::getUid, uidList));
        // 删除用户的点赞信息
        thumbsUpDao.delete(new LambdaQueryWrapper<ThumbsUp>().in(ThumbsUp::getUid, uidList));
        //删除用户的收藏信息
        favoritesDao.delete(new LambdaQueryWrapper<Favorites>().in(Favorites::getUid, uidList));
        //删除用户
        userDao.deleteBatchIds(uidList);
    }

    @Override
    public void updateUserDisable(UserDisableVO userDisableVO) {
        // 更新用户禁用状态
        User user = User.builder()
                .uid(userDisableVO.getUid())
                .status(userDisableVO.getStatus().equals(1))
                .build();
        userDao.updateById(user);
    }

    @Override
    public void sendCode(String email) {
        // 校验账号是否合法
        if (!CommonUtils.checkEmail(email)) {
            throw new BizException("请输入正确邮箱");
        }
        // 生成六位随机验证码发送
        String code = CommonUtils.getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期30分钟，请不要告诉他人哦！")
                .build();

        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, null, new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间为30分钟
        redisUtil.set(USER_CODE_KEY + email, code, CODE_EXPIRE_TIME);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(ResetPasswordVO resetPasswordVO) {
        User userDB = userDao.selectOne(new LambdaQueryWrapper<User>().select(User::getUid, User::getEmail).eq(User::getUsername, resetPasswordVO.getUsername()));
        if (userDB == null) {
            throw new BizException("该用户不存在，请重新确认");
        }
        if (!userDB.getEmail().equals(resetPasswordVO.getEmail())) { // 用户名和邮箱不匹配
            throw new BizException("您输入的用户名和邮箱不匹配！");
        }
        String realCode = (String) redisUtil.get(USER_CODE_KEY + resetPasswordVO.getEmail());    // 先验证邮箱验证码是否正确
        if (!realCode.equals(resetPasswordVO.getCode())) {
            throw new BizException("您输入的邮箱验证码不正确");
        }
        User user = new User();
        user.setUid(userDB.getUid());
        user.setStatus(MessageConstant.USER_ABLE);
        user.setPassword(encoder.encode(resetPasswordVO.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userDao.updateById(user);
    }

    @Override
    public Page<UserOnlineDTO> listOnlineUsers(QueryPageBean queryPageBean) {
        // 获取security在线session
        List<UserOnlineDTO> userOnlineDTOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .filter(item -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(queryPageBean.getQueryString()) || item.getNickname().contains(queryPageBean.getQueryString()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());

        Page<UserOnlineDTO> userOnlineDTOPage = new Page<>();
        // 执行分页
        int fromIndex = ((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        int size = queryPageBean.getPageSize();
        int toIndex = userOnlineDTOList.size() - fromIndex > size ? fromIndex + size : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex);
        userOnlineDTOPage.setRecords(userOnlineList);
        userOnlineDTOPage.setTotal(userOnlineDTOList.size());
        return userOnlineDTOPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeOnlineUser(Long uid) {
        // 获取用户session
        List<Object> userIdList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            UserDetailDTO userDetailDTO = (UserDetailDTO) item;
            return userDetailDTO.getUid().equals(uid);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userIdList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销session
        allSessions.forEach(SessionInformation::expireNow);
        redisUtil.del(TOKEN_ALLOW_LIST + uid);
    }


    @Override
    public String getToken(UserInfoDTO userInfoDTO) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("id", String.valueOf(userInfoDTO.getUid()));
        payload.put("lastIp", userInfoDTO.getLastIp());
        payload.put("username", userInfoDTO.getUsername());
        return JWTUtils.getToken(payload);
    }



    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    public UserDetailDTO getUserDetail(User user, String ipAddress, String ipSource) {
        // 更新登录信息
        userDao.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getLastIp, ipAddress)
                .set(User::isStatus, USER_ABLE)
                .set(User::getIpSource, ipSource)
                .eq(User::getUid, user.getUid()));
        // 封装信息
        return userDetailsService.convertUserDetail(user, request);
    }
}
