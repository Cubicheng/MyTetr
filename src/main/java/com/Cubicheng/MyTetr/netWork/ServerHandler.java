package com.Cubicheng.MyTetr.netWork;

import com.Cubicheng.MyTetr.GameApp;
import com.almasb.fxgl.dsl.FXGL;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("有客户端连接" + ctx.channel().remoteAddress());
//        var welcomMessageMessage = "欢迎你来到服务器".getBytes(StandardCharsets.UTF_8);
//        ctx.writeAndFlush(Unpooled.wrappedBuffer(welcomMessageMessage));

        Platform.runLater(
                () -> {
                    var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                    if (service == null) return;
                    var gridpane = service.get_gridpane();
                    var text = util.get_text(gridpane, 0, 1);
                    text.setText("玩家2 已就绪，可以开玩！");

                    var play_btn = util.get_text(gridpane, 0, 2);
                    play_btn.setText("点击开始游戏");
                }
        );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Platform.runLater(
                () -> {
                    var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                    if (service == null) return;
                    var gridpane = service.get_gridpane();
                    var text = util.get_text(gridpane, 0, 1);
                    text.setText("等待 玩家2 加入...");

                    var play_btn = util.get_text(gridpane, 0, 2);
                    play_btn.setText("");
                }
        );
    }
}
