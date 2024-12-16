package com.Cubicheng.MyTetr.netWork.protocol.serialize;

public interface Serializer {

    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();

    byte getSeriallizerCode();

    byte[] serilize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);

    static Serializer getSerializer(byte seriallizerCode) {
        switch (seriallizerCode) {
            case JSON_SERIALIZER:
                return new JSONSerializer();
            default:
                return null;
        }
    }
}
