package pers.fl.server.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.entity.Result;
import pers.fl.common.po.Message;
import pers.fl.server.annotation.IpRequired;
import pers.fl.server.annotation.OptLog;
import pers.fl.server.service.MessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static pers.fl.common.constant.OptTypeConst.REMOVE;

/**
 * <p>
 * 留言功能的前端控制器
 * </p>
 *
 * @author fengliang
 * @since 2021-02-08
 */
@RestController
@CrossOrigin
@Api(value = "留言模块", description = "留言模块的接口信息")
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @ApiOperation("获取留言列表")
    @GetMapping("/getMessageList")
    public Result getMessageList() {
        return Result.ok("获取留言列表信息成功", messageService.getMessageList());
    }

    @ApiOperation("获取留言分页信息")
    @PostMapping("/getMessagePage")
    public Result getMessagePage(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取留言分页信息", messageService.getMessagePage(queryPageBean));
    }

    @ApiOperation("添加留言")
    @IpRequired
    @PostMapping("/add")
    public Result addMessage(@RequestBody Message message, HttpServletRequest request) {
        boolean flag = messageService.addMessage(message, (String) request.getAttribute("host"));
        if (flag) {
            return Result.ok("添加留言成功");
        } else {
            return Result.fail("添加留言失败");
        }
    }

    @OptLog(optType = REMOVE)
    @ApiOperation("删除留言")
    @DeleteMapping("/admin/delete")
    public Result deleteMessage(@RequestBody List<Long> messageIdList){
        messageService.deleteMessage(messageIdList);
        return Result.ok("删除成功");
    }

}

