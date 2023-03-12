package pers.fl.server.controller.admapi;

import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.constant.MessageConstant;
import pers.fl.common.constant.RedisConst;
import pers.fl.common.entity.Result;
import pers.fl.common.po.User;
import pers.fl.common.utils.JWTUtils;
import pers.fl.common.vo.UserVO;
import pers.fl.server.annotation.IpRequired;

import pers.fl.server.service.ResourceService;
import pers.fl.server.service.UserService;
import pers.fl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

import static pers.fl.common.constant.RedisConst.HOUR;
import static pers.fl.common.constant.RedisConst.USER_CODE_KEY;


/**
 * <p>
 * 信息管理控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@CrossOrigin
@RestController
@Slf4j
@Api(value = "信息管理模块", description = "管理用户信息验证")
public class IndexAdminController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResourceService resourceService;

    @ApiOperation(value = "普通用户登录接口")
    @PostMapping("/admapi/login")
    @IpRequired
    public Result login(@RequestBody UserVO userVO, HttpServletRequest request) {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        userService.verifyCode(userVO.getVerKey(), userVO.getCode()); // 验证如果不通过，后台直接抛异常
        log.info("用户名:[{}]", user.getUsername());
        request.getSession().setAttribute("username", user.getUsername());   // 给websocket取出
        log.info("密码:[{}]", user.getPassword());
        try {
            //认证成功，生成jwt令牌
            user.setLastIp((String) request.getAttribute("host"));
            User userDB = userService.login(user);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(userDB.getUid()));
            payload.put("lastIp", userDB.getLastIp());
            payload.put("username", userDB.getUsername());
            String token = JWTUtils.getToken(payload);
            redisUtil.set(RedisConst.TOKEN_ALLOW_LIST + userDB.getUid(), token, HOUR);   // token设置白名单，因此可以管理token的有效期
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("token", token);
            userInfo.put("user", userDB);
            return Result.ok("token生成成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("token生成失败,请检查你的账号与密码是否匹配");
    }

    @ApiOperation(value = "验证码")
    @RequestMapping("/admapi/captcha")
    public Result captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为10分钟
        redisUtil.set(USER_CODE_KEY + key, verCode, 600);
        request.getSession().setAttribute("CAPTCHA", verCode);  //存入session
        HashMap<String, Object> code = new HashMap<>();
        code.put("verKey", key);
        code.put("verCode", specCaptcha.toBase64());
        // 将key和base64返回给前端
        return Result.ok(MessageConstant.VERIFICATION_CODE_SUCCESS, code);
    }

    @ApiOperation(value = "注销登录接口")
    @PostMapping("/admapi/logout")
    public Result logout() {
        return Result.ok("登出成功");
    }


}

