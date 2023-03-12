package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.GroupChat;
import pers.fl.common.vo.GroupChatVO;

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
public interface GroupChatDao extends BaseMapper<GroupChat> {

    /**
     * 获取群聊记录
     * @param rows 条数
     * @return list
     */
    List<GroupChatVO> getMessage(Integer rows);
}
