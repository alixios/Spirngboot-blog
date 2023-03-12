package pers.fl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.entity.Result;
import pers.fl.server.service.BlogService;

import javax.annotation.Resource;

/**
 * 分类展示模块
 *
 * @author fengliang 2021年01月31日
 */

@Api(value = "分类展示模块", description = "分类展示模块的接口信息")
@RequestMapping("/typeShow")
@RestController
@CrossOrigin
public class TypeShowController {
    @Resource
    private BlogService blogService;

    @ApiOperation(value = "根据分类分页展示", notes = "返回分页数据")
    @PostMapping("/getById")
    public Result getByTypeId(@RequestBody QueryPageBean queryPageBean) {
        if (queryPageBean.getTypeId() == null)
            return Result.ok("获取分类信息成功", blogService.findHomePage(queryPageBean));
        return Result.ok("根据id获取分类信息成功", blogService.getByTypeId(queryPageBean));
    }
}
