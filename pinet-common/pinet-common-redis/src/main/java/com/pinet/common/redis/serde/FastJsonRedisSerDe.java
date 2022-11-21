package com.pinet.common.redis.serde;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pinet.core.util.CharsetUtils;
import lombok.SneakyThrows;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class FastJsonRedisSerDe<T> implements RedisSerializer<T> {

    private Class<T> clazz;

    public FastJsonRedisSerDe(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @SneakyThrows
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null)
            return new byte[0];
        // 自省
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(CharsetUtils.UTF_8);
    }

    @SneakyThrows
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return JSON.parseObject(new String(bytes, CharsetUtils.UTF_8), clazz);
    }
}
