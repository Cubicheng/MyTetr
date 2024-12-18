package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.ApplicationType;
import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.Pair;
import com.Cubicheng.MyTetr.netWork.client.Client;
import com.Cubicheng.MyTetr.netWork.protocol.UpdateMovablePiecePacket;
import com.Cubicheng.MyTetr.netWork.server.Server;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import static com.Cubicheng.MyTetr.gameWorld.ConfigVars.ARR;
import static com.Cubicheng.MyTetr.gameWorld.ConfigVars.DAS;
import static com.Cubicheng.MyTetr.gameWorld.Variables.*;

public class MovablePieceComponent extends OnePieceComponent {

    private Pair<Integer, Integer>[] kick_transation;

    private Timer l_timer, r_timer, down_timer;

    private double blink_time = 0;
    private int blink_break_cnt = 0;

    private long down_delta = 1500;

    private boolean is_collided = false;

    private boolean is_dead = false;

    public void setIs_dead(boolean is_dead) {
        this.is_dead = is_dead;
        down_timer.cancel();
    }

    public MovablePieceComponent(double x, double y, int id) {
        super(x, y, id);
    }

    private void push_UpdateMovablePiecePacket() {
        if (Application.getApplicationType() == ApplicationType.Server) {
            Server.getInstance().getHandler()
                    .push_UpdateMovablePiecePacket(new UpdateMovablePiecePacket(x, y, rotate_index));
        } else if (Application.getApplicationType() == ApplicationType.Client) {
            Client.getInstance().getHandler()
                    .push_UpdateMovablePiecePacket(new UpdateMovablePiecePacket(x, y, rotate_index));
        }
    }

    private void push_OnHardDropPacket() {
        if (Application.getApplicationType() == ApplicationType.Server) {
            Server.getInstance().getHandler()
                    .push_OnHardDropPacket();
        } else if (Application.getApplicationType() == ApplicationType.Client) {
            Client.getInstance().getHandler()
                    .push_OnHardDropPacket();
        }
    }

    private void push_OnHoldPacket() {
        if (Application.getApplicationType() == ApplicationType.Server) {
            Server.getInstance().getHandler()
                    .push_OnHoldPacket();
        } else if (Application.getApplicationType() == ApplicationType.Client) {
            Client.getInstance().getHandler()
                    .push_OnHoldPacket();
        }
    }

    public void update(UpdateMovablePiecePacket packet) {
        x = packet.getX();
        y = packet.getY();
        rotate_index = packet.getRotate_index();
        update_texture();
        update_entity_position();
    }

    @Override
    public void onAdded() {
        x = 4;
        y = 21;

        kick_transation = new Pair[5];
        for (int i = 0; i < 5; i++) {
            kick_transation[i] = new Pair<>(0, 0);
        }

        if (player_id == 0) {
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
    }

    @Override
    public void onRemoved() {
        down_timer.cancel();
    }

    private boolean check_bottom_collide() {
        return y == get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).getY();
    }

    @Override
    public void onUpdate(double tpf) {
        if (is_dead) {
            return;
        }
        if (techomino == null) {
            get_next_piece();
        }
        if (check_bottom_collide()) {
            visibility = 0.8 + 0.2 * Math.cos(2 * Math.PI * blink_time);
            blink_time += tpf;
            get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).setVisibility(0.0);
            update_texture();
        } else {
            if (blink_time != 0) {
                visibility = 1.0;
                blink_time = 0;
                get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).setVisibility(ConfigVars.ghost_piece_visibility);
                update_texture();
            }
        }
        if (blink_time > SOFT_DROP_TIME && player_id == 0) {
            hard_drop();
        }
    }

    private void get_next_piece() {
        is_collided = false;
        Entity gameMap = get_entity(Type.GameMap, 0);
        if (gameMap == null) {
            System.out.println("gameMap is null");
            return;
        }
        techominoType = gameMap.getComponent(GameMapComponent.class).get_next_piece();
        techomino = int2techomino.get(techominoType);
        now_texture = new ImageView(ImageBuffer.texture[techominoType].image());
        rotate_index = 0;

        x = 4;
        y = 21;

        get_entity(Type.GameMap, 0).getComponent(GameMapComponent.class).update_next_pieces();
        update_entity_position();
        update_texture();

        get_entity(Type.WarnPiece, 0).getComponent(WarnPieceComponent.class).get_next_piece();
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
                .with(new MovablePieceComponent(data.get("startX"), data.get("startY"), data.get("id")))
                .with(components)
                .type(Type.MovablePiece)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    public void hard_drop() {
        if (is_dead) {
            return;
        }
        if (player_id == 0) {
            push_OnHardDropPacket();
        }
        get_entity(Type.GameMap, 0).getComponent(GameMapComponent.class).add_piece();
        get_entity(Type.HoldPiece, 0).getComponent(HoldPieceComponent.class).set_can_hold(true);
        get_next_piece();
    }

    public void hold() {
        if (!get_entity(Type.HoldPiece, 0).getComponent(HoldPieceComponent.class).get_can_hold())
            return;

        if (player_id == 0) {
            push_OnHoldPacket();
        }

        int hold_type = get_entity(Type.HoldPiece, 0).getComponent(HoldPieceComponent.class).get_techomino_type();

        get_entity(Type.HoldPiece, 0).getComponent(HoldPieceComponent.class).set_techomino(techominoType);

        get_entity(Type.HoldPiece, 0).getComponent(HoldPieceComponent.class).set_can_hold(false);

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

    public void on_move_down_begin() {
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

    public void on_move_down_end() {
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

    private void blink_break() {
        if (blink_time != 0) {
            blink_break_cnt++;
            blink_time = 0;
            if (blink_break_cnt == 15 && player_id == 0) {
                hard_drop();
                get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).setVisibility(ConfigVars.ghost_piece_visibility);
                blink_break_cnt = 0;
            }
        }
    }

    public void move_left() {
        if (can_move_to(x - 1, y)) {
            blink_break();
            x--;
            update_entity_position();
            push_UpdateMovablePiecePacket();
        }
    }

    public void move_right() {
        if (can_move_to(x + 1, y)) {
            blink_break();
            x++;
            update_entity_position();
            push_UpdateMovablePiecePacket();
        }
    }

    public void move_down() {
        if (can_move_to(x, y - 1)) {
            blink_break();
            y--;
            update_entity_position();
            push_UpdateMovablePiecePacket();
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
            if (can_move_to(xx, yy)) {
                x = xx;
                y = yy;
                blink_break();
                update_entity_position();
                update_texture();
                push_UpdateMovablePiecePacket();
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
