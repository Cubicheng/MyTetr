package com.Cubicheng.MyTetr.gameScenes.serverScene;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.Background;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.GetService;
import com.Cubicheng.MyTetr.gameWorld.Player;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.netWork.server.Server;
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
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ServerWaitScene implements PushAndPopGameSubScene, GetService {
    public static final String SCENE_NAME = "ServerWaitScene";

    private GameWorld gameWorld;
    private Background background;

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
    private Text text;
    private Text play_btn;
    private GridPane gridpane;

    @Override
    public void initGame(GameWorld gameWorld, XInput input) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void initUI(GameScene gameScene, XInput input) {
        background = new Background("menu.png", gameScene, gameWorld, 1.0);
        try {
            Server.getInstance().start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        gridpane = new GridPane();

        var server_title = new Text("房间 IP: " + server_ip);
        server_title.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        text = new Text("等待 玩家2 加入...");
        text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        play_btn = new Text("");
        play_btn.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        var glow = new Glow(1.0);

        play_btn.setOnMouseExited(_1 -> {
            play_btn.setEffect(null);
        });

        play_btn.setOnMouseEntered(_1 -> {
            play_btn.setEffect(glow);
        });

        play_btn.setOnMouseClicked(_1 -> {
            FXGL.<GameApp>getAppCast().push(ServerPlayScene.SCENE_NAME);
        });

        gridpane.add(server_title, 0, 0);
        gridpane.add(text, 0, 1);
        gridpane.add(play_btn, 0, 2);

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2 + 110);
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
