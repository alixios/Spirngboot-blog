package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.ChatLog;
import pers.fl.common.vo.ChatLogVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2021-04-12
 */
@Repository
public interface ChatLogDao extends BaseMapper<ChatLog> {

    /**
     * 获取某用户的聊天记录
     * @return list
     */
    List<ChatLog> getMessage(@Param("chatLogVO") ChatLogVO chatLogVO);
}
