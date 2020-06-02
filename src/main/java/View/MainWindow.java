package View;

import View.Menus.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class MainWindow extends Application {

    private static Scene currentScene = new Login().display();
    private static Stage primaryStage = null;

    public static void setCurrentScene(Scene currentScene) {
        MainWindow.currentScene = currentScene;
        primaryStage.setScene(currentScene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        MainWindow.primaryStage = primaryStage;
        primaryStage.setScene(currentScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void loadingFailed(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setOnCloseRequest(event -> System.exit(-1));
        alert.showAndWait();
    }

    public static void invalidInput(String title, String massage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, massage);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
