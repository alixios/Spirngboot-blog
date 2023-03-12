package pers.fl.server.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fl.common.po.admin.Menu;

/**
 * <p>
 * 菜单Mapper 接口
 * </p>
 *
 * @author fengliang
 * @since 2022-01-14
 */
@Repository
public interface MenuDao extends BaseMapper<Menu> {

}
