package Models.Objects;

import Models.MainObject;
import Models.Objects.Racket;
import View.Menus.Game;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class Shot extends MainObject {

    // fields
    private static final int size = 6;
    private static int max_Shots = 20;
    static {
        speed = 8;
    }

    // getter and setters
    public static int getMax_Shots() {
        return max_Shots;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }

    public int getPosY() {
        return posY;
    }

    // controllers
    public static void setup() {
        setSpeed(8);
        max_Shots = 20;
    }

    @Override
    public void update() {
        setPosY(getPosY() - getSpeed());
    }

    @Override
    public void drawObj() {

        if ((Game.getScoreForGame() / 10) % 3 == 2) {
            shot_T02();
            return;
        }
        shot_T01();
        setSpeed(20);
        max_Shots = 20;
    }

    @Override
    public void ifCollide() {
        //
    }

    private void shot_T01() {
        Game.getGc().setFill(Color.RED);
        Game.getGc().fillOval(posX, posY + 10, size, size);
    }

    private void shot_T02() {
        Game.getGc().setFill(Color.YELLOWGREEN);
        Game.getGc().fillRect(posX - size, posY + 10, size, size + 30);
        Game.getGc().fillRect(posX + size, posY + 10, size, size + 30);
        max_Shots = 10000000;
        setSpeed(50);
    }

    // constructors
    public Shot(int objectSize, int posX, int posY) {
        super(objectSize, posX, posY);
    }
}
