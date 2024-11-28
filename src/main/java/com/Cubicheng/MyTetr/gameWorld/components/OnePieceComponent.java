package com.Cubicheng.MyTetr.gameWorld.components;

import atlantafx.base.util.BBCodeHandler;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.SinglePlayer;
import com.Cubicheng.MyTetr.gameWorld.techominoData.Techomino;
import com.Cubicheng.MyTetr.gameWorld.techominoData.TechominoType;
import com.Cubicheng.MyTetr.util.Pair;
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

    private Pair<Integer, Integer>[] kick_transation;

    @Override
    public void onAdded() {
        x = 4;
        y = 20;
        kick_transation = new Pair[5];
        for (int i = 0; i < 5; i++) {
            kick_transation[i] = new Pair<>(0, 0);
        }
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
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(now_texture.getImage());
            imageView.setLayoutX(techomino.techomino[rotate_index][i].first() * BLOCK_SIZE);
            imageView.setLayoutY(-techomino.techomino[rotate_index][i].second() * BLOCK_SIZE);
            getEntity().getViewComponent().addChild(imageView);
        }
    }

    private void get_next_piece() {
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
                .at(startX + 4 * BLOCK_SIZE, startY - BLOCK_SIZE)
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

    private boolean check_collide(int x, int y) {
        Entity gameMap = get_map();
        if (gameMap == null) {
            System.out.println("gameMap is null");
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int xx = techomino.techomino[rotate_index][i].first() + x;
            int yy = techomino.techomino[rotate_index][i].second() + y;
            if (xx < 0 || xx >= MAP_WIDTH || yy < 0)
                return false;
            if (gameMap.getComponent(GameMapComponent.class).get_playfiled().get(yy).get(xx) != 0) {
                return false;
            }
        }
        return true;
    }

    public void move_left() {
        if (!is_moved) {
            if (check_collide(x - 1, y)) {
                x--;
                getEntity().translateX(-BLOCK_SIZE);
            }
            is_moved = true;
        }
    }

    public void move_right() {
        if (!is_moved) {
            if (check_collide(x + 1, y)) {
                x++;
                getEntity().translateX(BLOCK_SIZE);
            }
            is_moved = true;
        }
    }

    public void move_down() {
        if (!is_moved) {
            if (check_collide(x, y - 1)) {
                y--;
                getEntity().translateY(BLOCK_SIZE);
            }
            is_moved = true;
        }
    }

    public void rotate(int option) {
        int new_index = (rotate_index + option) % 4;
        for (int i = 0; i < 5; i++) {
            kick_transation[i].setFirst(techomino.offset[rotate_index][i].first() - techomino.offset[new_index][i].first());
            kick_transation[i].setSecond(techomino.offset[rotate_index][i].second() - techomino.offset[new_index][i].second());
        }
        for (int i = 0; i < 5; i++) {
            int xx = x + kick_transation[i].first();
            int yy = y + kick_transation[i].second();
            if (check_collide(xx, yy)) {
                x = xx;
                y = yy;
                rotate_index = new_index;
                update_texture();
                return;
            }
        }
    }

    public void left_rotate() {
        if (!is_moved) {
            rotate(3);
            is_moved = true;
        }
    }

    public void right_rotate() {
        if (!is_moved) {
            rotate(1);
            is_moved = true;
        }
    }
}