package com.Cubicheng.MyTetr.netWork;

import com.Cubicheng.MyTetr.GameApp;
import com.almasb.fxgl.dsl.FXGL;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        var messageData = "你好 netty server!".getBytes(StandardCharsets.UTF_8);
//        ctx.writeAndFlush(Unpooled.wrappedBuffer(messageData));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Platform.runLater(
                () -> {
                    var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                    if (service == null) return;
                    var gridpane = service.get_gridpane();
                    var text = util.get_text(gridpane, 0, 1);
                    text.setText("玩家1 退出，房间已解散...");
                }
        );
    }
}
