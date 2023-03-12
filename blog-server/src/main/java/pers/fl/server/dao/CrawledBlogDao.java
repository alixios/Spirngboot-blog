package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.CrawledBlog;

@Repository
public interface CrawledBlogDao extends BaseMapper<CrawledBlog> {
}
