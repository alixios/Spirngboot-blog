package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fl.common.dto.ViewsDTO;
import pers.fl.common.po.Views;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2021-02-16
 */
@Repository
public interface ViewsDao extends BaseMapper<Views> {

    List<ViewsDTO> getViewsData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
