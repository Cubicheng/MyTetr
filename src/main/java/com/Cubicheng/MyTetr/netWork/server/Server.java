package com.Cubicheng.MyTetr.netWork.server;

import com.Cubicheng.MyTetr.netWork.Constants;
import com.Cubicheng.MyTetr.netWork.packetHandler.PacketDecoder;
import com.Cubicheng.MyTetr.netWork.packetHandler.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerHandler handler;

    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        handler = new ServerHandler();
        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new PacketDecoder());
                            ch.pipeline().addLast(handler);
                            //编码器应该放在管线开头
                            ch.pipeline().addFirst(new PacketEncoder());
                        }
                    });
            ChannelFuture channelFuture = boot.bind(Constants.port).sync().addListener(future1 -> {
                if (future1.isSuccess()) {
                    System.out.println("端口绑定成功");
                } else {
                    System.out.println("端口绑定失败");
                }
            });
//            channelFuture.channel().closeFuture().sync();
            //sync是阻塞行为
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        System.out.println("server程序已经退出");
    }

    private Server() {
    }


    public static final Server instance = new Server();

    public static Server getInstance(){
        return instance;
    }

    public ServerHandler getHandler() {
        return handler;
    }
}
