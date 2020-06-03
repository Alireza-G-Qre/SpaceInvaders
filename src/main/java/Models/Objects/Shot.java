package Models.Objects;

import Models.MainObject;
import View.Menus.Game;
import javafx.scene.paint.Color;

public class Shot extends MainObject {

    // fields
    private static int shotSpeed = 10;
    private static final int size = 6;
    private static int max_Shots = 20;

    // getter and setters
    public static int getMax_Shots() {
        return max_Shots;
    }

    public static int getSize() {
        return size;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }

    public static int getShotSpeed() {
        return shotSpeed;
    }

    public static void setShotSpeed(int shotSpeed) {
        Shot.shotSpeed = shotSpeed;
    }

    public int getPosY() {
        return posY;
    }

    // controllers
    public static void setup() {
        setShotSpeed(8);
        max_Shots = 20;
    }

    @Override
    public void update() {
        setPosY(getPosY() - getShotSpeed());
    }

    @Override
    public void drawObj() {

        if ((Game.getScoreForGame() / 10) % 3 == 2) {
            shot_T02();
            return;
        }
        shot_T01();
        setShotSpeed(10);
        max_Shots = 20;
    }

    @Override
    public void ifCollide() {
        setRemove(true);
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
        setShotSpeed(50);
    }

    // constructors
    public Shot(int objectSize, int posX, int posY) {
        super(objectSize, posX, posY);
    }
}
