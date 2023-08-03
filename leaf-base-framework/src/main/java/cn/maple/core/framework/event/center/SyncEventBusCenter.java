package cn.maple.core.framework.event.center;

import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

@SuppressWarnings("unused")
public class SyncEventBusCenter {
    private static final String APPLICATION_NAME = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class);

    private static final Integer CPU_CORE_NUMBER = Runtime.getRuntime().availableProcessors();

    private static final EventBus SYNC_EVENT_BUS = new EventBus(new SubscriberExceptionHandler() {
        /**
         * Handles exceptions thrown by subscribers.
         *
         * @param exception
         * @param context
         */
        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            if (exception instanceof Exception) {
                throw (RuntimeException) exception;
            }
        }
    });

    private SyncEventBusCenter() {
    }

    public static EventBus getInstance() {
        return SYNC_EVENT_BUS;
    }

    public static void register(Object obj) {
        SYNC_EVENT_BUS.register(obj);
    }

    public static void unregister(Object obj) {
        SYNC_EVENT_BUS.unregister(obj);
    }

    public static void post(Object obj) {
        SYNC_EVENT_BUS.post(obj);
    }
}
