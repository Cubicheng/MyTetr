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

public class NextPieceComponent extends OnePieceComponent {

    public static EntityBuilder builder(Component... components) {
        var builder = FXGL.entityBuilder().type(Type.NextPiece);
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
                .with(new NextPieceComponent())
                .type(Type.NextPiece)
                .with(components)
                .zIndex(Integer.MAX_VALUE)
                .build();
    }



    public void set_techomino(int techominoType) {
        this.techominoType = techominoType;
        this.techomino = int2techomino.get(techominoType);
        this.now_texture = new ImageView(ImageBuffer.texture[techominoType].image());
        switch(techominoType){
            case 0:
                render_dx = -BLOCK_SIZE/2;
                render_dy = -BLOCK_SIZE/2;
                break;
            case 3:
                render_dx = -BLOCK_SIZE/2;
                render_dy = 0;
                break;
            default:
                render_dx = 0;
                render_dy = 0;
                break;
        }
        update_texture();
    }
}
