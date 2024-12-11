package com.Cubicheng.MyTetr.gameWorld.components.piece;

import com.Cubicheng.MyTetr.GameApp;
import com.Cubicheng.MyTetr.gameWorld.ImageBuffer;
import com.Cubicheng.MyTetr.gameWorld.components.GameMapComponent;
import com.Cubicheng.MyTetr.gameWorld.techominoData.Techomino;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.FXGL;

import com.Cubicheng.MyTetr.gameWorld.Type;
import javafx.scene.image.ImageView;

import static com.Cubicheng.MyTetr.gameWorld.Constants.*;

public class OnePieceComponent extends Component {

    protected ImageView now_texture;

    protected int x, y;

    protected double render_dx = 0, render_dy = 0;

    protected int rotate_index = 0;

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    protected int techominoType;
    protected Techomino techomino;

    protected double startX;
    protected double startY;

    public OnePieceComponent(double x,double y){
        startX = x;
        startY = y;
    }

    double visibility = 1.0;

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

    public int get_techomino_type() {
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
        if(techomino == null){
            return ;
        }
        getEntity().getViewComponent().clearChildren();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(now_texture.getImage());
            imageView.setOpacity(visibility);
            imageView.setLayoutX(render_dx + techomino.techomino[rotate_index][i].first() * BLOCK_SIZE);
            imageView.setLayoutY(render_dy - techomino.techomino[rotate_index][i].second() * BLOCK_SIZE);
            getEntity().getViewComponent().addChild(imageView);
        }
    }

    protected boolean can_move_to(int x, int y) {
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