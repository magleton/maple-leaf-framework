package cn.maple.core.framework.dto.protocol.res;

import cn.maple.core.framework.annotation.GXFieldComment;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXPaginationProtocol<T> extends GXBaseResProtocol {
    private static final long serialVersionUID = -6977700102950116740L;

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
    public GXPaginationProtocol(List<T> list, long totalCount, long pageSize, long currPage) {
        this.records = list;
        this.total = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currPage;
        this.pages = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public GXPaginationProtocol(GXPaginationResDto<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.pageSize = page.getPageSize();
        this.currentPage = page.getCurrentPage();
        this.pages = page.getPages();
    }

    /**
     * 分页
     */
    public GXPaginationProtocol(List<T> list) {
        this.records = list;
    }
}
