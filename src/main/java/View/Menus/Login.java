package View.Menus;

import Models.Player;
import Tools.IWindow;
import View.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Login implements IWindow {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private CheckBox checkbox;

    @Override
    public Scene display() {

        try {
            return FXMLLoader.load(new File("src/main/resources/Login/Login.fxml").toURI().toURL());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            MainWindow.loadingFailed("loading login menu failed.");
        }
        return null;
    }

    public void login(ActionEvent actionEvent) {
        String username = this.username.getText();
        String password = this.password.getText();

        try {
            Player player = Player.getPlayerByUserName(username);

            if (!player.getPassword().equals(password)) {
                MainWindow.invalidInput("Wrong username", "Your password is incorrect.");
                return;
            }

            Game.setPlayer(player);

            MainWindow.setCurrentScene(new Game().display());

        } catch (NoSuchElementException e) {
            MainWindow.invalidInput("Wrong username", "Player with this username not found.");
        }
    }

    public void register(ActionEvent actionEvent) {
        Register register = new Register();
        MainWindow.setCurrentScene(register.display());
    }

    public void checkbox(ActionEvent actionEvent) {
        login.disableProperty().setValue(!checkbox.isSelected());
        register.disableProperty().setValue(!checkbox.isSelected());
    }
}
