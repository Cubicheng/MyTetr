package com.Cubicheng.MyTetr.gameWorld.components;

import com.Cubicheng.MyTetr.Application;
import com.Cubicheng.MyTetr.ApplicationType;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.Pair;
import com.Cubicheng.MyTetr.gameWorld.*;
import com.Cubicheng.MyTetr.gameWorld.components.piece.GhostPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.NextPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.WarnPieceComponent;
import com.Cubicheng.MyTetr.netWork.client.Client;
import com.Cubicheng.MyTetr.netWork.server.Server;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.Cubicheng.MyTetr.gameWorld.Variables.*;

public class GameMapComponent extends Component {

    private List<List<Integer>> playfiled = new ArrayList<>(MAP_HEIGHT);

    private NextQueue next_queue;
    private AttackQueue attack_queue;
    private int y;
    private Random random;
    private int combo_cnt = 0;

    private boolean is_warning = false;

    private int player_id;

    public GameMapComponent(int id) {
        this.player_id = id;
    }

    public void add_attack_to_queue(int attack, int x) {
        attack_queue.add(attack, x);
        update_attack_bar();
    }

    public void add_garbage() {
        Pair<Integer, Integer> pair = attack_queue.get_front();
        if (pair == null) {
            return;
        }
        update_attack_bar();
        int attack = pair.first();
        int x = pair.second();

        for (int i = MAP_HEIGHT - 1; i >= attack; i--) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                playfiled.get(i).set(j, playfiled.get(i - attack).get(j));
            }
        }

        for (int i = 0; i < attack; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (j == x) {
                    playfiled.get(i).set(j, -1);
                } else {
                    playfiled.get(i).set(j, 8);
                }
            }
        }
        update_texture();
    }

    @Override
    public void onAdded() {
        next_queue = new NextQueue(seed);
        attack_queue = new AttackQueue();
        random = new Random();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            playfiled.add(generate_new_empty_row());
        }
    }

    public int get_next_piece() {
        return next_queue.get_next_piece_and_pop();
    }

    public void update_next_pieces() {
        Entity gameMap = get_entity(Type.GameMap, 0);
        for (int i = 0; i < 5; i++) {
            Entity nextPiece = get_entity(Type.NextPiece, i);
            int type = gameMap.getComponent(GameMapComponent.class).get_next_piece(i);
            nextPiece.getComponent(NextPieceComponent.class).set_techomino(type);
        }
    }

    public int get_next_piece(int id) {
        return next_queue.get_next_piece(id);
    }

    public List<List<Integer>> get_playfiled() {
        return playfiled;
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.GameMap);
        for (var component : components)
            builder.with(component);
        return builder;
    }

    public static Entity of(SpawnData data, Component... components) {
        return of(builder(), data, components);
    }

    public static Entity of(EntityBuilder builder, SpawnData data, Component... components) {
        return builder
                .at(data.getX(), data.getY())
                .with(new GameMapComponent(data.get("id")))
                .type(Type.GameMap)
                .with(components)
                .build();
    }

    private Entity get_entity(Type type, int id) {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_entity(type, id + player_id * typeSize.get(type));
    }

    List<Integer> generate_new_empty_row() {
        List<Integer> row = new ArrayList<>(MAP_WIDTH);
        for (int i = 0; i < MAP_WIDTH; i++) {
            row.add(-1);
        }
        return row;
    }

    private void handle_warning(int max_y) {
        if (get_entity(Type.WarnPiece, 0).getComponent(WarnPieceComponent.class).is_dead()) {
            get_entity(Type.MovablePiece, 0).getComponent(MovablePieceComponent.class).setIs_dead(true);
            FXGL.<GameApp>getAppCast().getFrontlineService().get_player(player_id).on_die();
            return;
        }
        if (max_y > 16) {
            if (!is_warning) {
                is_warning = true;
                get_entity(Type.WarnPiece, 0).getComponent(WarnPieceComponent.class).setVisibility(1.0);
                var mapImage = get_entity(Type.MapImageEntity, 0);
                mapImage.getViewComponent().addChild(ImageBuffer.map_warn_texture);
            }
        } else {
            if (is_warning) {
                is_warning = false;
                get_entity(Type.WarnPiece, 0).getComponent(WarnPieceComponent.class).setVisibility(0.0);
                var mapImage = get_entity(Type.MapImageEntity, 0);
                mapImage.getViewComponent().removeChild(ImageBuffer.map_warn_texture);
            }
        }
    }

    private void update_attack_bar() {
        Entity attack_bar = get_entity(Type.AttackBar, 0);
        attack_bar.getViewComponent().clearChildren();
        var rect = new Rectangle(17, 80 * attack_queue.getSum());
        rect.setFill(Color.RED);
        rect.setOpacity(0.7);
        rect.setLayoutY(-rect.getHeight());
        attack_bar.getViewComponent().addChild(rect);
    }

    private void update_texture() {
        Entity gameMap = get_entity(Type.GameMap, 0);
        gameMap.getViewComponent().clearChildren();
        int max_y = 0;
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (playfiled.get(i).get(j) != -1) {
                    max_y = Math.max(max_y, i);
                    ImageView imageView = new ImageView(ImageBuffer.texture[playfiled.get(i).get(j)].image());
                    imageView.setLayoutX(j * BLOCK_SIZE);
                    imageView.setLayoutY(-i * BLOCK_SIZE);
                    gameMap.getViewComponent().addChild(imageView);
                }
            }
        }
        handle_warning(max_y);
    }

    private void set_map(int x, int y, int type) {
        playfiled.get(y).set(x, type);
    }

    private void push_AttackPacket(int attack, int x) {
        attack = attack_queue.clear_attack(attack);
        update_attack_bar();
        if (player_id != 0) return;
        if (Application.getApplicationType() == ApplicationType.Server) {
            Server.getInstance().getHandler().push_AttackPacket(attack, x);
            get_entity(Type.GameMap, 1).getComponent(GameMapComponent.class).add_attack_to_queue(attack, x);
        } else if (Application.getApplicationType() == ApplicationType.Client) {
            Client.getInstance().getHandler().push_AttackPacket(attack, x);
            get_entity(Type.GameMap, 1).getComponent(GameMapComponent.class).add_attack_to_queue(attack, x);
        }
    }

    private boolean is_block(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            return true;
        }
        return playfiled.get(y).get(x) != -1;
    }

    private boolean isTspin() {
        var movablePieceComponent = get_entity(Type.MovablePiece, 0).getComponent(MovablePieceComponent.class);
        int now_techomino_type = movablePieceComponent.get_techomino_type();
        if (now_techomino_type != 5) {
            return false;
        }
        int x = movablePieceComponent.getX();
        int y = movablePieceComponent.getY();

        int block_cnt = 0;
        if (is_block(x - 1, y - 1)) block_cnt++;
        if (is_block(x + 1, y - 1)) block_cnt++;
        if (is_block(x - 1, y + 1)) block_cnt++;
        if (is_block(x + 1, y + 1)) block_cnt++;

        return block_cnt >= 3;
    }

    private void clear_line() {
        int clear_line_cnt = 0;
        boolean is_tspin = isTspin();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            boolean is_full = true;
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (playfiled.get(i).get(j) == -1) {
                    is_full = false;
                    break;
                }
            }
            if (!is_full) continue;
            clear_line_cnt++;
            playfiled.remove(i);
            playfiled.add(generate_new_empty_row());
            i--;
        }

        int attack_line_cnt = 0;

        if (clear_line_cnt > 0) {
            if (is_tspin) {
                attack_line_cnt = ATTACK_TABLE[clear_line_cnt + 3][combo_cnt];
            } else {
                attack_line_cnt = ATTACK_TABLE[clear_line_cnt - 1][combo_cnt];
            }
        }

        if (attack_line_cnt > 0) {
            push_AttackPacket(attack_line_cnt, random.nextInt(MAP_WIDTH));
        }

        if (clear_line_cnt == 0) {
            add_garbage();
        }

        if (clear_line_cnt == 0) {
            combo_cnt = 0;
        } else {
            combo_cnt++;
        }
    }

    public void add_piece() {
        int techominoType = get_entity(Type.MovablePiece, 0).getComponent(MovablePieceComponent.class).get_techomino_type();
        for (int i = 0; i < 4; i++) {
            int x = get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).getX(i);
            int y = get_entity(Type.GhostPiece, 0).getComponent(GhostPieceComponent.class).getY(i);
            set_map(x, y, techominoType);
        }
        clear_line();
        update_texture();
    }
}
