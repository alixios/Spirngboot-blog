package pers.fl.server.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.entity.Result;
import pers.fl.server.service.ArchivesService;

import javax.annotation.Resource;

/**
 * 归档模块
 *
 * @author fengliang 2021年01月26日
 */

@Api(value = "归档模块", description = "归档模块的接口信息")
@RequestMapping("/archives")
@RestController
@CrossOrigin
public class ArchivesController {

    @Resource
    private ArchivesService archivesService;

    @GetMapping("/getArchivesList")
    @ApiOperation(value = "获取归档列表")
    public Result getArchivesList() {
        return Result.ok("获取归档信息成功", archivesService.getArchivesList());
    }
}

