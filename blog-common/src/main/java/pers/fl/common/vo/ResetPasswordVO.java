package pers.fl.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2022-02-28
 */
@Data
public class ResetPasswordVO implements Serializable {
    private String username;

    private String password;

    private String email;

    private String code; // 邮箱验证码
}
