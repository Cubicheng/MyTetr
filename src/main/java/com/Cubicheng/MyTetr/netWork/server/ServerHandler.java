package com.Cubicheng.MyTetr.netWork.server;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.clientScene.ClientPlayScene;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.netWork.codec.PacketCodec;
import com.Cubicheng.MyTetr.netWork.protocol.StartRespondPacket;
import com.Cubicheng.MyTetr.netWork.protocol.UpdateMovablePiecePacket;
import com.Cubicheng.MyTetr.netWork.util;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import javafx.application.Platform;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        ctx.fireChannelActive();

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
        channels.remove(ctx.channel());
        ctx.fireChannelInactive();

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler收到的Packet:" + msg);
        if (msg instanceof UpdateMovablePiecePacket) {
            Platform.runLater(() -> {
                var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                Entity movablePiece = service.get_entity(Type.MovablePiece, 1);
                movablePiece.getComponent(MovablePieceComponent.class).update((UpdateMovablePiecePacket) msg);
            });
        }
    }

    public void startGame() {
        System.out.println("开始游戏");
        var startRespondPacket = new StartRespondPacket();
        var byteBuf = PacketCodec.encode(startRespondPacket, null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }

    public void update_movable_piece(UpdateMovablePiecePacket packet) {
        var byteBuf = PacketCodec.encode(packet, null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }
}