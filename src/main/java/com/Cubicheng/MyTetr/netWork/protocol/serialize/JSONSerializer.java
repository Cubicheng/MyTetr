package com.Cubicheng.MyTetr.netWork.protocol.serialize;

import com.alibaba.fastjson2.JSON;

public class JSONSerializer implements Serializer {
    @Override
    public byte getSeriallizerCode() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serilize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
