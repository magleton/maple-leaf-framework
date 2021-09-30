package com.geoxus.core.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.common.constant.GXCommonConstant;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.io.StringReader;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@MappedTypes({List.class})
public class GXJsonToListTypeHandler extends BaseTypeHandler<List<Map<String, Object>>> {
    @GXFieldComment(zhDesc = "标识核心模型主键名字")
    private static final String CORE_MODEL_PRIMARY_NAME = GXCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME;

    @GXFieldComment(zhDesc = "当前字段的名字")
    private String columnName;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, Object>> parameter, JdbcType jdbcType) throws SQLException {
        final String parameterString = listToJson(parameter);
        StringReader reader = new StringReader(parameterString);
        ps.setCharacterStream(i, reader, parameterString.length());
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = "";
        int coreModelId;
        Clob clob = rs.getClob(columnName);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        coreModelId = rs.getInt(CORE_MODEL_PRIMARY_NAME);
        this.columnName = columnName;
        return jsonToList(value, coreModelId);
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnIndex);
        int coreModelId;
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        coreModelId = rs.getInt(CORE_MODEL_PRIMARY_NAME);
        return jsonToList(value, coreModelId);
    }

    @Override
    public List<Map<String, Object>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = cs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToList(value, cs.getInt(CORE_MODEL_PRIMARY_NAME));
    }

    private List<Map<String, Object>> jsonToList(String from, int coreModelId) {
        from = StrUtil.isEmpty(from) ? "[]" : from;
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
