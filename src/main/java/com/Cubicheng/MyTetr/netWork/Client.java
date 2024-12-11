package com.Cubicheng.MyTetr.netWork;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Constant;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Client {

    private String ip;

    private boolean isConnected = false;

    private NioEventLoopGroup workGroup;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp(){
        return ip;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void start(){
        workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler());

        var resultFuture = bootstrap.connect(ip, Constants.port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
                isConnected = true;
            } else {
                System.out.println("连接失败");
            }
        });

    }

    public void shutdown() {
        workGroup.shutdownGracefully();
        ip = "255.255.255.255";
        isConnected = false;
        System.out.println("Client程序已经退出");
    }

    public static final Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }
}
