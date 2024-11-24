package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import com.almasb.fxgl.dsl.FXGL;

import java.util.Optional;

public class MultiPlayer implements PushAndPopGameSubScene{
    public static final String SCENE_NAME = "Multi_Player";

    private GameWorld gameWorld;
    private Entity player;


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

        ImageView map_image = new ImageView(FXGL.image("map.png"));

        double width = map_image.getBoundsInLocal().getWidth();
        double height = map_image.getBoundsInLocal().getHeight();

        double new_width = gameScene.getAppHeight()/height*width;
        double new_height = gameScene.getAppHeight();

        map_image.setFitWidth(new_width);
        map_image.setFitHeight(new_height);

        map_image.setLayoutX((gameScene.getAppWidth() - new_width)/2);
        map_image.setLayoutY(0);

        ImageView background = new ImageView(FXGL.image("background.jpg"));
        background.setFitWidth(gameScene.getAppWidth());
        background.setFitHeight(gameScene.getAppHeight());

        gameScene.addUINode(background);

        gameScene.addUINode(map_image);
    }
}