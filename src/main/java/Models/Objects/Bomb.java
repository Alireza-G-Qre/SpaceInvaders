package Models.Objects;

import View.Menus.Game;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bomb extends Racket {

    private int moveNum;
    private int moveState;
    private static int speed = 2;
    private static final int Max_Bombs = 10;
    private static List<Image> bombs_image = Arrays.asList(
            new Image("file:src/main/resources/Images/Bombs/Bomb-01.png"),
            new Image("file:src/main/resources/Images/Bombs/Bomb-02.png"),
            new Image("file:src/main/resources/Images/Bombs/Bomb-03.png"),
            new Image("file:src/main/resources/Images/Bombs/Bomb-04.png"),
            new Image("file:src/main/resources/Images/Bombs/Bomb-05.png")
    );

    private static final Media explosion_Sound = new Media(
            new File("src/main/resources/Game/explosion.wav").toURI().toString()
    );

    // Getter and Setters
    public static int getMax_Bombs() {
        return Max_Bombs;
    }

    public static int getSpeed() {
        return speed;
    }

    @NotNull
    @Contract(" -> new")
    public static MediaPlayer getVoice() {
        return new MediaPlayer(explosion_Sound);
    }

    public static void setSpeed(int speed) {
        Bomb.speed = speed;
    }

    // controllers
    @Override
    public void update() {
        super.update();
        if (!explosion) {
            move();
            setPosY(getPosY() + getSpeed());
            if (posY > Game.getHeight()) {
                explosion = false;
                setRemove(true);
            }
        }
    }

    public static void setup() {
        setSpeed(2);
    }

    private void move() {

        if (moveNum == 0) {

            Random random = new Random();
            moveState = random.nextInt(3);
            moveNum = 25;
        }

        switch (moveState) {
            case 0:
                moveToRight();
                break;
            case 1:
                moveToLeft();
                break;
        }

        moveNum--;
    }

    @Override
    public void ifCollide() {
        super.ifCollide();
        new Thread(
                () -> Bomb.getVoice().play()
        ).start();
    }

    // constructors
    @NotNull
    @Contract(" -> new")
    public static Bomb newBomb() {
        return new Bomb(
                Racket.getRacketSize(),
                Game.getRandom().nextInt(Game.getWidth() - Racket.getRacketSize()),
                Game.getRandom().nextInt(Game.getHeight() / 4),
                Bomb.bombs_image.get(Game.getRandom().nextInt(Bomb.bombs_image.size()))
        );
//        return null;
    }

    private Bomb(int objectSize, int posX, int posY, Image image) {
        super(objectSize, posX, posY, image);
    }
}
