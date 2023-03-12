package pers.fl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.fl.common.po.Blog;

public interface ArchivesService {
    /**
     * 博客归档数据
     * @return
     */
    Page<Blog> getArchivesList();
}
