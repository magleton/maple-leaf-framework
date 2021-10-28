package com.geoxus.core.framework.dto.inner.res;

import com.geoxus.core.framework.annotation.GXFieldComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXPaginationResDto<T> extends GXBaseResDto {
    @GXFieldComment(zhDesc = "总记录数")
    private long total;

    @GXFieldComment(zhDesc = "每页记录数")
    private long pageSize;

    @GXFieldComment(zhDesc = "总页数")
    private long pages;

    @GXFieldComment(zhDesc = "当前页数")
    private long currentPage;

    @GXFieldComment(zhDesc = "列表数据")
    private transient List<T> records;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public GXPaginationResDto(List<T> list, long totalCount, long pageSize, long currPage) {
        this.records = list;
        this.total = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currPage;
        this.pages = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public GXPaginationResDto(List<T> list) {
        this.records = list;
    }
}
