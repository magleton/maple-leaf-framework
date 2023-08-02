package cn.maple.core.datasource.enums;

import lombok.Getter;

@Getter
public enum GXModelEventNamingEnums {
    SAVE_ENTITY("save_entity", "保存实体对象"),
    UPDATE_ENTITY("update_entity", "更新实体对象"),
    UPDATE_FIELD("update_field", "更新指定字段"),
    DELETE_SOFT("delete_soft", "通过条件软删除");

    /**
     * 事件名字
     */
    private final String eventName;

    /**
     * 事件描述
     */
    private final String desc;

    GXModelEventNamingEnums(String eventName, String desc) {
        this.eventName = eventName;
        this.desc = desc;
    }

}
