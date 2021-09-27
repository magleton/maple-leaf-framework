package com.geoxus.cache;

import org.apache.ibatis.cache.CacheException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

final class JdkSerializer {
    private JdkSerializer() {
    }

    static byte[] serialize(Object obj) {
        if (obj == null) {
            return null;
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