package cn.maple.core.datasource.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.io.StringReader;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@MappedTypes({List.class})
public class GXJSONToListTypeHandler extends BaseTypeHandler<List<Map<String, Object>>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, Object>> parameter, JdbcType jdbcType) throws SQLException {
        final String parameterString = listToJson(parameter);
        StringReader reader = new StringReader(parameterString);
        ps.setCharacterStream(i, reader, parameterString.length());
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnName);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToList(value);
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToList(value);
    }

    @Override
    public List<Map<String, Object>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = cs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToList(value);
    }

    private List<Map<String, Object>> jsonToList(String from) {
        from = CharSequenceUtil.isEmpty(from) ? "[]" : from;
        if (!JSONUtil.isJson(from) || (JSONUtil.isJsonObj(from) && JSONUtil.parseObj(from).isEmpty())) {
            return Collections.emptyList();
        }
        if (JSONUtil.isJsonObj(from)) {
            from = '[' + from + ']';
        }
        Dict dict = Dict.create();
        final JSONArray jsonArray = JSONUtil.parseArray(from);
        for (Object object : jsonArray) {
            for (String attribute : dict.keySet()) {
                ((JSONObject) object).remove(attribute);
            }
        }
        return Convert.convert(new TypeReference<>() {
        }, jsonArray);
    }

    private String listToJson(List<Map<String, Object>> from) {
        return JSONUtil.toJsonStr(from);
    }
}
