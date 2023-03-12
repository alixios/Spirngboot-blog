package pers.fl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fl.common.po.admin.UserRole;
import pers.fl.server.dao.admin.TbUserRoleDao;
import pers.fl.server.service.UserRoleService;


@Service
public class UserRoleServiceImpl extends ServiceImpl<TbUserRoleDao, UserRole> implements UserRoleService {

}
