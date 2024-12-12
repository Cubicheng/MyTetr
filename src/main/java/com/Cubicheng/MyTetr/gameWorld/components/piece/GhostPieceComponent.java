package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameScenes.MainMenu;
import com.Cubicheng.MyTetr.gameWorld.ConfigVars;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.image.ImageView;

public class GhostPieceComponent extends OnePieceComponent {

    public GhostPieceComponent(double x, double y, int id) {
        super(x, y, id);
    }

    @Override
    public void onAdded() {
        x = 4;
        y = 20;
        now_texture = new ImageView(ImageBuffer.texture[9].image());
        visibility = ConfigVars.ghost_piece_visibility;

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
                .with(new GhostPieceComponent(data.get("startX"), data.get("startY"), data.get("id")))
                .with(components)
                .type(Type.GhostPiece)
                .zIndex(Integer.MAX_VALUE - 10)
                .build();
    }

    @Override
    public void onUpdate(double tpf) {
        if (FXGL.<GameApp>getAppCast().get_last_gameScene().getClass() == MainMenu.class) {
            return;
        }
        Entity movablePiece = get_entity(Type.MovablePiece, 0);
        assert movablePiece != null;
        this.techomino = movablePiece.getComponent(MovablePieceComponent.class).get_techomino();
        rotate_index = movablePiece.getComponent(MovablePieceComponent.class).get_rotate_index();
        x = movablePiece.getComponent(MovablePieceComponent.class).getX();
        y = movablePiece.getComponent(MovablePieceComponent.class).getY();
        while (can_move_to(x, y - 1)) {
            y--;
        }
        update_entity_position();
        update_texture();
    }

    public int getX(int id) {
        return getX() + techomino.techomino[rotate_index][id].first();
    }

    public int getY(int id) {
        return getY() + techomino.techomino[rotate_index][id].second();
    }
}
