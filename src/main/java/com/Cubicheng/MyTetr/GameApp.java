package com.Cubicheng.MyTetr;

import com.Cubicheng.MyTetr.gameScenes.MainMenu;
import com.Cubicheng.MyTetr.gameScenes.MultiPlayer;
import com.Cubicheng.MyTetr.gameScenes.MultiPlayerSelector;
import com.Cubicheng.MyTetr.gameScenes.SinglePlayer;
import com.Cubicheng.MyTetr.gameWorld.PropertyKey;
import com.almasb.fxgl.app.GameSettings;
import com.whitewoodcity.fxgl.app.GameApplication;
import com.whitewoodcity.fxgl.app.ReplaceableGameSceneBuilder;
import com.whitewoodcity.fxgl.dsl.FXGL;
import com.whitewoodcity.fxgl.service.ReplaceableGameScene;
import com.whitewoodcity.fxgl.service.XGameScene;
import javafx.stage.Screen;
import com.Cubicheng.MyTetr.gameWorld.*;

public class GameApp extends GameApplication {

    public GameApp(String initSceneName, ReplaceableGameSceneBuilder sceneBuilder) {
        super(initSceneName, sceneBuilder);
    }

    public GameApp(String initSceneName) {
        super(initSceneName);
    }

    @Override
    @SuppressWarnings("all")
    protected void initSettings(GameSettings settings) {
        this.logoString = "Demo";
        settings.setHeight(1000);
        settings.setWidth((int) (Screen.getPrimary().getBounds().getWidth() / Screen.getPrimary().getBounds().getHeight() * 1000));
        settings.setTitle(logoString);
        settings.setMainMenuEnabled(false);
        settings.setGameMenuEnabled(false);
        settings.setFontUI("BlackOpsOne-Regular.ttf");
//    settings.setSceneFactory(new SceneFactory() {
//      @Override
//      public LoadingScene newLoadingScene() {
//        return new LoadingScene(getLoadingBackgroundFill(), getTheme().textColor);
//      }
//    });
    }

    @Override
    public Object push(String sceneName, Object... parameters) {
        var app = super.push(sceneName, parameters);

        if (app instanceof ReplaceableGameScene) {
            FXGL.set(PropertyKey.SCENE_NAME, sceneName);
        }

        return app;
    }

    @Override
    protected XGameScene getGameSceneByName(String sceneName, Object... parameters) {
        return switch (sceneName) {
            case MainMenu.SCENE_NAME -> new MainMenu();
            case SinglePlayer.SCENE_NAME -> new SinglePlayer();
            case MultiPlayerSelector.SCENE_NAME -> new MultiPlayerSelector();
            case MultiPlayer.SCENE_NAME -> new MultiPlayer();
            default -> throw new RuntimeException("Wrong GameScene type");
        };
    }

    public GetService getFrontlineService() {
        var app = get_last_gameScene();
        if (app instanceof GetService getService) {
            return getService;
        } else return null;
    }

    public Object get_last_gameScene(){
        return gameScenes.getLast();
    }
}
