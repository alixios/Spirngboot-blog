package pers.fl.server.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pers.fl.common.dto.UserLoginDTO;
import pers.fl.common.entity.Result;
import pers.fl.common.po.User;
import pers.fl.common.utils.JWTUtils;
import pers.fl.server.dao.UserDao;
import pers.fl.server.utils.BeanCopyUtils;
import pers.fl.server.utils.RedisUtil;
import pers.fl.server.utils.UserUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


import static pers.fl.common.constant.RedisConst.HOUR;
import static pers.fl.common.constant.RedisConst.TOKEN_ALLOW_LIST;
import static pers.fl.common.enums.StatusCodeEnum.*;

/**
 * 登录成功处理
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Resource
    private UserDao userDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 返回登录信息
        User user = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), User.class);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        try {
            //认证成功，生成jwt令牌
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(user.getUid()));
            payload.put("lastIp", user.getLastIp());
            payload.put("username", user.getUsername());
            String token = JWTUtils.getToken(payload);
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            BeanUtils.copyProperties(user, userLoginDTO);
            userLoginDTO.setUid(String.valueOf(user.getUid()));
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("token", token);
            userInfo.put("user", userLoginDTO);
            redisUtil.set(TOKEN_ALLOW_LIST + user.getUid(), token, HOUR);   // token设置白名单，因此可以管理token的有效期
            // 这里的JsonSerialize不起作用，所以要手动将Long类型的uid转换成String，否则会失去精度
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result(true, SUCCESS.getCode(), "token生成成功", userInfo)));
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result(false, FAIL.getCode(), "token生成失败")));
        }
        // 更新用户ip，最近登录时间
        updateUserInfo();
    }

    /**
     * 更新用户信息
     */
    @Async
    public void updateUserInfo() {
        User user = new User();
        user.setUid(UserUtils.getLoginUser().getUid());
        user.setLastIp(UserUtils.getLoginUser().getLastIp());
        user.setIpSource(UserUtils.getLoginUser().getIpSource());
        user.setStatus(true);
        user.setLastLoginTime(UserUtils.getLoginUser().getLastLoginTime());
        userDao.updateById(user);
    }

}
