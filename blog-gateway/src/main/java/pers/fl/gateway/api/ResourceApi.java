package pers.fl.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 需要用feign调用blog-server的接口
 */
@FeignClient(value = "BLOG-SERVER")
public interface ResourceApi {

    //获取需要鉴权的资源
    @GetMapping("/resource/list")
    public List<String> getResourceList();

    @GetMapping("/resource/getUserResource")
    public List<String> getUserResource(@RequestParam("uid") String uid);
}
