package pers.fl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fl.common.po.GroupChat;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupChatVO extends GroupChat implements Serializable {
    private String avatar;  // 用户头像
    private String username;
    private String nickname;
    private Long uid;
    private Integer type;

    /**
     * 转换后的时间
     */
    private String formatTime;
}
