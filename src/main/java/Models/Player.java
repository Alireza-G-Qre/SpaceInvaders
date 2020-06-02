package Models;

import DataBase.Adaptors.Data;
import DataBase.Adaptors.Savable;
import DataBase.DataBase;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Player implements Savable<Player> , Comparable<Player>{

    // Data methods
    private static List<Player> playerList = DataBase.load(Player.class).stream()
            .map(savable -> (Player) savable)
            .collect(Collectors.toList());

    public static Player getPlayerByUserName(String username) throws NoSuchElementException {
        return playerList.stream()
                .filter(player -> username.equals(player.getUsername()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player not found."));
    }

    public static boolean isTherePlayerWithThisUserUsername(String username) {
        return playerList.stream().anyMatch(player -> username.equals(player.getUsername()));
    }

    public static void addPlayer(@NotNull Player player) {
        playerList.add(player);
        DataBase.save(player);
    }

    @NotNull
    public static List<Player> getPlayerListSortedByRecord() {
        return Player.playerList.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @Override
    public int compareTo(@NotNull Player o) {
        return Integer.compare(getGreatRecord(),o.getGreatRecord());
    }

    // Fields
    private String username;
    private String password;
    private int greatRecord;

    // Getter and Setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getGreatRecord() {
        return greatRecord;
    }

    public void setGreatRecord(int greatRecord) {
        this.greatRecord = greatRecord;
        DataBase.save(this);
    }

    // Savable methods
    @Override
    public String getSaveCode() {
        return getUsername();
    }

    @Override
    public Data<Player> serial() {
        return new Data<>(new Player())
                .addField(getUsername())
                .addField(getPassword())
                .addField(getGreatRecord());
    }

    @Override
    public Player deSerial(@NotNull Data<?> data) {
        List<Object> objects = data.getFields();
        this.username = (String) objects.get(0);
        this.password = (String) objects.get(1);
        this.greatRecord = (int) objects.get(2);
        return this;
    }

    // Constructors
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private Player() {
    }
}
