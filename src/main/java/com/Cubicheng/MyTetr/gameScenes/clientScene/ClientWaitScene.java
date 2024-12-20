package com.Cubicheng.MyTetr.gameScenes.clientScene;

import com.Cubicheng.MyTetr.Background;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.GetService;
import com.Cubicheng.MyTetr.gameWorld.Player;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.netWork.client.Client;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ClientWaitScene implements PushAndPopGameSubScene, GetService {
    public static final String SCENE_NAME = "ClientWaitScene";

    private GameWorld gameWorld;
    private Background background;

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                Client.getInstance().shutdown();
                FXGL.<GameApp>getAppCast().pop();
            }
        }, KeyCode.ESCAPE);
        return new XInput(input);
    }

    private GridPane gridpane;
    private Text client_title;
    private Text text;

    @Override
    public void initGame(GameWorld gameWorld, XInput input) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        background = new Background("menu.png", gameScene, gameWorld, 1.0);

        Client.getInstance().start();

        gridpane = new GridPane();

        client_title = new Text("房间 IP: " + Client.getInstance().getIp());
        client_title.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        text = new Text("连接成功！等待 玩家1 开始...");
        text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        gridpane.add(client_title, 0, 0);
        gridpane.add(text, 0, 1);

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2 + 210);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() * 1.2);

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }

    @Override
    public GameWorld get_game_world() {
        return null;
    }

    @Override
    public Entity get_entity(Type type, int id) {
        return null;
    }
    
    @Override
    public GridPane get_gridpane() {
        return gridpane;
    }

    @Override
    public Player get_player(int id) {
        return null;
    }
}
