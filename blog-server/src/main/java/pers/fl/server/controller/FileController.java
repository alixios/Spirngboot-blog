package pers.fl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.fl.common.entity.Result;
import pers.fl.server.strategy.context.UploadStrategyContext;

import java.util.HashMap;

@Api("文件上传")
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private UploadStrategyContext uploadStrategyContext;


    //上传头像的方法
    @PostMapping("/userAvatar")
    @ApiOperation(value = "用户上传头像")
    public Result userAvatar(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"userAvatar/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    @PostMapping("/articleImage")
    @ApiOperation(value = "用户上传文章图片")
    public Result articleImage(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"articleImage/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

}
