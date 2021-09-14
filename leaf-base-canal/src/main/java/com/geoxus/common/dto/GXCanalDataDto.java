package com.geoxus.common.dto;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GXCanalDataDto extends GXBaseDto {
    /**
     * 新数据
     */
    private List<Dict> data;

    /**
     * 主键的字段名字
     */
    private Long id;

    /**
     * 数据库名字
     */
    private String database;

    /**
     * 时间戳
     */
    private Long es;

    /**
     * 是否DDL修改
     */
    private Boolean isDdl;

    /**
     * 数据库类型
     * {
     * "id": "int(11)",
     * "content": "varchar(255)"
     * }
     */
    private Dict mysqlType;

    /**
     * 旧数据
     */
    private List<Dict> old;

    /**
     * 数据表的主键ID列表
     * "pkNames": ["id"]
     */
    private List<String> pkNames;

    /**
     * SQL语句
     */
    private String sql;

    /**
     * SQL字段的数据类型
     * {
     * "id": 4,
     * "content": 12
     * }
     */
    private Dict sqlType;

    /**
     * 数据表的名字
     */
    private String table;

    /**
     * 事务时间
     */
    private Long ts;

    /**
     * 操作类型
     * UPDATE、DELETE、INSERT
     */
    private String type;
}
