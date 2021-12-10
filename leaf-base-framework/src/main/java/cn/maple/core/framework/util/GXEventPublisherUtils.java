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

public class GXEventPublisherUtils {
    /**
     * 缓存已经注册的GUAVA事件监听器
     */
    private static final ConcurrentHashMap<Object, String> REGISTER_CACHE = new ConcurrentHashMap<>(256);

    private GXEventPublisherUtils() {
    }

    /**
     * 派发SpringBoot事件
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
        Object bean = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(bean)) {
            throw new GXBusinessException("指定的监听类型不存在");
        }
        String s = REGISTER_CACHE.get(bean);
        if (CharSequenceUtil.isEmpty(s)) {
            asyncEventBus.register(bean);
            REGISTER_CACHE.put(bean, listenerClazz.getName());
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
        Object bean = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(bean)) {
            throw new GXBusinessException("指定的监听类型不存在");
        }
        String s = REGISTER_CACHE.get(bean);
        if (CharSequenceUtil.isEmpty(s)) {
            eventBus.register(Objects.requireNonNull(GXSpringContextUtils.getBean(listenerClazz)));
            REGISTER_CACHE.put(bean, listenerClazz.getName());
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
        String s = REGISTER_CACHE.get(listener);
        if (CharSequenceUtil.isEmpty(s)) {
            asyncEventBus.register(listener);
            REGISTER_CACHE.put(listener, listener.getClass().getName());
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
        String s = REGISTER_CACHE.get(listener);
        if (CharSequenceUtil.isEmpty(s)) {
            eventBus.register(listener);
            REGISTER_CACHE.put(listener, listener.getClass().getName());
        }
        eventBus.post(event);
    }

    /**
     * 销毁Guava异步事件监听器
     *
     * @param listener 监听器的对象
     */
    public static void unregisterGuavaAsyncEventObserver(Object listener) {
        String s = REGISTER_CACHE.get(listener);
        if (CharSequenceUtil.isNotEmpty(s)) {
            AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
            asyncEventBus.unregister(listener);
            REGISTER_CACHE.remove(listener);
        }
    }

    /**
     * 销毁Guava同步事件监听器
     *
     * @param listener 监听器的对象
     */
    public static void unregisterGuavaSyncEventObserver(Object listener) {
        String s = REGISTER_CACHE.get(listener);
        if (CharSequenceUtil.isNotEmpty(s)) {
            EventBus eventBus = SyncEventBusCenter.getInstance();
            eventBus.unregister(listener);
            REGISTER_CACHE.remove(listener);
        }
    }

    /**
     * 销毁Guava异步事件监听器
     *
     * @param listenerClazz 监听器的类型
     */
    public static void unregisterGuavaAsyncEventObserver(Class<?> listenerClazz) {
        Object bean = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(bean)) {
            return;
        }
        String s = REGISTER_CACHE.get(bean);
        if (CharSequenceUtil.isNotEmpty(s)) {
            AsyncEventBus asyncEventBus = AsyncEventBusCenter.getInstance();
            asyncEventBus.unregister(Objects.requireNonNull(GXSpringContextUtils.getBean(listenerClazz)));
            REGISTER_CACHE.remove(bean);
        }
    }

    /**
     * 销毁Guava同步事件监听器
     *
     * @param listenerClazz 监听器的类型
     */
    public static void unregisterGuavaSyncEventObserver(Class<?> listenerClazz) {
        Object bean = GXSpringContextUtils.getBean(listenerClazz);
        if (Objects.isNull(bean)) {
            return;
        }
        String s = REGISTER_CACHE.get(bean);
        if (CharSequenceUtil.isNotEmpty(s)) {
            EventBus eventBus = SyncEventBusCenter.getInstance();
            eventBus.unregister(Objects.requireNonNull(GXSpringContextUtils.getBean(listenerClazz)));
            REGISTER_CACHE.remove(bean);
        }
    }
}
