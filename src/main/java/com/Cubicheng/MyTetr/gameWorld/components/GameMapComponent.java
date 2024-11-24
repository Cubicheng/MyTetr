package com.Cubicheng.MyTetr.gameWorld.components;

import com.Cubicheng.MyTetr.gameWorld.Type;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

import java.util.Vector;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class GameMapComponent extends Component {

    private Vector< Vector<Integer> > playfiled = new Vector<>(MAP_HEIGHT);

    public GameMapComponent() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            Vector<Integer> row = new Vector<>(MAP_WIDTH);
            for (int j = 0; j < MAP_WIDTH; j++) {
                row.add(0);
            }
            playfiled.add(row);
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
        return builder.build();
    }
}
