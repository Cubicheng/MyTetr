package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.GhostPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.HoldPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.NextPieceComponent;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.ImagePattern;

import static com.Cubicheng.MyTetr.gameWorld.Constants.BLOCK_SIZE;

public class Player {
    private Entity movablePiece, ghostPiece, holdPiece;
    private Entity gameMap;
    private Entity[] nextPiece;
    private Entity mapImageEntity;

    public double startX = Constants.startX;
    public double startY = Constants.startY;

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public Player(GameScene gameScene, GameWorld gameWorld, double dx, double dy) {
        nextPiece = new Entity[5];

        var map_image = FXGL.image("map.png");

        double new_width = gameScene.getAppHeight() / map_image.getHeight() * map_image.getWidth();
        double new_height = gameScene.getAppHeight();

        var map_texture = new Texture(map_image);

        map_texture.setFitWidth(new_width);
        map_texture.setFitHeight(new_height);

        mapImageEntity = new EntityBuilder()
                .at((gameScene.getAppWidth() - new_width) / 2 + dx, dy)
                .view(map_texture)
                .zIndex(Integer.MIN_VALUE)
                .build();

        gameWorld.addEntity(mapImageEntity);

        startX = startX * new_width + (gameScene.getAppWidth() - new_width) / 2 + dx;
        startY = startY * new_height + dy;

        gameMap = GameMapComponent.of(new SpawnData(startX, startY + 19 * BLOCK_SIZE).put("startX", startX).put("startY", startY));
        movablePiece = MovablePieceComponent.of(new SpawnData(0, 0).put("startX", startX).put("startY", startY));
        ghostPiece = GhostPieceComponent.of(new SpawnData(0, 0).put("startX", startX).put("startY", startY));
        holdPiece = HoldPieceComponent.of(new SpawnData(startX - 3 * BLOCK_SIZE, startY + 2.4 * BLOCK_SIZE).put("startX", startX).put("startY", startY));

        System.out.println(gameMap);
        System.out.println(movablePiece);

        gameWorld.addEntities(gameMap, movablePiece, ghostPiece, holdPiece);

        for (int i = 0; i < 5; i++) {
            nextPiece[i] = NextPieceComponent.of(new SpawnData(startX + 13 * BLOCK_SIZE, startY + (3 * i + 2.4) * BLOCK_SIZE).put("startX", startX).put("startY", startY));
            gameWorld.addEntity(nextPiece[i]);
        }
    }

    public Entity getMovablePiece() {
        return movablePiece;
    }

    public Entity getGhostPiece() {
        return ghostPiece;
    }

    public Entity getHoldPiece() {
        return holdPiece;
    }

    public Entity getGameMap() {
        return gameMap;
    }

    public Entity[] getNextPiece() {
        return nextPiece;
    }

    public Entity getMapImageEntity() {
        return mapImageEntity;
    }
}
