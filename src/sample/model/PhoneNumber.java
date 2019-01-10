package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class PhoneNumber {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty number;

    public PhoneNumber() {
        this.id = new SimpleIntegerProperty();
        this.number = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int number) {
        this.number.set(number);
    }
}
