package com.Cubicheng.MyTetr.gameWorld.components;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.NextQueue;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.piece.GhostPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.NextPieceComponent;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.Cubicheng.MyTetr.gameWorld.Variables.*;

public class GameMapComponent extends Component {

    private List<List<Integer>> playfiled = new ArrayList<>(MAP_HEIGHT);

    private NextQueue next_queue;
    private int y;

    private int player_id;

    public GameMapComponent(int id) {
        this.player_id = id;
    }

    @Override
    public void onAdded() {
        next_queue = new NextQueue(seed);
        for (int i = 0; i < MAP_HEIGHT; i++) {
            playfiled.add(generate_new_empty_row());
        }
    }

    public int get_next_piece() {
        return next_queue.get_next_piece();
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

    private void update_texture() {
        Entity gameMap = get_entity(Type.GameMap, 0);
        gameMap.getViewComponent().clearChildren();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (playfiled.get(i).get(j) != -1) {
                    ImageView imageView = new ImageView(ImageBuffer.texture[playfiled.get(i).get(j)].image());
                    imageView.setLayoutX(j * BLOCK_SIZE);
                    imageView.setLayoutY(-i * BLOCK_SIZE);
                    gameMap.getViewComponent().addChild(imageView);
                }
            }
        }
    }

    private void set_map(int x, int y, int type) {
        playfiled.get(y).set(x, type);
    }

    private void clear_line() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            boolean is_full = true;
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (playfiled.get(i).get(j) == -1) {
                    is_full = false;
                    break;
                }
            }
            if (!is_full) continue;
            playfiled.remove(i);
            playfiled.add(generate_new_empty_row());
            i--;
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
