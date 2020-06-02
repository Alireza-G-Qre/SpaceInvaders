package Models;

import View.Menus.Game;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Racket {

    // Static content
    protected static final int move_steps = 10;
    protected static int size = 60;

    protected static List<Image> explosion_images = Arrays.asList(
            new Image("file:src/main/resources/Images/Explosion/Explosion-01.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-02.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-03.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-04.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-05.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-06.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-07.jpg"),
            new Image("file:src/main/resources/Images/Explosion/Explosion-08.jpg")
    );

    protected static int explosion_steps = explosion_images.size();

    private static List<Image> rocket_images = Arrays.asList(
            new Image("file:src/main/resources/Images/Rackets/Racket-01.png"),
            new Image("file:src/main/resources/Images/Rackets/Racket-02.png"),
            new Image("file:src/main/resources/Images/Rackets/Racket-03.png")
    );

    // field
    protected int posX, posY, explosionStep;
    protected Image image;
    protected boolean ok = true, destroyed = false;

    // Getter and setters
    public boolean isOk() {
        return ok;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public static int getSize() {
        return size;
    }

    // controller method
    public void moveToUp() {
        posY = Math.max(0, posY- move_steps);
    }

    public void moveToLeft() {
        posX = Math.max(0, posX - move_steps);
    }

    public void moveToRight() {
        posX = Math.min(Game.getWidth() - size, posX + move_steps);
    }

    public void moveToDown() {
        posY = Math.min(Game.getHeight() - size, posY + move_steps);
    }

    public Shot shoot() {
        return new Shot(posX + Racket.size / 2, posY - Racket.size / 2);
    }

    public void update() {

        if (!ok) {
            if(explosionStep % 5 == 0) {
                image = explosion_images.get(explosionStep / 5);
            }
            destroyed = ++explosionStep >= explosion_steps * 5;
        }
    }

    public void draw() {
        Game.getGc().drawImage(image, posX, posY, size, size);
    }

    public boolean collide(@NotNull Racket other) {
        int disX = Math.abs(posX - other.posX);
        int disY = Math.abs(posY - other.posY);
        return Math.sqrt(disX * disX + disY * disY) < size;
    }

    public void explode() {
        ok = false;
        explosionStep = 0;
    }

    @NotNull
    @Contract(" -> new")
    public static Racket newRacket() {
        return new Racket(Game.getWidth() / 2,
                Game.getHeight() - Racket.getSize(),
                rocket_images.get(Game.getRandom().nextInt(rocket_images.size()))
        );
//        return null;
    }

    // constructors
    protected Racket(int posX, int posY, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }
}
