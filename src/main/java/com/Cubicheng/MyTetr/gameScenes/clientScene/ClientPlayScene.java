package com.Cubicheng.MyTetr.gameScenes.clientScene;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.GetService;
import com.Cubicheng.MyTetr.gameWorld.Player;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.KeyTrigger;
import com.almasb.fxgl.input.TriggerListener;
import com.almasb.fxgl.input.UserAction;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;

import java.util.List;
import java.util.Optional;

public class ClientPlayScene implements PushAndPopGameSubScene, GetService {
    public static final String SCENE_NAME = "Client_Play";

    private GameWorld gameWorld;
    private Player player0, player1;

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("警告");
                alert.setHeaderText("确认退出吗？");
                alert.setContentText("你将会失去你的进度。");
                alert.initOwner(Application.getStage());
                alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    player1.getMovablePiece().removeFromWorld();
                    player1.getHoldPiece().removeFromWorld();
                    player1.getGhostPiece().removeFromWorld();
                    for (Entity entity : player1.getNextPiece()) {
                        entity.removeFromWorld();
                    }
                    player1.getGameMap().removeFromWorld();
                    FXGL.<GameApp>getAppCast().pop();
                }
            }
        }, KeyCode.ESCAPE);

        input.addTriggerListener(new TriggerListener() {
            @Override
            protected void onKeyBegin(KeyTrigger keyTrigger) {
                if (keyTrigger.getKey() == KeyCode.SHIFT) {
                    player0.getMovablePiece().getComponent(MovablePieceComponent.class).hold();
                }
            }
        });
        return new XInput(input);
    }

    @Override
    public void initGame(GameWorld gameWorld, XInput input) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        var background = FXGL.image("back3.jpg");
        gameScene.setBackgroundColor(new ImagePattern(background, 0, 0, 1, 1, true));

        player0 = new Player(0, gameScene, gameWorld, -290, 0);
        player1 = new Player(1, gameScene, gameWorld, 290, 0);

        List.of(KeyCode.SPACE)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::hard_drop));

        List.of(KeyCode.LEFT)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, () -> {
                            player0.getMovablePiece().getComponent(MovablePieceComponent.class).move_left();
                            player0.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_left_begin();
                        })
                        .onActionEnd(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::on_move_left_end));

        List.of(KeyCode.RIGHT)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, () -> {
                            player0.getMovablePiece().getComponent(MovablePieceComponent.class).move_right();
                            player0.getMovablePiece().getComponent(MovablePieceComponent.class).on_move_right_begin();
                        })
                        .onActionEnd(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::on_move_right_end));

        List.of(KeyCode.DOWN)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::on_move_down_begin)
                        .onActionEnd(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::on_move_down_end));

        List.of(KeyCode.UP)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::right_rotate));

        List.of(KeyCode.Z)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::left_rotate));

        List.of(KeyCode.A)
                .forEach(keyCode -> input
                        .onActionBegin(keyCode, player0.getMovablePiece().getComponent(MovablePieceComponent.class)::double_ratate));

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
    public GridPane get_gridpane() {
        return null;
    }
}