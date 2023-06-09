package pers.fl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.entity.Result;
import pers.fl.server.service.BlogService;
import pers.fl.server.service.BlogTagService;

import javax.annotation.Resource;

/**
 * 分类展示模块
 *
 * @author fengliang 2021年01月31日
 */

@Api(value = "标签展示模块", description = "标签展示模块的接口信息")
@RequestMapping("/tagShow")
@RestController
@CrossOrigin
public class TagShowController {
    @Resource
    private BlogService blogService;
    @Resource
    private BlogTagService tagService;

    @ApiOperation(value = "根据标签类型分页展示", notes = "返回分页数据")
    @PostMapping("/getById")
    public Result getByTagId(@RequestBody QueryPageBean queryPageBean) {
        if (queryPageBean.getTagId() == null) {
            return Result.ok("获取标签信息成功", blogService.findHomePage(queryPageBean));
        }
        return Result.ok("根据id获取标签信息成功", tagService.getByTagId(queryPageBean));
    }

}
