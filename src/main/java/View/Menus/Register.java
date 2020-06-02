package View.Menus;

import Models.Player;
import Tools.IWindow;
import View.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Register implements IWindow, Initializable {

    private static String s = new File("src/main/resources/Register/register-clip.mp4").toURI().toString();
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordOne;
    @FXML
    private PasswordField passwordTow;
    @FXML
    private CheckBox checkbox;
    @FXML
    private Button register;
    @FXML
    private MediaView mediaView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media media = new Media(s);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    @Override
    public Scene display() {
        try {
            return FXMLLoader.load(new File("src/main/resources/Register/Register.fxml").toURI().toURL());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            MainWindow.loadingFailed("loading register menu failed.");
        }
        return null;
    }

    public void checkbox(ActionEvent actionEvent) {
        register.setDisable(!checkbox.isSelected());
    }

    public void register(ActionEvent actionEvent) {
        String username = this.username.getText();
        String passwordOne = this.passwordOne.getText();
        String passwordTow = this.passwordTow.getText();

        if (Player.isTherePlayerWithThisUserUsername(username)) {
            MainWindow.invalidInput("Duplicated username", "Enter another one.");
            return;
        }

        if (!passwordOne.equals(passwordTow)) {
            MainWindow.invalidInput("Not match passwords", "passOne and passTow aren't match together.");
            return;
        }

        Player.addPlayer(new Player(username, passwordOne));

        MainWindow.setCurrentScene(new Login().display());
    }
}
