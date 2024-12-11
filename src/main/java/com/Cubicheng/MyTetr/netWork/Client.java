package com.Cubicheng.MyTetr.netWork;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    private NioEventLoopGroup workGroup;
    public void start() throws InterruptedException {
        workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler());

        var resultFuture = bootstrap.connect("127.0.0.1", Constants.port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败");
            }
        });
    }

    public void shutdown() {
        workGroup.shutdownGracefully();
        System.out.println("Client程序已经退出");
    }

    public static final Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }
}
