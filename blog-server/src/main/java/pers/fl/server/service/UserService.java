package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.dto.UserAreaDTO;
import pers.fl.common.dto.UserBackDTO;
import pers.fl.common.dto.UserInfoDTO;
import pers.fl.common.dto.UserOnlineDTO;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.User;
import pers.fl.server.dto.UserDetailDTO;
import pers.fl.common.vo.ResetPasswordVO;
import pers.fl.common.vo.UpdateUserVO;
import pers.fl.common.vo.UserDisableVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author fengliang
 * @since 2021-01-26
 */
public interface UserService extends IService<User> {
    /**
     * 利用jwt登录验证
     *
     * @param user
     * @return user
     */
    User login(User user);

    /**
     * 查询用户是否存在
     *
     * @param username
     * @return
     */
    boolean UserExist(String username);

    /**
     * 根据用户id查询用户
     *
     * @param userId
     * @return user
     */
    User findById(Long userId);

    /**
     * 用户注册
     *
     * @param user user
     * @return boolean
     */
    boolean add(User user);

    /**
     * 检验验证码
     *
     * @return boolean
     */
    boolean verifyCode(String verKey, String captcha);

    /**
     * 更新用户信息
     * @param updateUserVO
     * @return boolean
     */
    boolean updateUser(UpdateUserVO updateUserVO);

    /**
     * 获取本站用户列表
     * @return userList
     */
    List<User> getUserList();

    /**
     * 获取用户分布地区
     * @return list
     */
    List<UserAreaDTO> listUserAreas();

    void statisticalUserArea();

    User selectByUsername(String username);

    /**
     * 获取后台用户列表
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<UserBackDTO> adminUser(QueryPageBean queryPageBean);

    /**
     * 获取在线用户列表
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<UserOnlineDTO> listOnlineUsers(QueryPageBean queryPageBean);

    /**
     * 获取用户信息
     * @param username
     * @param request
     * @param ipAddress
     * @param ipSource
     * @return
     */
    UserDetailDTO getUserDetail(String username, HttpServletRequest request, String ipAddress, String ipSource);

    /**
     * 修改用户禁用状态
     * @param userDisableVO 禁用状态vo
     */
    void updateUserDisable(UserDisableVO userDisableVO);

    /**
     *
     * @param uidList 被删除的用户id
     */
    void delete(List<Long> uidList);

    /**
     * 发送邮箱验证码
     * @param email 用户邮箱
     */
    void sendCode(String email);

    /**
     * 用户修改密码
     * @param resetPasswordVO 用户
     */
    void resetPassword(ResetPasswordVO resetPasswordVO);

    /**
     * 强制用户下线
     * @param uid 用户id
     */
    void removeOnlineUser(Long uid);

    /**
     * 生成token
     * @return token
     */
    String getToken(UserInfoDTO userInfoDTO);


    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    UserDetailDTO getUserDetail(User user, String ipAddress, String ipSource);

}
