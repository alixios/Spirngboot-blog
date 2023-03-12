package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.dto.RoleDTO;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.admin.Role;
import pers.fl.common.vo.RoleVO;
import pers.fl.common.vo.UserRoleVO;

import java.util.List;

public interface RoleService extends IService<Role> {

    /**
     * 获取后台角色数据
     * @param queryPageBean 分页实体
     * @return list
     */
    Page<RoleDTO> listRoles(QueryPageBean queryPageBean);

    /**
     * 获取后台全部角色数据
     * @return list
     */
    List<Role> listAllRoles();

    /**
     * 后台修改用户角色
     * @param userRoleVO 用户更新信息
     */
    void updateUserRole(UserRoleVO userRoleVO);

    /**
     * 编辑角色信息
     * @param roleVO 角色信息
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);
}
