package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.Order;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2022-05-08
 */
@Repository
public interface OrderDao extends BaseMapper<Order> {
}
