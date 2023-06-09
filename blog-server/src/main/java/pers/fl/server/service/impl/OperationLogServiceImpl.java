package pers.fl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fl.common.dto.OperationLogDTO;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.admin.OperationLog;
import pers.fl.server.dao.admin.OperationLogDao;
import pers.fl.server.service.OperationLogService;
import pers.fl.server.utils.BeanCopyUtils;

import java.util.List;

/**
 * 操作日志服务
 *
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDao, OperationLog> implements OperationLogService {

    @Override
    public Page<OperationLogDTO> listOperationLogs(QueryPageBean queryPageBean) {
        Page<OperationLog> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询日志列表
        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), OperationLog::getOptModule, queryPageBean.getQueryString())
                .or()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), OperationLog::getOptDesc, queryPageBean.getQueryString())
                .orderByDesc(OperationLog::getId));
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
        Page<OperationLogDTO> operationLogDTOPage = new Page<>();
        operationLogDTOPage.setRecords(operationLogDTOList);
        operationLogDTOPage.setTotal((int) operationLogPage.getTotal());
        return operationLogDTOPage;
    }

}
