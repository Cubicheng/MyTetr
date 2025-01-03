package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.serverScene.ServerWaitScene;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.ReplaceableGameScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class IntroScene implements ReplaceableGameScene {
    public static final String SCENE_NAME = "IntroScene";

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        gameScene.getRoot().setStyle("-fx-background-color: #000000;");

        var gridpane = new GridPane();
        var glow = new Glow(1.0);

        var text = new Text("于混沌中寻求平衡，于创造中构筑湮灭。");
        text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(50));
        text.setEffect(glow);

        var hint = new Text("——Cubicheng 出品——");
        hint.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));
        hint.setEffect(glow);

        text.setTranslateX(FXGL.getAppCenter().getX() - text.getBoundsInLocal().getWidth() / 2);
        text.setTranslateY(FXGL.getAppCenter().getY());

        hint.setTranslateX(FXGL.getAppCenter().getX() - hint.getBoundsInLocal().getWidth() / 2);
        hint.setTranslateY(FXGL.getAppCenter().getY() + 100);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXGL.<GameApp>getAppCast().push(MainMenu.SCENE_NAME);
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);

        gameScene.addUINode(text);
        gameScene.addUINode(hint);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), gameScene.getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        fadeTransition.play();
    }
}
