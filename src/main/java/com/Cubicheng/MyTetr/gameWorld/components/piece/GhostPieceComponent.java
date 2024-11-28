package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;
import static com.Cubicheng.MyTetr.gameWorld.Constants.BLOCK_SIZE;

public class GhostPieceComponent extends OnePieceComponent {

    @Override
    public void onAdded() {
        x = 4;
        y = 20;
        now_texture = new ImageView(texture[9].image());
        opacity = ConfigVars.ghost_piece_opacity;
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.GhostPiece);
        for (var component : components)
            builder.with(component);
        return builder;
    }

    public static Entity of(SpawnData data, Component... components) {
        return of(builder(), data, components);
    }

    public static Entity of(EntityBuilder builder, SpawnData data, Component... components) {
        return builder
                .with(new GhostPieceComponent())
                .with(components)
                .type(Type.GhostPiece)
                .zIndex(Integer.MAX_VALUE - 10)
                .build();
    }

    @Override
    public void onUpdate(double tpf) {
        Entity movablePiece = get_entity(Type.MovablePiece);
        assert movablePiece != null;
        this.techomino = movablePiece.getComponent(MovablePieceComponent.class).get_techomino();
        rotate_index = movablePiece.getComponent(MovablePieceComponent.class).get_rotate_index();
        x = movablePiece.getComponent(MovablePieceComponent.class).getX();
        y = movablePiece.getComponent(MovablePieceComponent.class).getY();
        while (check_collide(x, y - 1)) {
            y--;
        }
        update_entity_position();
        update_texture();
    }
}