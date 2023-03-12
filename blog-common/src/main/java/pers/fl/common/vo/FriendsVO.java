package pers.fl.common.vo;

import lombok.Data;
import pers.fl.common.po.Friends;

import java.io.Serializable;

@Data
public class FriendsVO extends Friends implements Serializable {
    private String username;
    private String nickname;
    private String avatar;
    private String lastContent;
    private Integer messageNum = 0; // 未读消息数量
}
