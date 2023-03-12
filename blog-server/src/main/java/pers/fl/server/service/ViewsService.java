package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.dto.ViewsDTO;
import pers.fl.common.po.Views;

import java.util.List;

public interface ViewsService extends IService<Views> {

    /**
     * 获取七天全站浏览量数据
     * @return list
     */
    List<ViewsDTO> getViewsData();

}
