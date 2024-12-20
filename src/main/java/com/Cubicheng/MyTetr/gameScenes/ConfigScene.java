package com.Cubicheng.MyTetr.gameScenes;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.Background;
import com.Cubicheng.MyTetr.ConfigData;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.gameWorld.Variables;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitewoodcity.fxgl.service.PushAndPopGameSubScene;
import com.whitewoodcity.fxgl.service.XInput;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ConfigScene implements PushAndPopGameSubScene {
    public static final String SCENE_NAME = "CONFIG";

    private static final double VISIBILITY_DELTA = 0.01;

    private ConfigData configData;

    private GameWorld gameWorld;

    private Background background;

    @Override
    public XInput initInput(Input input) {
        input.addAction(new UserAction("Escape") {
            @Override
            protected void onActionBegin() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("保存修改吗？");
                alert.initOwner(Application.getStage());
                alert.getDialogPane().setStyle("-fx-font-family: \"IPix\";");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.writeValue(new File(Variables.config_file_path), configData);
                        ConfigVars.update_config_from_json();
                    } catch (IOException e) {
                        System.err.println("读取或写入JSON文件出错：" + e.getMessage());
                    }
                }
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            configData = objectMapper.readValue(new File(Variables.config_file_path), ConfigData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var background = FXGL.image("menu.png");
        gameScene.setBackgroundColor(new ImagePattern(background, 0, 0, 1, 1, true));

        var gridpane = new GridPane();

        gridpane.setPadding(new Insets(10, 10, 10, 10));

        var header = new Text("设置");
        header.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(40));

        gridpane.add(header, 1, 0);

        var das_text = new Text("启动快速横移的前摇");
        das_text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        var das_slider = new Slider();
        das_slider.setMin(17);
        das_slider.setMax(333);
        das_slider.setValue(configData.getDas());
        das_slider.setPrefWidth(500);

        var das_value = new Text(String.valueOf((long) das_slider.getValue()) + "ms");
        das_value.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        das_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                long roundedValue = Math.round(new_val.doubleValue());
                das_slider.setValue(roundedValue);
                das_value.setText(String.valueOf(roundedValue) + "ms");
                configData.setDas(roundedValue);
            }
        });

        var hbox1 = new HBox();
        hbox1.getChildren().addAll(das_text, das_slider, das_value);
        hbox1.setSpacing(20);

        var arr_text = new Text("快速横移时间间隔  ");
        arr_text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        var arr_slider = new Slider();
        arr_slider.setMin(1);
        arr_slider.setMax(83);
        arr_slider.setValue(configData.getArr());
        arr_slider.setMinorTickCount(1);
        arr_slider.setPrefWidth(500);

        var arr_value = new Text(String.valueOf((long) arr_slider.getValue()) + "ms");
        arr_value.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        arr_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                long roundedValue = Math.round(new_val.doubleValue());
                arr_slider.setValue(roundedValue);
                configData.setArr(roundedValue);
                arr_value.setText(String.valueOf(roundedValue) + "ms");
            }
        });

        var hbox2 = new HBox();
        hbox2.getChildren().addAll(arr_text, arr_slider, arr_value);
        hbox2.setSpacing(20);

        var sfd_arr_text = new Text("快速下降时间间隔  ");
        sfd_arr_text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        var sfd_arr_slider = new Slider();
        sfd_arr_slider.setMin(1);
        sfd_arr_slider.setMax(100);
        sfd_arr_slider.setValue(configData.getSfd_ARR());
        sfd_arr_slider.setPrefWidth(500);

        var sfd_arr_value = new Text(String.valueOf((long) sfd_arr_slider.getValue()) + "ms");
        sfd_arr_value.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        sfd_arr_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                long roundedValue = Math.round(new_val.doubleValue());
                sfd_arr_slider.setValue(roundedValue);
                configData.setSfd_ARR(roundedValue);
                sfd_arr_value.setText(String.valueOf(roundedValue) + "ms");
            }
        });

        var hbox3 = new HBox();
        hbox3.getChildren().addAll(sfd_arr_text, sfd_arr_slider, sfd_arr_value);
        hbox3.setSpacing(20);

        var visibility_text = new Text("幽灵块能见度       ");
        visibility_text.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        var visibility_slider = new Slider();
        visibility_slider.setMin(0);
        visibility_slider.setMax(1);
        visibility_slider.setValue(configData.getGhost_piece_visibility());
        visibility_slider.setPrefWidth(500);

        var visibility_value = new Text(String.valueOf((long) (visibility_slider.getValue() * 100)) + "%");
        visibility_value.setFont(FXGL.getAssetLoader().loadFont("IPix.ttf").newFont(30));

        visibility_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                double roundedValue = Math.round(new_val.doubleValue() / VISIBILITY_DELTA) * VISIBILITY_DELTA;
                visibility_slider.setValue(roundedValue);
                configData.setGhost_piece_visibility(roundedValue);
                visibility_value.setText(String.valueOf((long) (roundedValue * 100)) + "%");
            }
        });

        var hbox4 = new HBox();
        hbox4.getChildren().addAll(visibility_text, visibility_slider, visibility_value);
        hbox4.setSpacing(20);

        gridpane.add(hbox1, 1, 1);
        gridpane.add(hbox2, 1, 2);
        gridpane.add(hbox3, 1, 3);
        gridpane.add(hbox4, 1, 4);

        gridpane.setTranslateX((FXGL.getAppCenter().getX() - gridpane.getBoundsInLocal().getWidth()) / 2);
        gridpane.setTranslateY(FXGL.getAppCenter().getY() + 50);

        gridpane.setHgap(30);
        gridpane.setVgap(30);

        gameScene.addUINode(gridpane);
    }
}
