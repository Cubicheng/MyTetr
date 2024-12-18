package com.Cubicheng.MyTetr;

import com.Cubicheng.MyTetr.gameScenes.MainMenu;
import com.whitewoodcity.atlantafx.base.theme.CityDark;
import com.whitewoodcity.fxgl.service.FillService;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.awt.*;

public class Application extends javafx.application.Application implements FillService {
    static Stage stage;
    static private ApplicationType applicationType = ApplicationType.None;

    static public ApplicationType getApplicationType() {
        return applicationType;
    }

    static public void setApplicationType(ApplicationType applicationType){
        Application.applicationType = applicationType;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        javafx.application.Application.setUserAgentStylesheet(new CityDark().getUserAgentStylesheet());

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        stage.setWidth(width / 4);
        stage.setHeight(height / 4);

        var stackpane = new StackPane();
        stage.setScene(new Scene(stackpane));

        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.SHORTCUT_DOWN));
        stage.setFullScreenExitHint("");

        stage.show();

        var gamePane = GameApp.embeddedLaunch(new GameApp(MainMenu.SCENE_NAME));

        gamePane.setRenderFill(Color.TRANSPARENT);
        stackpane.getChildren().add(gamePane);

        gamePane.prefWidthProperty().bind(stage.getScene().widthProperty());
        gamePane.prefHeightProperty().bind(stage.getScene().heightProperty());
        gamePane.renderWidthProperty().bind(stage.getScene().widthProperty());
        gamePane.renderHeightProperty().bind(stage.getScene().heightProperty());
    }

    public static final Stage getStage() {
        return stage;
    }

    public static void main(String... args) {
        System.setProperty("prism.lcdtext", "false");
        Application.launch(Application.class, args);
    }
}