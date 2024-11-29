package com.Cubicheng.MyTetr.gameWorld.components;

import atlantafx.base.util.BBCodeHandler;
import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.NextQueue;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.piece.GhostPieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.MovablePieceComponent;
import com.Cubicheng.MyTetr.gameWorld.components.piece.OnePieceComponent;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.Vector;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class GameMapComponent extends Component {

    private Vector<Vector<Integer>> playfiled = new Vector<>(MAP_HEIGHT);

    private NextQueue next_queue;
    private int y;

    @Override
    public void onAdded() {
        next_queue = new NextQueue();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            Vector<Integer> row = new Vector<>(MAP_WIDTH);
            for (int j = 0; j < MAP_WIDTH; j++) {
                row.add(-1);
            }
            playfiled.add(row);
        }
    }

    public int get_next_piece() {
        return next_queue.get_next_piece();
    }

    public Vector<Vector<Integer>> get_playfiled() {
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
                .at(startX, startY + 19 * BLOCK_SIZE)
                .with(new GameMapComponent())
                .type(Type.GameMap)
                .with(components)
                .build();
    }

    private Entity get_entity(Type type, int id) {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_entity(type, id);
    }

    private Entity get_entity(Type type) {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_entity(type);
    }

    private void update_texture() {
        Entity gameMap = get_entity(Type.GameMap);
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
            for (int j = i; j < MAP_HEIGHT - 1; j++) {
                for (int k = 0; k < MAP_WIDTH; k++) {
                    playfiled.get(j).set(k, playfiled.get(j + 1).get(k));
                }
            }
            i--;
        }
    }

    public void add_piece() {
        int techominoType = get_entity(Type.MovablePiece).getComponent(MovablePieceComponent.class).get_techomino_type();
        for (int i = 0; i < 4; i++) {
            int x = get_entity(Type.GhostPiece).getComponent(GhostPieceComponent.class).getX(i);
            int y = get_entity(Type.GhostPiece).getComponent(GhostPieceComponent.class).getY(i);
            set_map(x, y, techominoType);
        }
        clear_line();
        update_texture();
    }
}
