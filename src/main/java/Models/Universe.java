package Models;

import View.Menus.Game;
import javafx.scene.paint.Color;

import java.util.Random;

public class Universe {

    private int posX, posY;
    private int h, w, r, g, b;
    private double opacity;

    public int getPosY() {
        return posY;
    }

    public Universe () {
        Random random = Game.getRandom();
        posX = random.nextInt(Game.getWidth());
        posY = 0;
        w = random.nextInt(5) + 1;
        h = random.nextInt(5) + 1;
        r = random.nextInt(100) + 150;
        g = random.nextInt(100) + 150;
        b = random.nextInt(100) + 150;
        opacity = Math.abs(random.nextDouble());
        opacity = Math.min(opacity, 0.5);
    }

    public void draw() {
        if (opacity > 0.8) opacity -= 0.1;
        if (opacity < 0.1) opacity += 0.1;
        Game.getGc().setFill(Color.rgb(r,g,b,opacity));
        Game.getGc().fillOval(posX,posY,w,h);
        posY += 10;
    }
}
