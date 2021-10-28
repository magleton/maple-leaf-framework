package com.geoxus.redisson.cache;

import javax.cache.CacheException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

final class JDKSerializer {
    /**
     * 私有化构造函数
     */
    private JDKSerializer() {
    }

    /**
     * 序列化对象
     *
     * @param obj 待序列化的对象
     * @return 字节数组
     */
    static byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        try (
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bao)
        ) {
            oos.writeObject(obj);
            return bao.toByteArray();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * 反序列化对象
     *
     * @param bytes 待反序列化的对象字节数组
     * @return 反序列化的对象
     */
    static Object unSerialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (
                ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bai)
        ) {
            return ois.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }
}
