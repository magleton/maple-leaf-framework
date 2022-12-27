package cn.maple.core.datasource.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.dto.GXDataFilterInnerDto;
import cn.maple.core.datasource.util.GXDataFilterThreadLocalUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Objects;

/**
 * 数据过滤
 *
 * @author 塵渊 britton@126.com
 */
@Slf4j
public class GXDataFilterInterceptor implements InnerInterceptor {
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        GXDataFilterInnerDto scope = getDataScope();
        // 不进行数据过滤
        if (Objects.isNull(scope) || CharSequenceUtil.isBlank(scope.getSqlFilter())) {
            return;
        }

        // 拼接新SQL
        String buildSql = getSelect(boundSql.getSql(), scope);

        // 重写SQL
        PluginUtils.mpBoundSql(boundSql).sql(buildSql);
        log.info("重写数据范围SQL");
    }

    private GXDataFilterInnerDto getDataScope() {
        // 判断参数里是否有DataScope对象
        GXDataFilterInnerDto dataScope = GXDataFilterThreadLocalUtils.getDataFilterInnerDto();
        if (Objects.nonNull(dataScope)) {
            return dataScope;
        }

        return null;
    }

    private String getSelect(String buildSql, GXDataFilterInnerDto scope) {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(buildSql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            Expression expression = plainSelect.getWhere();
            if (expression == null) {
                plainSelect.setWhere(new StringValue(scope.getSqlFilter()));
            } else {
                AndExpression andExpression = new AndExpression(expression, new StringValue(scope.getSqlFilter()));
                plainSelect.setWhere(andExpression);
            }

            return select.toString();
        } catch (JSQLParserException e) {
            return buildSql;
        }
    }
}