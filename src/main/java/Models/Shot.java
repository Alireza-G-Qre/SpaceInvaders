package Models;

import Models.Objects.Racket;
import View.Menus.Game;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class Shot {

    // fields
    private static final int size = 6;
    private static int max_Shots = 20;
    private static double speed = 8;
    private boolean remove;
    private int posX, posY;

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
    public void update() {
        posY -= speed;
    }

    public void draw() {

        if ((Game.getScoreForGame() / 10) % 3 == 2) {
            Game.getGc().setFill(Color.YELLOWGREEN);
            speed = 50;
            Game.getGc().fillRect(posX - size, posY + 10, size, size + 30);
            Game.getGc().fillRect(posX + size, posY + 10, size, size + 30);
            max_Shots = 10000000;
            return;
        }

        if (max_Shots != 20) {
            max_Shots = 20;
            speed = 8;
        }

        Game.getGc().setFill(Color.RED);
        Game.getGc().fillOval(posX, posY + 10, size, size);
    }

    public static void setup() {
        speed = 8;
        max_Shots = 20;
    }

    public boolean collide(@NotNull Racket other) {
        int disX = Math.abs(posX - other.posX);
        int disY = Math.abs(posY - other.posY);
        return Math.sqrt(disX * disX + disY * disY) < Racket.getSize() / 2.0 + size / 2.0;
    }

    // constructors
    public Shot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
