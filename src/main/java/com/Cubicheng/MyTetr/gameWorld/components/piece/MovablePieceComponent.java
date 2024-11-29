package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.util.Pair;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class MovablePieceComponent extends OnePieceComponent {

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

    @Override
    public void onUpdate(double tpf) {
        if (techomino == null) {
            get_next_piece();
        }
    }

    private void get_next_piece() {
        Entity gameMap = get_entity(Type.GameMap);
        if (gameMap == null) {
            System.out.println("gameMap is null");
            return;
        }
        techominoType = gameMap.getComponent(GameMapComponent.class).get_next_piece();
        techomino = int2techomino.get(techominoType);
        now_texture = new ImageView(ImageBuffer.texture[techominoType].image());
        rotate_index = 0;

        x = 4;
        y = 20;

        update_next_pieces();
        update_entity_position();
        update_texture();
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.MovablePiece);
        for (var component : components)
            builder.with(component);
        return builder;
    }

    public static Entity of(SpawnData data, Component... components) {
        return of(builder(), data, components);
    }

    public static Entity of(EntityBuilder builder, SpawnData data, Component... components) {
        return builder
                .with(new MovablePieceComponent())
                .with(components)
                .type(Type.MovablePiece)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    private void update_next_pieces() {
        Entity gameMap = get_entity(Type.GameMap);
        for (int i = 0; i < 5; i++) {
            Entity nextPiece = get_entity(Type.NextPiece,i);
            int type = gameMap.getComponent(GameMapComponent.class).get_next_piece(i);
            nextPiece.getComponent(NextPieceComponent.class).set_techomino(type);
        }
    }

    public void hard_drop() {
        if (!is_moved) {
            get_entity(Type.GameMap).getComponent(GameMapComponent.class).add_piece();
            get_next_piece();
            is_moved = true;
        }
    }

    public void reset_is_moved() {
        is_moved = false;
    }

    public void move_left() {
        if (!is_moved) {
            if (check_collide(x - 1, y)) {
                x--;
                update_entity_position();
            }
            is_moved = true;
        }
    }

    public void move_right() {
        if (!is_moved) {
            if (check_collide(x + 1, y)) {
                x++;
                update_entity_position();
            }
            is_moved = true;
        }
    }

    public void move_down() {
        if (!is_moved) {
            if (check_collide(x, y - 1)) {
                y--;
                update_entity_position();
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
        int last_index = rotate_index;
        rotate_index = new_index;
        for (int i = 0; i < 5; i++) {
            int xx = x + kick_transation[i].first();
            int yy = y + kick_transation[i].second();
            if (check_collide(xx, yy)) {
                x = xx;
                y = yy;
                update_entity_position();
                update_texture();
                return;
            }
        }
        rotate_index = last_index;
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

    public void double_ratate() {
        System.out.println("double_ratate");
        if (!is_moved) {
            rotate(2);
            is_moved = true;
        }
    }
}
