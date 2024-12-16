package com.Cubicheng.MyTetr.netWork.codec;

import com.Cubicheng.MyTetr.netWork.protocol.Command;
import com.Cubicheng.MyTetr.netWork.protocol.Packet;
import com.Cubicheng.MyTetr.netWork.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PacketCodec {
    private static final int MAGIC_NUMBER = 0x11451419;

    public static ByteBuf encode(Packet packet, ByteBuf out) {
        ByteBuf byteBuf = out;
        if (out == null) {
            byteBuf = Unpooled.buffer();
        }
        byte[] bytes = Serializer.DEFAULT.serilize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSeriallizerCode());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);

        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    static final int HEADER_LENGTH = 11;

    public static Packet decode(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() < HEADER_LENGTH) {
            return null;
        }
        int magicNumber = byteBuf.readInt();
        if (magicNumber != MAGIC_NUMBER) {
            return null;
        }
        int version = byteBuf.readByte();
        byte serializerCode = byteBuf.readByte();
        var serializer = Serializer.getSerializer(serializerCode);
        byte command = byteBuf.readByte();
        var requestType = Command.getRequestByCommand(command);
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return null;
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }
}
