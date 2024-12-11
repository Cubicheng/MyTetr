package com.Cubicheng.MyTetr.netWork;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        var messageData = "你好 netty server!".getBytes(StandardCharsets.UTF_8);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(messageData));
    }
}
