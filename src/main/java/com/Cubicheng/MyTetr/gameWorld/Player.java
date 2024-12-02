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

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;
import static com.Cubicheng.MyTetr.gameWorld.Constants.BLOCK_SIZE;

public class Player {
    private Entity movablePiece, ghostPiece, holdPiece;
    private Entity gameMap;
    private Entity[] nextPiece;
    private Entity mapImageEntity;

    public Player(GameScene gameScene, GameWorld gameWorld){
        nextPiece = new Entity[5];
        var map_image = FXGL.image("map.png");

        double new_width = gameScene.getAppHeight() / map_image.getHeight() * map_image.getWidth();
        double new_height = gameScene.getAppHeight();

        var map_texture = new Texture(map_image);

        map_texture.setFitWidth(new_width);
        map_texture.setFitHeight(new_height);

        mapImageEntity = new EntityBuilder()
                .at((gameScene.getAppWidth() - new_width) / 2, 0)
                .view(map_texture)
                .zIndex(Integer.MIN_VALUE)
                .build();

        gameWorld.addEntity(mapImageEntity);

        if(!is_startXY_set) {
            startX = startX * new_width + (gameScene.getAppWidth() - new_width) / 2;
            startY = startY * new_height;
            is_startXY_set = true;
        }

        gameMap = GameMapComponent.of(new SpawnData(startX, startY + 19 * BLOCK_SIZE));
        movablePiece = MovablePieceComponent.of(new SpawnData(0, 0));
        ghostPiece = GhostPieceComponent.of(new SpawnData(0, 0));
        holdPiece = HoldPieceComponent.of(new SpawnData(startX - 3 * BLOCK_SIZE, startY + 2.4 * BLOCK_SIZE));

        System.out.println(gameMap);
        System.out.println(movablePiece);

        gameWorld.addEntities(gameMap, movablePiece, ghostPiece, holdPiece);

        for (int i = 0; i < 5; i++) {
            nextPiece[i] = NextPieceComponent.of(new SpawnData(startX + 13 * BLOCK_SIZE, startY + (3 * i + 2.4) * BLOCK_SIZE));
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
