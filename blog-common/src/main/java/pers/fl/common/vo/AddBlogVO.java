package pers.fl.common.vo;

import lombok.Data;
import pers.fl.common.po.Blog;

@Data
public class AddBlogVO extends Blog {
    private Integer[] value; // 存放的是博客对应的标签列表
}
