package com.Cubicheng.MyTetr.gameScenes;


import com.Cubicheng.MyTetr.Application;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.ReplaceableGameScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.Cubicheng.MyTetr.GameApp;

public class MainMenu implements ReplaceableGameScene {
    public static final String SCENE_NAME = "MainMenu";

    @Override
    public XInput initInput(Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions) {
        var input = new XInput(keyPresses, keyReleases, keyActions);
        List.of(KeyCode.ESCAPE).forEach(keyCode ->
                input.onActionBegin(keyCode, () -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Sure to leave?");
                    alert.setContentText("You 'll lose your progress.");
                    alert.initOwner(Application.getStage());
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        System.exit(0);
                    }
                })
        );

        return input;
    }

    @Override
    public void initGame(GameWorld gameWorld, XInput input) {
        FXGL.getAssetLoader().clearCache();
        FXGL.getWorldProperties().clear();
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        var gridpane = new GridPane();
        var glow = new Glow(1.0);

        var singlePlayerbtn = new Text("Singleplayer");
        singlePlayerbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50));

        var multiPlayerbtn = new Text("Multiplayer");
        multiPlayerbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50));

        singlePlayerbtn.setOnMouseExited(_1 -> {
            singlePlayerbtn.setEffect(null);
        });

        singlePlayerbtn.setOnMouseEntered(_1 -> {
            singlePlayerbtn.setEffect(glow);
        });

        multiPlayerbtn.setOnMouseExited(_1 -> {
            multiPlayerbtn.setEffect(null);
        });

        multiPlayerbtn.setOnMouseEntered(_1 -> {
            multiPlayerbtn.setEffect(glow);
        });

        singlePlayerbtn.setOnMouseClicked(_1 -> {
            FXGL.<GameApp>getAppCast().push(SinglePlayer.SCENE_NAME);
        });

        multiPlayerbtn.setOnMouseClicked(_1 -> {
            FXGL.<GameApp>getAppCast().push(MultiPlayerSelector.SCENE_NAME);
        });

        gridpane.add(singlePlayerbtn, 1, 0);
        gridpane.add(multiPlayerbtn, 1, 1);

        gridpane.setTranslateX(FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth() * 2 / 3);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        var background = FXGL.image("background.jpg");
        gameScene.setBackgroundColor(new ImagePattern(background, 0, 0, 1, 1, true));

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }
}
