package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.ApplicationType;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.*;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.util.Optional;

import static com.Cubicheng.MyTetr.gameWorld.Variables.BLOCK_SIZE;

public class Player {
    private Entity movablePiece, ghostPiece, holdPiece, warnPiece;
    private Entity gameMap;
    private Entity[] nextPiece;
    private Entity mapImageEntity;

    private int id;

    public double startX = Variables.startX;
    public double startY = Variables.startY;

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public void on_die() {
        if (Application.getApplicationType() == ApplicationType.None) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wasted.");
            alert.initOwner(Application.getStage());
            alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
            alert.show();
            alert.setOnHidden(evt -> {
                on_remove();
                FXGL.<GameApp>getAppCast().pop();
            });
        } else if (id == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("输！");
            alert.initOwner(Application.getStage());
            alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
            alert.show();
            alert.setOnHidden(evt -> {
                FXGL.<GameApp>getAppCast().getFrontlineService().get_player(0).on_remove();
                FXGL.<GameApp>getAppCast().getFrontlineService().get_player(1).on_remove();
                FXGL.<GameApp>getAppCast().pop();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("赢！");
            alert.initOwner(Application.getStage());
            alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
            alert.show();
            alert.setOnHidden(evt -> {
                FXGL.<GameApp>getAppCast().getFrontlineService().get_player(0).on_remove();
                FXGL.<GameApp>getAppCast().getFrontlineService().get_player(1).on_remove();
                FXGL.<GameApp>getAppCast().pop();
            });
        }
    }

    public void on_remove() {
        movablePiece.removeFromWorld();
        holdPiece.removeFromWorld();
        ghostPiece.removeFromWorld();
        warnPiece.removeFromWorld();
        for (Entity entity : nextPiece) {
            entity.removeFromWorld();
        }
        gameMap.removeFromWorld();
    }

    public Player(int player_id, GameScene gameScene, GameWorld gameWorld, double dx, double dy) {
        var map_image = FXGL.image("map.png");
        var map_warn_image = FXGL.image("map_warn.png");

        double new_width = gameScene.getAppHeight() / map_image.getHeight() * map_image.getWidth();
        double new_height = gameScene.getAppHeight();

        ImageBuffer.map_texture = new Texture(map_image);
        ImageBuffer.map_texture.setOpacity(0.7);
        ImageBuffer.map_texture.setFitWidth(new_width);
        ImageBuffer.map_texture.setFitHeight(new_height);

        ImageBuffer.map_warn_texture = new Texture(map_warn_image);
        ImageBuffer.map_warn_texture.setOpacity(0.7);
        ImageBuffer.map_warn_texture.setFitWidth(new_width);
        ImageBuffer.map_warn_texture.setFitHeight(new_height);

        this.id = player_id;

        nextPiece = new Entity[5];

        mapImageEntity = new EntityBuilder()
                .at((gameScene.getAppWidth() - new_width) / 2 + dx, dy)
                .type(Type.MapImageEntity)
                .view(ImageBuffer.map_texture)
                .zIndex(Integer.MIN_VALUE + 10)
                .build();

        gameWorld.addEntity(mapImageEntity);

        startX = startX * new_width + (gameScene.getAppWidth() - new_width) / 2 + dx;
        startY = startY * new_height + dy;

        gameMap = GameMapComponent.of(new SpawnData(startX, startY + 19 * BLOCK_SIZE).put("id", id).put("seed", Variables.seed));
        movablePiece = MovablePieceComponent.of(new SpawnData(0, 0).put("startX", startX).put("startY", startY).put("id", id));
        ghostPiece = GhostPieceComponent.of(new SpawnData(0, 0).put("startX", startX).put("startY", startY).put("id", id));
        holdPiece = HoldPieceComponent.of(new SpawnData(startX - 3 * BLOCK_SIZE, startY + 2.4 * BLOCK_SIZE).put("startX", startX).put("startY", startY).put("id", id));

        gameWorld.addEntities(gameMap, movablePiece, ghostPiece, holdPiece);

        for (int i = 0; i < 5; i++) {
            nextPiece[i] = NextPieceComponent.of(new SpawnData(startX + 13 * BLOCK_SIZE, startY + (3 * i + 2.4) * BLOCK_SIZE).put("startX", startX).put("startY", startY).put("id", id));
            gameWorld.addEntity(nextPiece[i]);
        }

        warnPiece = WarnPieceComponent.of(new SpawnData(0, 0).put("startX", startX).put("startY", startY).put("id", id));
        gameWorld.addEntity(warnPiece);
    }

    public Entity getMovablePiece() {
        return movablePiece;
    }
}
