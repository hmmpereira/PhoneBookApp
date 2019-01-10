package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Address {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty phoneNumberId;

    public Address() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.phoneNumberId = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPhoneNumberId() {
        return phoneNumberId.get();
    }

    public void setPhoneNumberId(int phoneNumberId) {
        this.phoneNumberId.set(phoneNumberId);
    }
}
