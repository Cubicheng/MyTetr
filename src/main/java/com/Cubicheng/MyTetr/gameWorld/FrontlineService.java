package com.Cubicheng.MyTetr.gameWorld;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.whitewoodcity.fxgl.service.DimensionService;

import java.util.Arrays;

public interface FrontlineService extends DimensionService {

    default void updateFrontLine() {
        updateFrontLine(FXGL.getGameWorld());
    }

    default void updateFrontLine(GameWorld... gameWorlds) {
        Arrays.stream(gameWorlds).forEach(this::updateFrontLine);
    }

    public GameWorld get_game_world();
}
