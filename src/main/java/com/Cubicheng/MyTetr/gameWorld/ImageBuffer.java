package com.Cubicheng.MyTetr.gameWorld;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.whitewoodcity.fxgl.app.ImageData;
import javafx.scene.image.Image;

import static com.Cubicheng.MyTetr.gameWorld.Variables.BLOCK_SIZE;

public class ImageBuffer {
    public static final ImageData[] texture = {
            new ImageData("block_0.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_1.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_2.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_3.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_4.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_5.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_6.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_7.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_8.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_9.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_10.png", BLOCK_SIZE, BLOCK_SIZE),
    };

    public static Texture map_texture = null;
    public static Texture map_warn_texture = null;
}
