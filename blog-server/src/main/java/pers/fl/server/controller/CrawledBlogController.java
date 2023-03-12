package pers.fl.server.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.entity.Result;
import pers.fl.server.service.CrawledBlogService;

import javax.annotation.Resource;

/**
 * <p>
 * 爬取的博客页面控制器
 * </p>
 *
 * @author fengliang
 * @since 2021-03-14
 */
@RestController
@CrossOrigin
@RequestMapping("/crawler")
public class CrawledBlogController {
    @Resource
    private CrawledBlogService crawledBlogService;

    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/crawlerPage")
    public Result crawlerPage(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取分页数据成功", crawledBlogService.crawlerPage(queryPageBean));
    }

    @GetMapping("/{blogId}")
    public Result getOneBlog(@PathVariable("blogId") Long blogId) {
        return Result.ok("获取博客信息成功", crawledBlogService.getOneBlog(blogId));
    }
}

