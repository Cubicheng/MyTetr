package com.Cubicheng.MyTetr.netWork;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.almasb.fxgl.dsl.FXGL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Constant;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Client {

    private String ip;

    private NioEventLoopGroup workGroup;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void start() {
        workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler());

        var resultFuture = bootstrap.connect(ip, Constants.port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败");
                Platform.runLater(
                        () -> {
                            FXGL.<GameApp>getAppCast().pop();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("无法连接服务器，可能是房间未创建或者是 IP 地址输入错误。");
                            alert.initOwner(Application.getStage());
                            alert.showAndWait();
                        }
                );
            }
        });

    }

    public void shutdown() {
        workGroup.shutdownGracefully();
        ip = "255.255.255.255";
        System.out.println("Client程序已经退出");
    }

    public static final Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }
}
