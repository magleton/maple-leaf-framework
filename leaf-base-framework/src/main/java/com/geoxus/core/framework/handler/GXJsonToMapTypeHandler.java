package com.geoxus.core.framework.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.core.framework.constant.GXFrameWorkCommonConstant;
import com.geoxus.core.framework.service.GXCoreModelAttributePermissionService;
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
    @GXFieldComment(zhDesc = "标识核心模型主键名字")
    private static final String CORE_MODEL_PRIMARY_NAME = GXFrameWorkCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME;

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
        int coreModelId = 0;
        try {
            coreModelId = rs.getInt(CORE_MODEL_PRIMARY_NAME);
        } catch (SQLException e) {
            log.info("本条记录中,字段{}不存在!", CORE_MODEL_PRIMARY_NAME);
        }
        this.columnName = columnName;
        return jsonToMap(value, coreModelId);
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = rs.getClob(columnIndex);
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToMap(value, rs.getInt(CORE_MODEL_PRIMARY_NAME));
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = "";
        Clob clob = cs.getClob(columnIndex);
        int coreModelId = 0;
        try {
            coreModelId = cs.getInt(CORE_MODEL_PRIMARY_NAME);
        } catch (SQLException e) {
            log.info(e.getMessage());
        }
        if (clob != null) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return jsonToMap(value, coreModelId);
    }

    private Map<String, Object> jsonToMap(String from, int coreModelId) {
        from = CharSequenceUtil.isEmpty(from) ? "{}" : from;
        if (coreModelId == 0) {
            if (JSONUtil.isJson(from)) {
                Dict dict = JSONUtil.toBean(from, Dict.class);
                Dict retDict = Dict.create();
                dict.forEach((key, value) -> retDict.set(CharSequenceUtil.toCamelCase(key), value));
                return retDict;
            }
            return Dict.create();
        }
        GXCoreModelAttributePermissionService coreModelAttributePermissionService = GXSpringContextUtil.getBean(GXCoreModelAttributePermissionService.class);
        assert coreModelAttributePermissionService != null;
        Dict tmpDict = coreModelAttributePermissionService.getModelAttributePermissionByCoreModelId(coreModelId, Dict.create());
        final Dict jsonFieldDict = Convert.convert(Dict.class, tmpDict.getObj("json_field"));
        Dict dict = Dict.create();
        if (null != jsonFieldDict && !jsonFieldDict.isEmpty() && null != jsonFieldDict.getObj(this.columnName)) {
            dict = Convert.convert(Dict.class, jsonFieldDict.getObj(this.columnName));
        }
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
