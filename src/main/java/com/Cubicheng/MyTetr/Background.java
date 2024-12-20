package com.Cubicheng.MyTetr;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.texture.Texture;

public class Background {
    private Entity background;
    private Texture texture;

    public Background(String url, GameScene gameScene, GameWorld gameWorld, double opacity) {

        var background_image = FXGL.image(url);

        double new_width = gameScene.getAppHeight() / background_image.getHeight() * background_image.getWidth();
        double new_height = gameScene.getAppHeight();

        var background_texture = new Texture(background_image);

        background_texture.setOpacity(opacity);
        background_texture.setFitWidth(new_width);
        background_texture.setFitHeight(new_height);

        background = new EntityBuilder()
                .at((gameScene.getAppWidth() - new_width) / 2, 0)
                .view(background_texture)
                .zIndex(Integer.MIN_VALUE)
                .build();

        gameWorld.addEntity(background);
    }

}
