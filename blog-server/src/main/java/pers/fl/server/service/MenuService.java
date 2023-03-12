package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.dto.LabelOptionDTO;
import pers.fl.common.dto.MenuDTO;
import pers.fl.common.dto.UserMenuDTO;
import pers.fl.common.po.admin.Menu;
import pers.fl.common.vo.MenuVO;

import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 获取后台管理菜单列表
     * @return list
     */
    List<MenuDTO> listMenus();

    /**
     * 获取当前用户的菜单列表
     * @param uid 用户id
     * @return list
     */
    List<UserMenuDTO> listAdminMenus(Long uid);

    /**
     * 获取当前用户的菜单列表
     * @param uid 用户id
     * @return list
     */
    List<UserMenuDTO> listUserMenus(Long uid);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);
}
