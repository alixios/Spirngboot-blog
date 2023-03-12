package pers.fl.server.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fl.common.dto.ViewsDTO;
import pers.fl.common.po.Views;
import pers.fl.server.dao.ViewsDao;
import pers.fl.server.service.ViewsService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 访问量服务实现类
 * </p>
 *
 * @author fengliang
 * @since 2021-02-18
 */
@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsDao, Views> implements ViewsService {
    @Resource
    private ViewsDao viewsDao;

    @Cacheable(value = {"viewsData"}, key = "#root.methodName")
    public List<ViewsDTO> getViewsData() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return viewsDao.getViewsData(startTime, endTime);
    }
}
