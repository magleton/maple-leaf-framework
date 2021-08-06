package com.geoxus.core.common.vo.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GXPagination<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @GXFieldCommentAnnotation(zhDesc = "总记录数")
    private long total;

    @GXFieldCommentAnnotation(zhDesc = "每页记录数")
    private long pageSize;

    @GXFieldCommentAnnotation(zhDesc = "总页数")
    private long pages;

    @GXFieldCommentAnnotation(zhDesc = "当前页数")
    private long page;

    @GXFieldCommentAnnotation(zhDesc = "列表数据")
    private transient List<T> records;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public GXPagination(List<T> list, long totalCount, long pageSize, long currPage) {
        this.records = list;
        this.total = totalCount;
        this.pageSize = pageSize;
        this.page = currPage;
        this.pages = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public GXPagination(Page<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.pageSize = page.getSize();
        this.page = page.getCurrent();
        this.pages = page.getPages();
    }

    /**
     * 分页
     */
    public GXPagination(IPage<T> page) {
        this.records = page.getRecords();
        this.total = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.page = (int) page.getCurrent();
        this.pages = (int) page.getPages();
    }

    /**
     * 分页
     */
    public GXPagination(List<T> list) {
        this.records = list;
    }
}
