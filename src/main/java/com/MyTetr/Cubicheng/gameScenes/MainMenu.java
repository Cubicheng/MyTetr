package com.MyTetr.Cubicheng.gameScenes;


import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.ReplaceableGameScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Map;

import com.MyTetr.Cubicheng.GameApp;

public class MainMenu implements ReplaceableGameScene {
    public static final String SCENE_NAME = "MainMenu";

    @Override
    public XInput initInput(Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions) {
        var input = new XInput(keyPresses, keyReleases, keyActions);


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

        multiPlayerbtn.setOnMouseClicked(_2 -> {
            FXGL.<GameApp>getAppCast().push(MultiPlayerSelector.SCENE_NAME);
        });

        gridpane.add(singlePlayerbtn, 1, 0);
        gridpane.add(multiPlayerbtn, 1, 1);

        gridpane.setTranslateX(FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth() * 2 / 3);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        ImageView map_image = new ImageView(FXGL.image("background.jpg"));
        map_image.setFitWidth(gameScene.getAppWidth());
        map_image.setFitHeight(gameScene.getAppHeight());

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(map_image);

        gameScene.addUINode(gridpane);
    }
}
