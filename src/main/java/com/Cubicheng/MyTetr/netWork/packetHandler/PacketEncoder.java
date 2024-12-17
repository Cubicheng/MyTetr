package com.Cubicheng.MyTetr.netWork.packetHandler;

import com.Cubicheng.MyTetr.netWork.codec.PacketCodec;
import com.Cubicheng.MyTetr.netWork.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
//        System.out.println("PacketEncoder收到的Packet:" + msg);
        PacketCodec.encode(msg, out);
        ctx.writeAndFlush(out);
//        System.out.println("PacketEncoder最终发送出去的ByteBuf："
//                + ByteBufUtil.prettyHexDump(out));
    }
}
