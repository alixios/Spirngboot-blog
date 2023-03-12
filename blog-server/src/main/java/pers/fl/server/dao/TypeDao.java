package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.Type;
import pers.fl.common.vo.TypeVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2021-01-28
 */
@Repository
public interface TypeDao extends BaseMapper<Type> {

    /**
     * 获取每个分类的博客数量
     * @return 分类数据
     */
    List<TypeVO> getTypeCount();

    /**
     * 获取后台管理分页数据
     * @param queryPageBean 分页实体
     * @return list
     */
    List<TypeVO> adminType(@Param("queryPageBean") QueryPageBean queryPageBean);

}
