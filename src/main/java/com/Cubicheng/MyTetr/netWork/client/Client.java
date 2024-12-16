package com.Cubicheng.MyTetr.netWork.client;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.netWork.Constants;
import com.Cubicheng.MyTetr.netWork.packetHandler.PacketDecoder;
import com.Cubicheng.MyTetr.netWork.packetHandler.PacketEncoder;
import com.almasb.fxgl.dsl.FXGL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Client {

    private String ip;

    private ClientHandler handler;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    private NioEventLoopGroup workGroup;

    public ClientHandler getHandler() {
        return handler;
    }

    public void start() {
        Platform.runLater(() -> {
            Application.setIs_server(false);
        });

        handler = new ClientHandler();

        workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(handler);
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

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
                            alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
                            alert.showAndWait();
                        }
                );
            }
        });

    }

    public void shutdown() {
        Platform.runLater(() -> {
            Application.setIs_server(true);
        });

        workGroup.shutdownGracefully();
        ip = "255.255.255.255";
        System.out.println("Client程序已经退出");
    }

    public static final Client instance = new Client();

    public static Client getInstance() {
        return instance;
    }

}
