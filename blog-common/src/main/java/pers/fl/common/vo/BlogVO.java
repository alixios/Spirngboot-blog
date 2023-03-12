package pers.fl.common.vo;

import lombok.Data;
import pers.fl.common.po.Blog;

import java.io.Serializable;
import java.util.List;

/**
 * 设计博客模块查询的附加条件
 */
@Data
public class BlogVO extends Blog implements Serializable {

    private String typeName;    // 分类名称
    private String nickname;    //用户昵称
    private String avatar;      //用户头像

    /**
     * 文章标签
     */
    private List<String> tagNameList;


}
