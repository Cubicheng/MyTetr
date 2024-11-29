package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.techominoData.Techomino;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;

import com.Cubicheng.MyTetr.gameWorld.Type;
import com.whitewoodcity.fxgl.app.ImageData;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class OnePieceComponent extends Component {

    protected ImageView now_texture;

    protected int x, y;

    protected int rotate_index = 0;

    protected int techominoType;
    protected Techomino techomino;

    double opacity = 1.0;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int get_rotate_index() {
        return rotate_index;
    }

    public Techomino get_techomino() {
        return techomino;
    }

    public int get_techomino_type(){
        return techominoType;
    }

    @Override
    public void onAdded() {
        x = 4;
        y = 20;
    }

    protected void update_entity_position() {
        getEntity().setX(startX + x * BLOCK_SIZE);
        getEntity().setY(startY + (19 - y) * BLOCK_SIZE);
    }

    protected GameWorld get_GameWorld() {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_game_world();
    }

    protected Entity get_entity(Type type, int id) {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_entity(type, id);
    }

    protected Entity get_entity(Type type) {
        return FXGL.<GameApp>getAppCast().getFrontlineService().get_entity(type);
    }

    protected void update_texture() {
        getEntity().getViewComponent().clearChildren();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(now_texture.getImage());
            imageView.setOpacity(opacity);
            imageView.setLayoutX(techomino.techomino[rotate_index][i].first() * BLOCK_SIZE);
            imageView.setLayoutY(-techomino.techomino[rotate_index][i].second() * BLOCK_SIZE);
            getEntity().getViewComponent().addChild(imageView);
        }
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
                .type(Type.OnePiece)
                .with(components)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    protected boolean check_collide(int x, int y) {
        Entity gameMap = get_entity(Type.GameMap);
        if (gameMap == null) {
            System.out.println("gameMap is null");
            return false;
        }
        for (int i = 0; i < 4; i++) {
            int xx = techomino.techomino[rotate_index][i].first() + x;
            int yy = techomino.techomino[rotate_index][i].second() + y;
            if (xx < 0 || xx >= MAP_WIDTH || yy < 0)
                return false;
            if (gameMap.getComponent(GameMapComponent.class).get_playfiled().get(yy).get(xx) != -1) {
                return false;
            }
        }
        return true;
    }
}