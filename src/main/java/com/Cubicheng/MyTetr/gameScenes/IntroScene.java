package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.serverScene.ServerWaitScene;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.ReplaceableGameScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.geometry.Pos;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Map;

public class IntroScene implements ReplaceableGameScene {
    public static final String SCENE_NAME = "IntroScene";

    @Override
    public XInput initInput(Map<KeyCode, Runnable> keyPresses, Map<KeyCode, Runnable> keyReleases, Map<KeyCode, Runnable> keyActions) {
        var input = new XInput(keyPresses, keyReleases, keyActions);
        List.of(KeyCode.ENTER).forEach(keyCode ->
                input.onActionBegin(keyCode, () -> {
                    FXGL.<GameApp>getAppCast().push(MainMenu.SCENE_NAME);
                })
        );
        return input;
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        gameScene.getRoot().setStyle("-fx-background-color: #000000;");

        var gridpane = new GridPane();
        var glow = new Glow(1.0);

        var text = new Text("于混沌中寻求平衡，于创造中构筑湮灭。");
        text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(50));
        text.setEffect(glow);

        var hint = new Text("——按下回车进入——");
        hint.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));
        hint.setEffect(glow);

        text.setTranslateX(FXGL.getAppCenter().getX() - text.getBoundsInLocal().getWidth() / 2);
        text.setTranslateY(FXGL.getAppCenter().getY());

        hint.setTranslateX(FXGL.getAppCenter().getX() - hint.getBoundsInLocal().getWidth() / 2);
        hint.setTranslateY(FXGL.getAppCenter().getY() + 100);

        gameScene.addUINode(text);
        gameScene.addUINode(hint);
    }
}
