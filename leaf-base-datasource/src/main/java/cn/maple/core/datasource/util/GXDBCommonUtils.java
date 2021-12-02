package cn.maple.core.datasource.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.constant.GXBuilderConstant;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings({"unused"})
public class GXDBCommonUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXDBCommonUtils.class);

    private GXDBCommonUtils() {
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam       请求参数
     * @param key                添加的key
     * @param value              添加的value
     * @param returnRequestParam 是否返回requestParam
     * @return Dict
     */
    public static Dict addSearchCondition(Dict requestParam, String key, Object value, boolean returnRequestParam) {
        final Object obj = requestParam.getObj(GXBuilderConstant.SEARCH_CONDITION_NAME);
        if (null == obj) {
            return requestParam;
        }
        final Dict data = Convert.convert(Dict.class, obj);
        data.set(key, value);
        if (returnRequestParam) {
            requestParam.put(GXBuilderConstant.SEARCH_CONDITION_NAME, data);
            return requestParam;
        }
        return data;
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam       请求参数
     * @param sourceData         需要添加的map
     * @param returnRequestParam 是否返回requestParam
     * @return Dict
     */
    public static Dict addSearchCondition(Dict requestParam, Dict sourceData, boolean returnRequestParam) {
        final Object obj = requestParam.getObj(GXBuilderConstant.SEARCH_CONDITION_NAME);
        if (null == obj) {
            return requestParam;
        }
        final Dict data = Convert.convert(Dict.class, obj);
        data.putAll(sourceData);
        if (returnRequestParam) {
            requestParam.put(GXBuilderConstant.SEARCH_CONDITION_NAME, data);
            return requestParam;
        }
        return data;
    }

    /**
     * 获取实体的表名字
     *
     * @param clazz Class
     * @return String
     */
    public static String getTableName(Class<?> clazz) {
        final TableName annotation = AnnotationUtil.getAnnotation(clazz, TableName.class);
        if (null != annotation) {
            return annotation.value();
        }
        return "";
    }

    /**
     * 获取分页对象信息
     *
     * @param page    分页对象
     * @param records 分页数据
     * @return GXPagination
     */
    public static <R> GXPaginationResDto<R> convertPageToPaginationResDto(IPage<R> page, List<R> records) {
        long pages = page.getPages();
        long currentPage = page.getCurrent();
        long pageSize = page.getSize();
        long totalCount = page.getTotal();
        return new GXPaginationResDto<>(records, totalCount, pages, pageSize, currentPage);
    }

    /**
     * 获取分页对象信息
     *
     * @param page 分页对象
     * @return GXPagination
     */
    public static <R> GXPaginationResDto<R> convertPageToPaginationResDto(IPage<R> page) {
        long pages = page.getPages();
        long currentPage = page.getCurrent();
        long pageSize = page.getSize();
        long totalCount = page.getTotal();
        return new GXPaginationResDto<>(page.getRecords(), totalCount, pages, pageSize, currentPage);
    }
}
