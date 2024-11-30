package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;
import static com.Cubicheng.MyTetr.gameWorld.Constants.BLOCK_SIZE;

public class HoldPieceComponent extends OnePieceComponent {

    @Override
    public void onAdded() {
        can_hold = true;
    }

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.HoldPiece);
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
                .with(new HoldPieceComponent())
                .type(Type.HoldPiece)
                .with(components)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }

    boolean can_hold;

    public boolean get_can_hold() {
        return can_hold;
    }

    public void set_can_hold(boolean can_hold) {
        this.can_hold = can_hold;
        if(!can_hold){
            now_texture = new ImageView(ImageBuffer.texture[8].image());
        }else{
            now_texture = new ImageView(ImageBuffer.texture[techominoType].image());
        }
        update_texture();
    }
}