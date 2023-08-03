package cn.maple.core.datasource.enums;

import lombok.Getter;

@Getter
public enum GXModelEventNamingEnums {
    SAVE_ENTITY("save_entity", "saveEntity", "保存实体对象"),
    UPDATE_ENTITY("update_entity", "updateEntity", "更新实体对象"),
    UPDATE_FIELD("update_field", "updateField", "更新指定字段"),
    DELETE_SOFT("delete_soft", "deleteSoft", "通过条件软删除"),
    DELETE("delete", "delete", "通过条件物理删除");

    /**
     * 事件类型
     */
    private final String eventType;

    /**
     * 事件名字
     */
    private final String eventName;

    /**
     * 事件描述
     */
    private final String desc;

    GXModelEventNamingEnums(String eventType, String eventName, String desc) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.desc = desc;
    }
}
