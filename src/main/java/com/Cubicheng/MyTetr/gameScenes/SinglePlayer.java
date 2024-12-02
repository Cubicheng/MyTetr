package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.GetService;
import com.Cubicheng.MyTetr.gameWorld.Player;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.*;
import com.Cubicheng.MyTetr.gameWorld.components.piece.HoldPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.GhostPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.NextPieceComponent;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.texture.Texture;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.ImagePattern;

import java.util.List;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

import java.util.Optional;

public class SinglePlayer implements PushAndPopGameSubScene, GetService {
    public static final String SCENE_NAME = "Single_Player";

    private GameWorld gameWorld;
    private Player player;

    @Override
    public void initGame(GameWorld gameWorld, XInput input) {
        this.gameWorld = gameWorld;
    }

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Sure to forgive?");
                alert.setContentText("You 'll lose your progress.");
                alert.initOwner(Application.getStage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    player.getMovablePiece().removeFromWorld();
                    player.getHoldPiece().removeFromWorld();
                    player.getGhostPiece().removeFromWorld();
                    for (Entity entity : player.getNextPiece()) {
                        entity.removeFromWorld();
                    }
                    player.getGameMap().removeFromWorld();
                    FXGL.<GameApp>getAppCast().pop();
                }
            }
        }, KeyCode.ESCAPE);

        input.addAction(new UserAction("Hard_Drop") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).hard_drop();
            }
        }, KeyCode.SPACE);

        input.addAction(new UserAction("Left") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_left_begin();
                player.getMovablePiece().getComponent(MovablePieceComponent.class).move_left();
            }

            @Override
            protected void onActionEnd() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_left_end();
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Right") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_right_begin();
                player.getMovablePiece().getComponent(MovablePieceComponent.class).move_right();
            }

            @Override
            protected void onActionEnd() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_right_end();
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Down") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_down_begin();
            }

            @Override
            protected void onActionEnd() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_down_end();
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("rRotate") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).right_rotate();
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("lRotate") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).left_rotate();
            }
        }, KeyCode.Z);

        input.addAction(new UserAction("doubleRotate") {
            @Override
            protected void onActionBegin() {
                player.getMovablePiece().getComponent(MovablePieceComponent.class).double_ratate();
            }
        }, KeyCode.A);


        input.addTriggerListener(new TriggerListener() {
            @Override
            protected void onKeyBegin(KeyTrigger keyTrigger) {
                if (keyTrigger.getKey() == KeyCode.SHIFT) {
                    player.getMovablePiece().getComponent(MovablePieceComponent.class).hold();
                }
            }
        });
        return new XInput(input);
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {

        var gridpane = new GridPane();

        var background = FXGL.image("back2.jpg");
        gameScene.setBackgroundColor(new ImagePattern(background, 0, 0, 1, 1, true));

        player = new Player(gameScene,gameWorld);
    }

    @Override
    public GameWorld get_game_world() {
        return gameWorld;
    }

    @Override
    public Entity get_entity(Type type, int id) {
        return get_game_world().getEntitiesByType(type).get(id);
    }

    @Override
    public Entity get_entity(Type type) {
        return get_game_world().getEntitiesByType(type).getFirst();
    }
}
