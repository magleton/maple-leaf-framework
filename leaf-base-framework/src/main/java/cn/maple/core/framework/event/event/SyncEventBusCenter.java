package cn.maple.core.framework.event.event;

import com.google.common.eventbus.EventBus;

@SuppressWarnings("unused")
public class SyncEventBusCenter {
    private static final EventBus syncEventBus = new EventBus("sync-britton");

    private SyncEventBusCenter() {
    }

    public static EventBus getInstance() {
        return syncEventBus;
    }

    public static void register(Object obj) {
        syncEventBus.register(obj);
    }

    public static void unregister(Object obj) {
        syncEventBus.unregister(obj);
    }

    public static void post(Object obj) {
        syncEventBus.post(obj);
    }
}
