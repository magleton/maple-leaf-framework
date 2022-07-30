package cn.maple.core.datasource.dto;

/**
 * 数据范围过滤
 *
 * @author 塵渊 britton@126.com
 */
public class GXDataFilterInnerDto {
    private String sqlFilter;

    public GXDataFilterInnerDto(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    public String getSqlFilter() {
        return sqlFilter;
    }

    public void setSqlFilter(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    @Override
    public String toString() {
        return this.sqlFilter;
    }
}