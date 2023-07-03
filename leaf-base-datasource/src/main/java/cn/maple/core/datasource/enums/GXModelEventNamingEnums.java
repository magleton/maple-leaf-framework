package cn.maple.core.datasource.enums;

public enum GXModelEventNamingEnums {
    RETRIEVED("retrieved", ""),
    CREATING("creating", ""),
    CREATED("created", ""),
    UPDATING("updating", ""),
    UPDATED("updated", ""),
    SAVING("saving", ""),
    SAVED("saved", ""),
    DELETING("deleting", ""),
    TRASHED("trashed", ""),
    FORCE_DELETING("forceDeleting", ""),
    FORCE_DELETED("forceDeleted", ""),
    RESTORING("restoring", ""),
    RESTORED("restored", ""),
    REPLICATING("replicating", "");

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

    public String getEventName() {
        return eventName;
    }

    public String getDesc() {
        return desc;
    }
}
