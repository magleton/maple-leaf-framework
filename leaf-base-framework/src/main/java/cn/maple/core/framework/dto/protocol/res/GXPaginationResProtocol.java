package cn.maple.core.framework.dto.protocol.res;

import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXPaginationResProtocol<T> extends GXBaseResProtocol {
    private static final long serialVersionUID = -6977700102950116740L;

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
    private transient List<T> records;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pages      总页数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public GXPaginationResProtocol(List<T> list, long totalCount, long pages, long pageSize, long currPage) {
        this.records = list;
        this.total = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currPage;
        this.pages = pages;
    }

    /**
     * 分页
     */
    public GXPaginationResProtocol(GXPaginationResDto<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.pageSize = page.getPageSize();
        this.currentPage = page.getCurrentPage();
        this.pages = page.getPages();
    }

    /**
     * 分页
     */
    public GXPaginationResProtocol(List<T> list) {
        this.records = list;
    }
}
