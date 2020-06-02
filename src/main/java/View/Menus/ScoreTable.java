package View.Menus;

import Models.Player;
import Tools.IWindow;
import View.MainWindow;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreTable implements IWindow, Initializable {

    private static Player me = null;
    private long num = 0;

    @FXML
    private TextField myRank;
    @FXML
    private TextField myRecord;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player,Long> ranks;
    @FXML
    private TableColumn<Player,String> usernames;
    @FXML
    private TableColumn<Player,String> records;

    @Override
    public Scene display() {

        try {
            return FXMLLoader.load(new File("src/main/resources/ScoreTable/ScoreTable.fxml").toURI().toURL());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            MainWindow.loadingFailed("loading ScoreTable failed.");
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Player> playerList = Player.getPlayerListSortedByRecord();

        table.setItems(FXCollections.observableList(playerList));

        ranks.setCellValueFactory(param -> new SimpleLongProperty(++num).asObject());
        usernames.setCellValueFactory(new PropertyValueFactory<>("username"));
        records.setCellValueFactory(new PropertyValueFactory<>("greatRecord"));

        myRank.setText(playerList.indexOf(me) + 1 + "");
        myRecord.setText(me.getGreatRecord() + "");
    }

    public static void setMe(Player me) {
        ScoreTable.me = me;
    }

    public void backToGame() {
        MainWindow.setCurrentScene(new Game().display());
    }
}
