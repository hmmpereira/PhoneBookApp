package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.model.Contact;
import sample.model.Datasource;

import java.util.List;

public class ContactController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneNumberField;


    public void addNewContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        try {
            int phoneNumber = (Integer) Integer.parseInt(phoneNumberField.getText());

            Datasource.getInstance().insertContactIntoPhonebook(name, address, phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("The phone number was not a number. Please insert again without any spaces!");
        }
    }

    public void editContact(Contact contact) {
        String name = nameField.getText();
        if (name.isEmpty()) {
            name = contact.getName();
        }
        String address = addressField.getText();
        if (address.isEmpty()) {
            address = contact.getAddress();
        }
        try {
            int phoneNumber = (Integer) Integer.parseInt(phoneNumberField.getText());
            if (phoneNumber <= 0) {
                phoneNumber = contact.getPhoneNumber();
            }
            Datasource.getInstance().updateTables(contact.getId(), contact.getAddress(), contact.getPhoneNumber(), name, address, phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("The phone number was not a number. Please insert again without any spaces!");
        }
    }

    public List<Contact> searchContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        if (phoneNumberField.getText().isEmpty()) {
            return Datasource.getInstance().queryContact(name, address);
        } else {
            try {
                int phoneNumber = (Integer) Integer.parseInt(phoneNumberField.getText());

                return Datasource.getInstance().queryContact(name, address, phoneNumber);
            } catch (NumberFormatException e) {
                System.out.println("Check if the phone number is correct! (Only numbers are allowed!");
                return Datasource.getInstance().queryContact(name, address);
            }
        }
    }

}















