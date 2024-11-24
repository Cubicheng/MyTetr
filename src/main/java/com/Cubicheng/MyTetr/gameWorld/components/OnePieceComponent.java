package com.Cubicheng.MyTetr.gameWorld.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;

import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.ui.FXGLListView;
import com.whitewoodcity.fxgl.app.ImageData;
import javafx.scene.image.ImageView;

import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class OnePieceComponent extends Component {

    private static final ImageData texture = new ImageData("block_2.png", BLOCK_SIZE, BLOCK_SIZE);

    private int x, y;

    private long DAS = 167;

    private long ARR = 33;

    boolean is_moved = false;

    @Override
    public void onAdded() {
        ImageView view = new ImageView(OnePieceComponent.texture.image());
        x = 0;
        y = 20;
        entity.getViewComponent().addChild(view);
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.OnePiece);
        for (var component : components)
            builder.with(component);
        return builder;
    }

    public static Entity of(SpawnData data, Component... components) {
        return of(builder(), data, components);
    }

    public static Entity of(EntityBuilder builder, SpawnData data, Component... components) {
        return builder
                .at(startX, startY)
                .with(new OnePieceComponent())
                .with(components)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    public void reset_is_moved() {
        is_moved = false;
    }

    public void move_left() {
        if (!is_moved) {
            if (x > 0) {
                x--;
                getEntity().translateX(-BLOCK_SIZE);
            }
            is_moved = true;
        }
    }

    public void move_right() {
        if (!is_moved) {
            if (x < MAP_WIDTH - 1) {
                x++;
                getEntity().translateX(BLOCK_SIZE);
            }
            is_moved = true;
        }
    }

    public void move_down() {
        if(!is_moved) {
            if (y > 1) {
                y--;
                getEntity().translateY(BLOCK_SIZE);
            }
            is_moved = true;
        }
    }
}
