package com.Cubicheng.MyTetr.netWork.client;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.clientScene.ClientPlayScene;
import com.Cubicheng.MyTetr.gameScenes.serverScene.ServerPlayScene;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.Variables;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.netWork.codec.PacketCodec;
import com.Cubicheng.MyTetr.netWork.protocol.*;
import com.Cubicheng.MyTetr.netWork.util;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import javafx.application.Platform;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        Platform.runLater(
                () -> {
                    var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                    if (service == null) return;
                    var gridpane = service.get_gridpane();
                    var text = util.get_text(gridpane, 0, 1);
                    text.setText("玩家1 退出，房间已解散...");
                }
        );

        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StartRespondPacket) {
            Platform.runLater(() -> {
                Variables.seed = ((StartRespondPacket) msg).getSeed();
                FXGL.<GameApp>getAppCast().push(ClientPlayScene.SCENE_NAME);
            });
        } else if (msg instanceof UpdateMovablePiecePacket) {
            if(FXGL.<GameApp>getAppCast().get_last_gameScene().getClass()!= ClientPlayScene.class){
                return ;
            }
            Platform.runLater(() -> {
                var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                Entity movablePiece = service.get_entity(Type.MovablePiece, 1);
                movablePiece.getComponent(MovablePieceComponent.class).update((UpdateMovablePiecePacket) msg);
            });
        } else if (msg instanceof OnHardDropPacket) {
            Platform.runLater(() -> {
                var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                Entity movablePiece = service.get_entity(Type.MovablePiece, 1);
                movablePiece.getComponent(MovablePieceComponent.class).hard_drop();
            });
        } else if (msg instanceof OnHoldPacket) {
            Platform.runLater(() -> {
                var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                Entity movablePiece = service.get_entity(Type.MovablePiece, 1);
                movablePiece.getComponent(MovablePieceComponent.class).hold();
            });
        } else if (msg instanceof AttackPacket) {
            Platform.runLater(() -> {
                System.out.println("receive attack");
                var service = FXGL.<GameApp>getAppCast().getFrontlineService();
                Entity gameMap = service.get_entity(Type.GameMap, 0);
                gameMap.getComponent(GameMapComponent.class).add_attack_to_queue(((AttackPacket) msg).getAttack(), ((AttackPacket) msg).getX());
            });
        }
    }

    public void push_UpdateMovablePiecePacket(UpdateMovablePiecePacket packet) {
        var byteBuf = PacketCodec.encode(packet, null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }

    public void push_OnHardDropPacket() {
        var byteBuf = PacketCodec.encode(new OnHardDropPacket(), null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }

    public void push_OnHoldPacket() {
        var byteBuf = PacketCodec.encode(new OnHoldPacket(), null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }

    public void push_AttackPacket(int attack, int x) {
        var byteBuf = PacketCodec.encode(new AttackPacket(attack, x), null);
        channels.writeAndFlush(byteBuf).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Send message failed: " + future.cause());
            }
        });
    }
}
