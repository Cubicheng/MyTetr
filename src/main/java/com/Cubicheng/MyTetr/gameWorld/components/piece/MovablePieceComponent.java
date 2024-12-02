package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.util.Pair;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import static com.Cubicheng.MyTetr.gameWorld.ConfigVars.ARR;
import static com.Cubicheng.MyTetr.gameWorld.ConfigVars.DAS;
import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class MovablePieceComponent extends OnePieceComponent {

    private Pair<Integer, Integer>[] kick_transation;

    private Timer l_timer, r_timer, down_timer;

    private long down_delta = 1500;

    public MovablePieceComponent(double x, double y) {
        super(x, y);
    }

    @Override
    public void onAdded() {
        x = 4;
        y = 20;

        kick_transation = new Pair[5];
        for (int i = 0; i < 5; i++) {
            kick_transation[i] = new Pair<>(0, 0);
        }

        TimerTask down_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        move_down();
                    }
                });
            }
        };

        down_timer = new Timer();
        down_timer.scheduleAtFixedRate(down_task, down_delta, down_delta);
    }

    @Override
    public void onRemoved() {
        down_timer.cancel();
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

        get_entity(Type.GameMap).getComponent(GameMapComponent.class).update_next_pieces();
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
                .with(new MovablePieceComponent(data.get("startX"), data.get("startY")))
                .with(components)
                .type(Type.MovablePiece)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    public void hard_drop() {
        get_entity(Type.GameMap).getComponent(GameMapComponent.class).add_piece();
        get_entity(Type.HoldPiece).getComponent(HoldPieceComponent.class).set_can_hold(true);
        get_next_piece();
    }

    public void hold() {
        if (!get_entity(Type.HoldPiece).getComponent(HoldPieceComponent.class).get_can_hold())
            return;

        int hold_type = get_entity(Type.HoldPiece).getComponent(HoldPieceComponent.class).get_techomino_type();

        get_entity(Type.HoldPiece).getComponent(HoldPieceComponent.class).set_techomino(techominoType);

        get_entity(Type.HoldPiece).getComponent(HoldPieceComponent.class).set_can_hold(false);

        techominoType = hold_type;
        techomino = int2techomino.get(techominoType);
        now_texture = new ImageView(ImageBuffer.texture[techominoType].image());
        rotate_index = 0;
        x = 4;
        y = 20;
        update_entity_position();
        update_texture();
    }

    public void on_move_left_begin() {
        l_timer = new Timer();
        TimerTask l_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        move_left();
                    }
                });
            }
        };
        l_timer.scheduleAtFixedRate(l_task, DAS, ARR);
    }

    public void on_move_right_begin() {
        r_timer = new Timer();
        TimerTask r_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        move_right();
                    }
                });
            }
        };
        r_timer.scheduleAtFixedRate(r_task, DAS, ARR);
    }

    public void on_move_left_end() {
        l_timer.cancel();
        l_timer.purge();
    }

    public void on_move_right_end() {
        r_timer.cancel();
        r_timer.purge();
    }


    public void on_move_down_begin(){
        down_timer.cancel();
        down_timer.purge();
        TimerTask down_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        move_down();
                    }
                });
            }
        };
        down_timer = new Timer();
        down_timer.scheduleAtFixedRate(down_task, 0, ConfigVars.SFD_ARR);
    }

    public void on_move_down_end(){
        down_timer.cancel();
        down_timer.purge();
        down_timer = new Timer();
        TimerTask down_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        move_down();
                    }
                });
            }
        };
        down_timer.scheduleAtFixedRate(down_task, down_delta, down_delta);
    }

    public void move_left() {
        if (check_collide(x - 1, y)) {
            x--;
            update_entity_position();
        }
    }

    public void move_right() {
        if (check_collide(x + 1, y)) {
            x++;
            update_entity_position();
        }

    }

    public void move_down() {
        if (check_collide(x, y - 1)) {
            y--;
            update_entity_position();
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
        rotate(3);

    }

    public void right_rotate() {
        rotate(1);

    }


    public void double_ratate() {
        rotate(2);
    }
}
