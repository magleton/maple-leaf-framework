package cn.maple.core.framework.event.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class AsyncEventBusCenter {
    private static final AsyncEventBus asyncEventBus = new AsyncEventBus("async-britton", Executors.newFixedThreadPool(5));

    private AsyncEventBusCenter() {
    }

    public static EventBus getInstance() {
        return asyncEventBus;
    }

    public static void register(Object obj) {
        asyncEventBus.register(obj);
    }

    public static void unregister(Object obj) {
        asyncEventBus.unregister(obj);
    }

    public static void post(Object obj) {
        asyncEventBus.post(obj);
    }
}
