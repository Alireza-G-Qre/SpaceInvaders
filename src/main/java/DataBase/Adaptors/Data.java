package DataBase.Adaptors;

import java.util.ArrayList;
import java.util.List;

public class Data<T extends Savable<T>> {

    private T obj;
    private List<Object> fields = new ArrayList<>();

    public List<Object> getFields() {
        return fields;
    }

    public Savable<T> getObj() {
        return obj;
    }

    public Data<T> addField(Object obj) {
        fields.add(obj);
        return this;
    }

    public Data(T obj) {
        this.obj = obj;
    }
}
