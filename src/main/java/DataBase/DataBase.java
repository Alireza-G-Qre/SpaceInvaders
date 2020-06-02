package DataBase;

import DataBase.Adaptors.Data;
import DataBase.Adaptors.Savable;
import com.gilecode.yagson.YaGson;
import com.sun.istack.internal.NotNull;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataBase {

    private static YaGson yaGson = new YaGson();

    @org.jetbrains.annotations.NotNull
    @NotNull
    public static List<Savable<?>> load(@org.jetbrains.annotations.NotNull @NotNull Class<? extends Savable<?>> cls) {

        List<Savable<?>> savableList = new ArrayList<>();

        try (Stream<Path> pathStream = Files.walk(Paths.get(filePath(cls.getSimpleName()))).filter(Files::isRegularFile)) {
            pathStream.forEach(path -> {
                try {
                    String content = Files.readAllLines(path).get(0);
                    Data<?> data = yaGson.fromJson(content, Data.class);
                    savableList.add(data.getObj().deSerial(data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savableList;
    }

    public static void save(@org.jetbrains.annotations.NotNull @NotNull Savable<?> savable) {

        String path = String.format("%s/%s", filePath(savable.getClass().getSimpleName()), savable.getSaveCode());

        try {

            File file = new File(path);

            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("File didn't create!");
            }

            FileWriter writer = new FileWriter(file);

            String content = yaGson.toJson(savable.serial());

            writer.write(content);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @org.jetbrains.annotations.NotNull
    @Contract(pure = true)
    private static String filePath(String name) {
        return "src/main/resources/" + name + "s";
    }
}
