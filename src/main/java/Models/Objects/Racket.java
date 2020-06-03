package Models.Objects;

import Models.MainObject;
import View.Menus.Game;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Racket extends MainObject {

    // Static content
    protected static final int move_steps = 10;
    protected static final int RacketSize = 60;

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
    protected int explosionStep;
    protected Image image;
    protected boolean explosion;

    // Getter and setters
    public boolean isExplosion() {
        return explosion;
    }

    public static int getRacketSize() {
        return RacketSize;
    }

    // controller method
    public void moveToUp() {
        setPosY(Math.max(0, posY - move_steps));
    }

    public void moveToLeft() {
        setPosX(Math.max(0, posX - move_steps));
    }

    public void moveToRight() {
        setPosX(Math.min(
                Game.getWidth() - Racket.getRacketSize(),
                posX + move_steps
        ));
    }

    public void moveToDown() {
        setPosX(Math.min(Game.getHeight() - Racket.getRacketSize(),
                posY + move_steps
        ));
    }

    public Shot shoot() {
        return new Shot(
                Shot.getSize(),
                posX + Racket.getRacketSize() / 2,
                posY - Racket.getRacketSize() / 2
        );
    }

    @Override
    public void update() {

        if (explosion) {
            if (explosionStep % 5 == 0) {
                image = explosion_images.get(explosionStep / 5);
            }
            setRemove(++explosionStep >= explosion_steps * 5);
        }
    }

    @Override
    public void drawObj() {
        Game.getGc().drawImage(image, posX, posY, Racket.getRacketSize(), Racket.getRacketSize());
    }

    @Override
    public void ifCollide() {
        explosion = true;
        explosionStep = 0;
    }

    @NotNull
    @Contract(" -> new")
    public static Racket newRacket() {
        return new Racket(
                Racket.getRacketSize(),
                Game.getWidth() / 2,
                Game.getHeight() - Racket.getRacketSize(),
                rocket_images.get(Game.getRandom().nextInt(rocket_images.size()))
        );
    }

    // constructors
    protected Racket(int objectSize, int posX, int posY, Image image) {
        super(objectSize, posX, posY);
        this.image = image;
    }
}
