package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fl.common.dto.OperationLogDTO;
import pers.fl.common.entity.QueryPageBean;
import pers.fl.common.po.admin.OperationLog;

/**
 * 操作日志服务
 *
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查询日志列表
     *
     * @param queryPageBean 条件
     * @return 日志列表
     */
    Page<OperationLogDTO> listOperationLogs(QueryPageBean queryPageBean);

}
