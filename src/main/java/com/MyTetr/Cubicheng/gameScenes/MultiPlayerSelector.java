package com.MyTetr.Cubicheng.gameScenes;

import com.MyTetr.Cubicheng.GameApp;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;

import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class MultiPlayerSelector implements PushAndPopGameSubScene {
    public static final String SCENE_NAME = "Multi_Player_Selector";

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                FXGL.<GameApp>getAppCast().pop();
            }
        }, KeyCode.ESCAPE);
        return new XInput(input);
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        var gridpane = new GridPane();
        var glow = new Glow(1.0);

        var serverbtn = new Text("Create a room");
        serverbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50));

        var clientbtn = new Text("Join a room");
        clientbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(50));

        serverbtn.setOnMouseExited(_1 -> {
            serverbtn.setEffect(null);
        });

        serverbtn.setOnMouseEntered(_1 -> {
            serverbtn.setEffect(glow);
        });

        clientbtn.setOnMouseExited(_1 -> {
            clientbtn.setEffect(null);
        });

        clientbtn.setOnMouseEntered(_1 -> {
            clientbtn.setEffect(glow);
        });

        serverbtn.setOnMouseClicked(_1 -> {
            FXGL.<GameApp>getAppCast().push(MultiPlayer.SCENE_NAME);
        });

        clientbtn.setOnMouseClicked(_2 -> {
            FXGL.<GameApp>getAppCast().push(MultiPlayer.SCENE_NAME);
        });

        gridpane.add(serverbtn, 1, 0);
        gridpane.add(clientbtn, 1, 1);

        gridpane.setTranslateX(FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth() * 2 / 3);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        ImageView map_image = new ImageView(FXGL.image("background.jpg"));
        map_image.setFitWidth(gameScene.getAppWidth());
        map_image.setFitHeight(gameScene.getAppHeight());

        gameScene.addUINode(map_image);

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }
}
