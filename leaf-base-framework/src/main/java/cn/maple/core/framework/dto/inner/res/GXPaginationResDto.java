package cn.maple.core.framework.dto.inner.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("all")
public class GXPaginationResDto<T> extends GXBaseResDto {
    /**
     * 总记录数
     */
    private long total;

    /**
     * 每页记录数
     */
    private long pageSize;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 当前页数
     */
    private long currentPage;

    /**
     * 列表数据
     */
    private List<T> records;

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
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pages      总页数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public GXPaginationResDto(List<T> list, long totalCount, long pages, long pageSize, long currPage) {
        this.records = list;
        this.total = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currPage;
        this.pages = pages;
    }

    /**
     * 分页
     */
    public GXPaginationResDto(List<T> list) {
        this.records = list;
    }
}
