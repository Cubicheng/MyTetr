package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.clientScene.ClientWaitScene;
import com.Cubicheng.MyTetr.gameScenes.serverScene.ServerWaitScene;
import com.Cubicheng.MyTetr.netWork.Client;
import com.Cubicheng.MyTetr.netWork.util;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;

import io.netty.util.concurrent.Future;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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

        var serverbtn = new Text("创建房间");
        serverbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(40));

        var clientbtn = new Text("加入房间");
        clientbtn.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(40));

        var room_ip = new TextField();
        room_ip.setPromptText("请输入房间 IP");
        room_ip.setFocusTraversable(false);

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
                alert.showAndWait();
            }
        });

        clientbtn.setOnMouseClicked(_2 -> {
            Client.getInstance().setIp(room_ip.getText());
            Client.getInstance().start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Client.getInstance().isConnected());
            if (Client.getInstance().isConnected()) {
                FXGL.<GameApp>getAppCast().push(ClientWaitScene.SCENE_NAME);
            } else {
                Client.getInstance().shutdown();
                room_ip.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("无法连接服务器，可能是房间未创建或者是 IP 地址输入错误。");
                alert.initOwner(Application.getStage());
                alert.showAndWait();
            }
        });

        gridpane.add(serverbtn, 1, 0);
        gridpane.add(clientbtn, 1, 1);
        gridpane.add(room_ip, 1, 2);

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2);
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
