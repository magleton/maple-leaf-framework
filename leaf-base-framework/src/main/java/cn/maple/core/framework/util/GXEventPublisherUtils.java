package cn.maple.core.framework.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.event.event.AsyncEventBusCenter;
import cn.maple.core.framework.event.event.SyncEventBusCenter;
import cn.maple.core.framework.exception.GXBusinessException;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class GXEventPublisherUtils {
    /**
     * 缓存已经注册的GUAVA事件监听器
     */
    private static final ConcurrentHashMap<String, String> EVENT_BUS_REGISTER_CACHE = new ConcurrentHashMap<>(1024);

    private GXEventPublisherUtils() {
    }

    /**
     * 派发SpringBoot事件
     * 异步事件可以通过在监听器上面添加@Async注解实现, 但需要开启SpringBoot的异步功能
     * {@code @EnableAsync}
     *
     * @param event 事件对象
     */
    public static <T> void publishEvent(GXBaseEvent<T> event) {
        GXSpringContextUtils.getApplicationContext().publishEvent(event);
    }

    /**
     * 派发Guava异步事件
     *
     * @param event         事件对象
     * @param listenerClazz 监听器的类型
     */
    public static <T> void publishGuavaAsyncEvent(GXBaseEvent<T> event, Class<?> listenerClazz) {
        AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
        Object listener = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(listener)) {
            throw new GXBusinessException("指定的监听类型不存在");
        }
        String key = listenerClazz.getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isEmpty(s)) {
            asyncEventBus.register(listener);
            EVENT_BUS_REGISTER_CACHE.put(key, listenerClazz.getSimpleName());
        }
        asyncEventBus.post(event);
    }

    /**
     * 派发Guava同步事件
     *
     * @param event         事件对象
     * @param listenerClazz 监听器的类型
     */
    public static <T> void publishGuavaSyncEvent(GXBaseEvent<T> event, Class<?> listenerClazz) {
        EventBus eventBus = SyncEventBusCenter.getInstance();
        Object listener = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(listener)) {
            throw new GXBusinessException("指定的监听类型不存在");
        }
        String key = listenerClazz.getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isEmpty(s)) {
            eventBus.register(listener);
            EVENT_BUS_REGISTER_CACHE.put(key, listenerClazz.getSimpleName());
        }
        eventBus.post(event);
    }

    /**
     * 派发Guava异步事件
     *
     * @param event    事件对象
     * @param listener 监听器的对象
     */
    public static <T> void publishGuavaAsyncEvent(GXBaseEvent<T> event, Object listener) {
        AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
        String key = listener.getClass().getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isEmpty(s)) {
            asyncEventBus.register(listener);
            EVENT_BUS_REGISTER_CACHE.put(key, listener.getClass().getSimpleName());
        }
        asyncEventBus.post(event);
    }

    /**
     * 派发Guava同步事件
     *
     * @param event    事件对象
     * @param listener 监听器的对象
     */
    public static <T> void publishGuavaSyncEvent(GXBaseEvent<T> event, Object listener) {
        EventBus eventBus = SyncEventBusCenter.getInstance();
        String key = listener.getClass().getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isEmpty(s)) {
            eventBus.register(listener);
            EVENT_BUS_REGISTER_CACHE.put(key, listener.getClass().getSimpleName());
        }
        eventBus.post(event);
    }

    /**
     * 销毁Guava异步事件监听器
     *
     * @param listener 监听器的对象
     */
    public static void unregisterGuavaAsyncEventObserver(Object listener) {
        String key = listener.getClass().getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isNotEmpty(s)) {
            AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
            asyncEventBus.unregister(listener);
            EVENT_BUS_REGISTER_CACHE.remove(key);
        }
    }

    /**
     * 销毁Guava同步事件监听器
     *
     * @param listener 监听器的对象
     */
    public static void unregisterGuavaSyncEventObserver(Object listener) {
        String key = listener.getClass().getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isNotEmpty(s)) {
            EventBus eventBus = SyncEventBusCenter.getInstance();
            eventBus.unregister(listener);
            EVENT_BUS_REGISTER_CACHE.remove(key);
        }
    }

    /**
     * 销毁Guava异步事件监听器
     *
     * @param listenerClazz 监听器的类型
     */
    public static void unregisterGuavaAsyncEventObserver(Class<?> listenerClazz) {
        Object listener = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(listener)) {
            return;
        }
        String key = listenerClazz.getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isNotEmpty(s)) {
            AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
            asyncEventBus.unregister(listener);
            EVENT_BUS_REGISTER_CACHE.remove(key);
        }
    }

    /**
     * 销毁Guava同步事件监听器
     *
     * @param listenerClazz 监听器的类型
     */
    public static void unregisterGuavaSyncEventObserver(Class<?> listenerClazz) {
        Object listener = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(listener)) {
            return;
        }
        String key = listenerClazz.getName();
        String s = EVENT_BUS_REGISTER_CACHE.get(key);
        if (CharSequenceUtil.isNotEmpty(s)) {
            EventBus eventBus = SyncEventBusCenter.getInstance();
            eventBus.unregister(listener);
            EVENT_BUS_REGISTER_CACHE.remove(key);
        }
    }
}
