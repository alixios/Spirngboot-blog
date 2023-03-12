package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.CrawledBlog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengliang
 * @since 2021-03-14
 */
public interface CrawledBlogService extends IService<CrawledBlog> {

    Page<CrawledBlog> crawlerPage(QueryPageBean queryPageBean);

    CrawledBlog getOneBlog(Long blogId);
}
