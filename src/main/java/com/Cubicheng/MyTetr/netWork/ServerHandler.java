package com.Cubicheng.MyTetr.netWork;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有客户端连接" + ctx.channel().remoteAddress());
        var welcomMessageMessage = "欢迎你来到服务器".getBytes(StandardCharsets.UTF_8);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(welcomMessageMessage));
    }
}
