package DataBase.Adaptors;

public interface Savable<T extends Savable<T>> {

    String getSaveCode();

    Data<T> serial();

    T deSerial(Data<?> data);
}
