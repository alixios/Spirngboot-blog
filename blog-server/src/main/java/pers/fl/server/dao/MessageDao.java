package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.Message;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2021-02-08
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {

}
