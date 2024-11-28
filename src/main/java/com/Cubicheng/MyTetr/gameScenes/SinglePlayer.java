package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.FrontlineService;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.components.OnePieceComponent;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.whitewoodcity.fxgl.app.ImageData;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.ImagePattern;

import java.util.List;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

import java.util.Optional;

public class SinglePlayer implements PushAndPopGameSubScene, FrontlineService {
    public static final String SCENE_NAME = "Single_Player";

    private GameWorld gameWorld;
    private Entity onePiece;
    private Entity gameMap;

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
                    FXGL.<GameApp>getAppCast().pop();
                }
            }
        }, KeyCode.ESCAPE);
        return new XInput(input);
    }


    @Override
    public void initUI(GameScene gameScene, XInput input) {
        var gridpane = new GridPane();

        var map_image = FXGL.image("map.png");

        double width = map_image.getWidth();
        double height = map_image.getHeight();

        double new_width = gameScene.getAppHeight() / height * width;
        double new_height = gameScene.getAppHeight();

        var map_texture = new Texture(map_image);

        map_texture.setFitWidth(new_width);
        map_texture.setFitHeight(new_height);

        var mapEntity = new EntityBuilder()
                .at((gameScene.getAppWidth() - new_width) / 2, 0)
                .view(map_texture)
                .zIndex(Integer.MIN_VALUE)
                .build();

        gameWorld.addEntity(mapEntity);

        startX = startX * new_width + (gameScene.getAppWidth() - new_width) / 2;
        startY = startY * new_height;

        var background = FXGL.image("back1.jpg");
        gameScene.setBackgroundColor(new ImagePattern(background, 0, 0, 1, 1, true));

        gameMap = GameMapComponent.of(new SpawnData(0, 0));
        gameWorld.addEntity(gameMap);

        onePiece = OnePieceComponent.of(new SpawnData(0, 0));
        gameWorld.addEntity(onePiece);

        List.of(KeyCode.RIGHT).forEach(keyCode -> input.onAction(keyCode, onePiece.getComponent(OnePieceComponent.class)::move_right));
        List.of(KeyCode.LEFT).forEach(keyCode -> input.onAction(keyCode, onePiece.getComponent(OnePieceComponent.class)::move_left));
        List.of(KeyCode.DOWN).forEach(keyCode -> input.onAction(keyCode, onePiece.getComponent(OnePieceComponent.class)::move_down));
        List.of(KeyCode.SPACE).forEach(keyCode -> input.onAction(keyCode, onePiece.getComponent(OnePieceComponent.class)::on_drop));

        List.of(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.SPACE).forEach(keyCode -> input.onActionBegin(keyCode, onePiece.getComponent(OnePieceComponent.class)::reset_is_moved));
    }

    @Override
    public GameWorld get_game_world() {
        return gameWorld;
    }
}
