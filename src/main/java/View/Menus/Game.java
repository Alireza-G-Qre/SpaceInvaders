package View.Menus;

import Models.*;
import Models.Objects.Racket;
import Tools.IWindow;
import View.MainWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;

public class Game implements IWindow {

    // fields
    private Media gameMusic = new Media(new File("src/main/resources/Game/GameMusic.mp3").toURI().toString());
    private Image image = new Image("file:src/main/resources/Game/game-image.jpg");
    private static Player player = null;
    @FXML
    private Button startOrStop;
    @FXML
    private Canvas canvas;
    @FXML
    private Label state;
    @FXML
    private Label scoreView;

    private static Random random = new Random();
    private List<Universe> universeList;
    private List<Shot> shotList;
    private List<Bomb> bombList;
    private Racket ownRacket;
    private boolean gameOver;
    private static int width;
    private static int height;
    private static int scoreForGame;
    private static GraphicsContext gc;
    private MediaPlayer gameMusicPlayer = new MediaPlayer(gameMusic);
    private Thread gameMusicThread = new Thread(() -> gameMusicPlayer.play());

    private Timeline gameLineControl;
    private Timeline gameLineDrawing;

    @Override
    public Scene display() {

        try {
            return FXMLLoader.load(new File("src/main/resources/Game/Game.fxml").toURI().toURL());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            MainWindow.loadingFailed("loading game menu failed.");
        }
        return null;
    }

    // Getter and setters

    public static int getScoreForGame() {
        return scoreForGame;
    }

    public static void setPlayer(Player player) {
        Game.player = player;
    }

    public void setOwnRacket(Racket ownRacket) {
        this.ownRacket = ownRacket;
    }

    public static void setScoreForGame(int scoreForGame) {
        Game.scoreForGame = scoreForGame;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static Random getRandom() {
        return random;
    }

    public static GraphicsContext getGc() {
        return gc;
    }

    // controllers
    public void start() {

        width = (int) MainWindow.getPrimaryStage().getScene().getWidth();
        height = (int) (0.95 * MainWindow.getPrimaryStage().getScene().getHeight());

        Scene scene = MainWindow.getPrimaryStage().getScene();
        scene.setOnKeyPressed(null);

        setup_Rackets();
        setup_Score();

        gc = canvas.getGraphicsContext2D();

        gameLineControl = new Timeline(new KeyFrame(Duration.millis(10), e -> run_game_controller()));
        gameLineControl.setCycleCount(Timeline.INDEFINITE);

        gameLineDrawing = new Timeline(new KeyFrame(Duration.millis(40), e -> run_game_draw(gc)));
        gameLineDrawing.setCycleCount(Timeline.INDEFINITE);

        gameMusicThread.start();
        gameMusicPlayer.setOnEndOfMedia(() -> gameMusicPlayer.seek(Duration.ZERO));

        startGame();

        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case CONTROL:
                    if (shotList.size() < Shot.getMax_Shots()) {
                        shotList.add(ownRacket.shoot());
                    }
                    break;
                case RIGHT:
                    ownRacket.moveToRight();
                    break;
                case LEFT:
                    ownRacket.moveToLeft();
                    break;
                case UP:
                    ownRacket.moveToUp();
                    break;
                case DOWN:
                    ownRacket.moveToDown();
            }
        });
    }

    private void setup_Score() {
        setScoreForGame(0);
        updateScoreViewText(0);
    }

    private void setup_Rackets() {
        bombList = new ArrayList<>();
        shotList = new ArrayList<>();
        universeList = new ArrayList<>();

        for (int i = 0; i < Bomb.getMax_Bombs(); i++) {
            bombList.add(Bomb.newBomb());
        }

        setOwnRacket(Racket.newRacket());
        gameOver = false;
    }

    public void run_game_controller() {

        bombList.removeIf(Racket::isDestroyed);

        universeList.removeIf(universe -> universe.getPosY() > height);

        shotList.removeIf(shot -> shot.getPosY() < 0 || shot.isRemove());

        if (bombList.size() < Bomb.getMax_Bombs()) bombList.add(Bomb.newBomb());

        if (ownRacket.isOk() && bombList.stream().anyMatch(bomb -> ownRacket.collide(bomb))) ownRacket.explode();

        for (Shot shot : shotList) {
            for (Bomb bomb : bombList) {
                if (bomb.isOk() && shot.collide(bomb)) {
                    bomb.explode();
                    scoreForGame++;
                    shot.setRemove(true);
                    new Thread(
                            () -> Bomb.getVoice().play()
                    ).start();
                }
            }
        }

        Bomb.setSpeed(getScoreForGame() / 30.0 + 2);

        universeList.add(new Universe());

        gameOver = ownRacket.isDestroyed();

        if (gameOver) {
            gameOver_state_controller();
            MainWindow.setCurrentScene(new Game().display());
        }
    }

    public void run_game_draw(@NotNull GraphicsContext gc) {

        updateScoreViewText(scoreForGame);

        gc.drawImage(image, 0, 0, width, height);

        if (gameOver) {
            gameOver_state_draw();
            return;
        }

        universeList.forEach(Universe::draw);

        ownRacket.update();
        ownRacket.draw();

        bombList.forEach(bomb -> {
            bomb.update();
            bomb.draw();
        });

        shotList.forEach(shot -> {
            shot.update();
            shot.draw();
        });
    }

    private void gameOver_state_draw() {

        state.setText("Game Over.");
        state.setLayoutX(width / 2.0);
        state.setLayoutX(height / 2.0);
        state.setVisible(true);

        gameLineDrawing.stop();
    }

    private void gameOver_state_controller() {

        if (scoreForGame > player.getGreatRecord()) player.setGreatRecord(scoreForGame);

        setScoreForGame(0);

        gameLineControl.stop();
        gameMusicPlayer.dispose();

        Shot.setup();
        Bomb.setup();
    }

    private void stopGame() {
        gameLineControl.stop();
        gameLineDrawing.stop();
        gameMusicPlayer.pause();
        state.setText("Stop..");
    }

    private void startGame() {
        gameLineControl.play();
        gameLineDrawing.play();
        gameMusicPlayer.play();
        state.setVisible(false);
    }

    public void showScoreTable() {
        gameOver_state_draw();
        ScoreTable.setMe(player);
        gameOver_state_controller();
        MainWindow.setCurrentScene(new ScoreTable().display());
    }

    private void updateScoreViewText(int score) {
        scoreView.setText(
                String.format("SCORE : %s", score)
        );
    }

    public void stopOrStart(MouseEvent actionEvent) {
        stopGame();
        startOrStop.setOnMouseClicked(event -> {
            startGame();
            startOrStop.setOnMouseClicked(this::stopOrStart);
        });
    }
}
