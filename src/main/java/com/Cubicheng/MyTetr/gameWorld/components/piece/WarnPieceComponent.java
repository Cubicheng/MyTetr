package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Variables.int2techomino;

public class WarnPieceComponent extends OnePieceComponent {

    public WarnPieceComponent(double x, double y, int id) {
        super(x, y, id);
    }

    @Override
    public void onAdded() {
        x = 4;
        y = 21;
        visibility = 0.0;
        now_texture = new ImageView(ImageBuffer.texture[10].image());
    }

    @Override
    public void onUpdate(double tpf) {
        if (techomino == null) {
            get_next_piece();
        }
    }

    public void get_next_piece() {
        techominoType = get_entity(Type.GameMap, 0).getComponent(GameMapComponent.class).get_next_piece(0);
        techomino = int2techomino.get(techominoType);
        update_entity_position();
        update_texture();
    }

    public boolean is_dead() {
        return !can_move_to(x, y);
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.WarnPiece);
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
                .with(new WarnPieceComponent(data.get("startX"), data.get("startY"), data.get("id")))
                .type(Type.WarnPiece)
                .with(components)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }
}
