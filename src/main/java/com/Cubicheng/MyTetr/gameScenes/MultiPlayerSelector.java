package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.Background;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.clientScene.ClientWaitScene;
import com.Cubicheng.MyTetr.gameScenes.serverScene.ServerWaitScene;
import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.netWork.client.Client;
import com.Cubicheng.MyTetr.netWork.util;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class MultiPlayerSelector implements PushAndPopGameSubScene {
    public static final String SCENE_NAME = "Multi_Player_Selector";
    private GameWorld gameWorld;
    private Background background;

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
    public void initGame(GameWorld gameWorld, XInput input) {
        this.gameWorld = gameWorld;
    }
    @Override
    public void initUI(GameScene gameScene, XInput input) {
        background = new Background("menu.png", gameScene, gameWorld, 1.0);

        var gridpane = new GridPane();
        var glow = new Glow(1.0);

        var serverbtn = new Text("创建房间");
        serverbtn.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        var clientbtn = new Text("加入房间");
        clientbtn.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        var room_ip = new TextField();
        room_ip.setPromptText("请输入房间 IP");
        room_ip.setFocusTraversable(false);
        room_ip.setStyle("-fx-font-family: \"IPix\";");

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
            if (util.isNetworkConnected()) {
                FXGL.<GameApp>getAppCast().push(ServerWaitScene.SCENE_NAME);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("无法创建房间，请检查网络连接。");
                alert.initOwner(Application.getStage());
                alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
                alert.showAndWait();
            }
        });

        clientbtn.setOnMouseClicked(_2 -> {
            if (util.isNetworkConnected()) {
                Client.getInstance().setIp(room_ip.getText());
                FXGL.<GameApp>getAppCast().push(ClientWaitScene.SCENE_NAME);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("无法加入房间，请检查网络连接。");
                alert.initOwner(Application.getStage());
                alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
                alert.showAndWait();
            }
        });

        gridpane.add(serverbtn, 1, 0);
        gridpane.add(clientbtn, 1, 1);
        gridpane.add(room_ip, 1, 2);

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }
}
