package com.Cubicheng.MyTetr.gameScenes.serverScene;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.netWork.Server;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ServerWaitScene implements PushAndPopGameSubScene {
    public static final String SCENE_NAME = "ServerWaitScene";

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                Server.getInstance().shutdown();
                FXGL.<GameApp>getAppCast().pop();
            }
        }, KeyCode.ESCAPE);
        return new XInput(input);
    }

    private String server_ip = "127.0.0.1";

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        try {
            Server.getInstance().start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var gridpane = new GridPane();

        var server_title = new Text("房间 IP: " + server_ip);
        server_title.setFont(FXGL.getAssetLoader().loadFont("Lato-Bold.ttf").newFont(40));

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        ImageView map_image = new ImageView(FXGL.image("background.jpg"));
        map_image.setFitWidth(gameScene.getAppWidth());
        map_image.setFitHeight(gameScene.getAppHeight());

        gameScene.addUINode(map_image);

        gridpane.add(server_title, 0, 0);

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }
}
