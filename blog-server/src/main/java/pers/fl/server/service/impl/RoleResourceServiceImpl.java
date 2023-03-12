package pers.fl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fl.common.po.admin.RoleResource;
import pers.fl.server.dao.admin.TbRoleResourceDao;
import pers.fl.server.service.RoleResourceService;

/**
 * 角色资源服务
 *
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<TbRoleResourceDao, RoleResource> implements RoleResourceService {


}
