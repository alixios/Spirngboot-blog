package pers.fl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fl.common.dto.UserBackDTO;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.User;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2021-01-26
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    @Select("UPDATE user set password = #{password} where username = 'test1' ")
    void updatePassword(Long uid, String password);

    List<UserBackDTO> adminUser(@Param("queryPageBean") QueryPageBean queryPageBean);
}
