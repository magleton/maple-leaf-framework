package cn.maple.core.datasource.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.annotation.GXFieldComment;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.io.StringReader;
import java.sql.*;
import java.util.Map;

@MappedTypes({Map.class})
@Slf4j
public class GXJsonToMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    @GXFieldComment(zhDesc = "当前字段的名字")
    private String columnName;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        final String parameterString = mapToJson(parameter);
        StringReader reader = new StringReader(parameterString);
        ps.setCharacterStream(i, reader, parameterString.length());
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnName);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        this.columnName = columnName;
        return jsonToMap(value);
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToMap(value);
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = cs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToMap(value);
    }

    private Map<String, Object> jsonToMap(String from) {
        from = CharSequenceUtil.isEmpty(from) ? "{}" : from;
        Dict dict = Dict.create();
        final Dict map = Convert.convert(Dict.class, JSONUtil.toBean(from, Dict.class));
        for (String attribute : dict.keySet()) {
            map.remove(attribute);
        }
        Dict retDict = Dict.create();
        map.forEach((key, value) -> retDict.set(CharSequenceUtil.toCamelCase(key), value));
        return retDict;
    }

    private String mapToJson(Map<String, Object> from) {
        return JSONUtil.toJsonStr(from);
    }
}
