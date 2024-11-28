package com.Cubicheng.MyTetr.gameWorld.components;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.SinglePlayer;
import com.Cubicheng.MyTetr.gameWorld.techominoData.Techomino;
import com.Cubicheng.MyTetr.gameWorld.techominoData.TechominoType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;

import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.ui.FXGLListView;
import com.whitewoodcity.fxgl.app.ImageData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class OnePieceComponent extends Component {

    private static final ImageData[] texture = {
            new ImageData("block_0.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_1.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_2.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_3.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_4.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_5.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_6.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_7.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_8.png", BLOCK_SIZE, BLOCK_SIZE),
            new ImageData("block_9.png", BLOCK_SIZE, BLOCK_SIZE)
    };

    private ImageView now_texture;

    private int x, y;

    private int rotate_index;

    TechominoType techominoType;
    Techomino techomino;

    private long DAS = 167;

    private long ARR = 33;

    boolean is_moved = false;

    @Override
    public void onAdded() {
        x = 0;
        y = 20;
    }

    private Entity get_map() {
        var frontlineService = FXGL.<GameApp>getAppCast().getFrontlineService();
        if (frontlineService == null) {
            System.out.println("frontlineService is null");
            return null;
        }
        return frontlineService.get_game_world().getEntitiesByType(Type.GameMap).getFirst();
    }

    private void update_texture() {
        getEntity().getViewComponent().clearChildren();
        ImageView center_view = new ImageView(now_texture.getImage());
        getEntity().getViewComponent().addChild(center_view);
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(now_texture.getImage());
            imageView.setLayoutX(techomino.techomino[rotate_index][i].first() * BLOCK_SIZE);
            imageView.setLayoutY(-techomino.techomino[rotate_index][i].second() * BLOCK_SIZE);
            getEntity().getViewComponent().addChild(imageView);
        }
    }

    public void get_next_piece() {
        Entity gameMap = get_map();
        if (gameMap == null) {
            System.out.println("gameMap is null");
            return;
        }
        int next_id = gameMap.getComponent(GameMapComponent.class).get_next_piece();
        techominoType = int2techominoType.get(next_id);
        techomino = int2techomino.get(next_id);
        now_texture = new ImageView(texture[next_id].image());
        rotate_index = 0;

        update_texture();
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
                .type(Type.OnePiece)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    public void on_drop() {
        if (!is_moved) {
            get_next_piece();
            is_moved = true;
        }
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
        if (!is_moved) {
            if (y > 1) {
                y--;
                getEntity().translateY(BLOCK_SIZE);
            }
            is_moved = true;
        }
    }
}