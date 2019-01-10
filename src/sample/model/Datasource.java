package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "PhoneBook.db";

    public static final String FILE_PATH = System.getProperty("user.dir");

    public static final String CONNECTION_STRING = "jdbc:sqlite:" + FILE_PATH+ "/" + DB_NAME;

    public static final String TABLE_ADDRESS = "address";
    public static final String COLUMN_ADDRESS_ID = "_id";
    public static final String COLUMN_ADDRESS_NAME = "name";
    public static final String COLUMN_ADDRESS_PHONENUMBER = "phoneNumber";

    public static final String TABLE_PHONENUMBER = "phoneNumber";
    public static final String COLUMN_PHONENUMBER_ID = "_id";
    public static final String COLUMN_PHONENUMBER_NUMBER = "number";

    public static final String TABLE_CONTACTS = "Contacts";
    public static final String COLUMN_CONTACTS_ID = "_id";
    public static final String COLUMN_CONTACTS_NAME = "name";
    public static final String COLUMN_CONTACTS_ADDRESS = "address";
    public static final String COLUMN_CONTACTS_PHONENUMBER = "phoneNumber";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String INSERT_PHONENUMBER = "INSERT INTO " + TABLE_PHONENUMBER +
            '(' + COLUMN_PHONENUMBER_NUMBER + ") VALUES(?)";

    public static final String INSERT_ADDRESS = "INSERT INTO " + TABLE_ADDRESS +
            '(' + COLUMN_ADDRESS_NAME + ", " + COLUMN_ADDRESS_PHONENUMBER + ") VALUES(?, ?)";

    public static final String INSERT_CONTACT = "INSERT INTO " + TABLE_CONTACTS +
            "(" + COLUMN_CONTACTS_NAME + ", " + COLUMN_CONTACTS_ADDRESS + ", " + COLUMN_CONTACTS_PHONENUMBER +
            ") VALUES(?, ?, ?)";

    public static final String QUERY_PHONENUMBERS = "SELECT " + COLUMN_PHONENUMBER_ID + " FROM " +
            TABLE_PHONENUMBER + " WHERE " + COLUMN_PHONENUMBER_NUMBER + " = ?";

    public static final String QUERY_CONTACTS = "SELECT " + COLUMN_CONTACTS_ID + " FROM " +
            TABLE_CONTACTS + " WHERE " + COLUMN_CONTACTS_NAME + " = ? AND " + COLUMN_CONTACTS_ADDRESS + " = ? AND " +
            COLUMN_CONTACTS_PHONENUMBER + " = ?";

    public static final String QUERY_PHONEBOOK = "SELECT * FROM " +
            TABLE_CONTACTS + " WHERE " + COLUMN_CONTACTS_NAME + " = ? OR " + COLUMN_CONTACTS_ADDRESS + " = ? OR " +
            COLUMN_CONTACTS_PHONENUMBER + " = ?";

    public static final String QUERY_ADDRESSES = "SELECT " + COLUMN_ADDRESS_ID + " FROM " +
            TABLE_ADDRESS + " WHERE " + COLUMN_ADDRESS_NAME + " = ?";

    public static final String UPDATE_CONTACTS = "UPDATE " + TABLE_CONTACTS + " SET " +
            COLUMN_CONTACTS_NAME + " = ?, " + COLUMN_CONTACTS_ADDRESS + " = ?, " +
            COLUMN_CONTACTS_PHONENUMBER + " = ? WHERE " + COLUMN_CONTACTS_ID + " = ?";

    public static final String UPDATE_CONTACTS_NUMBER = "UPDATE " + TABLE_PHONENUMBER + " SET " +
            COLUMN_PHONENUMBER_NUMBER + " = ? WHERE " + COLUMN_PHONENUMBER_NUMBER + " = ?";

    public static final String UPDATE_CONTACTS_ADDRESS = "UPDATE " + TABLE_ADDRESS + " SET " +
            COLUMN_ADDRESS_NAME + " = ? WHERE " + COLUMN_ADDRESS_NAME + " = ?";

    public static final String DELETE_CONTACT = "DELETE FROM " +
            TABLE_CONTACTS + " WHERE " + COLUMN_CONTACTS_NAME + " = ? AND " + COLUMN_CONTACTS_ADDRESS + " = ?";

    public static final String DELETE_ADDRESS = "DELETE FROM " +
            TABLE_ADDRESS + " WHERE " + COLUMN_ADDRESS_NAME + " = ?";

    public static final String DELETE_PHONENUMBER = "DELETE FROM " +
            TABLE_PHONENUMBER + " WHERE " + COLUMN_PHONENUMBER_NUMBER + " = ?";

    private Connection conn;

    private PreparedStatement insertIntoPhoneNumber;
    private PreparedStatement insertIntoAddresses;
    private PreparedStatement insertIntoContacts;

    private PreparedStatement queryPhoneNumber;
    private PreparedStatement queryAddresses;
    private PreparedStatement queryContacts;
    private PreparedStatement queryPhoneBook;
    private PreparedStatement updateContactsName;
    private PreparedStatement updateContactsNumber;
    private PreparedStatement updateContactsAddress;
    private PreparedStatement deleteContact;
    private PreparedStatement deleteAddress;
    private PreparedStatement deletePhoneNumber;

    private static Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoPhoneNumber = conn.prepareStatement(INSERT_PHONENUMBER, Statement.RETURN_GENERATED_KEYS);
            insertIntoAddresses = conn.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
            insertIntoContacts = conn.prepareStatement(INSERT_CONTACT);
            queryPhoneNumber = conn.prepareStatement(QUERY_PHONENUMBERS);
            queryAddresses = conn.prepareStatement(QUERY_ADDRESSES);
            queryContacts = conn.prepareStatement(QUERY_CONTACTS);
            queryPhoneBook = conn.prepareStatement(QUERY_PHONEBOOK);
            updateContactsName = conn.prepareStatement(UPDATE_CONTACTS);
            updateContactsNumber = conn.prepareStatement(UPDATE_CONTACTS_NUMBER);
            updateContactsAddress = conn.prepareStatement(UPDATE_CONTACTS_ADDRESS);
            deleteContact = conn.prepareStatement(DELETE_CONTACT);
            deleteAddress = conn.prepareStatement(DELETE_ADDRESS);
            deletePhoneNumber = conn.prepareStatement(DELETE_PHONENUMBER);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            if(insertIntoPhoneNumber != null) {
                insertIntoPhoneNumber.close();
            }

            if(insertIntoAddresses != null) {
                insertIntoAddresses.close();
            }

            if (insertIntoContacts != null) {
                insertIntoContacts.close();
            }

            if(queryPhoneNumber != null) {
                queryPhoneNumber.close();
            }

            if(queryAddresses != null) {
                queryAddresses.close();
            }

            if (queryContacts != null) {
                queryContacts.close();
            }

            if(updateContactsName != null) {
                updateContactsName.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Contact> queryEveryContact(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_CONTACTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_CONTACTS_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Contact> contacts = new ArrayList<>();
            while (results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getInt(1));
                contact.setName(results.getString(2));
                contact.setAddress(results.getString(3));
                contact.setPhoneNumber(results.getInt(4));
                contacts.add(contact);
            }

            return contacts;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Contact> queryContact(String name, String address, int phoneNumber) {
        try {
            queryPhoneBook.setString(1, name);
            queryPhoneBook.setString(2, address);
            queryPhoneBook.setInt(3, phoneNumber);
            ResultSet results = queryPhoneBook.executeQuery();

            List<Contact> contacts = new ArrayList<>();
            while(results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getInt(1));
                contact.setName(results.getString(2));
                contact.setAddress(results.getString(3));
                contact.setPhoneNumber(results.getInt(4));
                contacts.add(contact);
            }

            return contacts;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Contact> queryContact(String name, String address) {
        try {
            queryPhoneBook.setString(1, name);
            queryPhoneBook.setString(2, address);
            queryPhoneBook.setInt(3, -1);
            ResultSet results = queryPhoneBook.executeQuery();

            List<Contact> contacts = new ArrayList<>();
            while(results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getInt(1));
                contact.setName(results.getString(2));
                contact.setAddress(results.getString(3));
                contact.setPhoneNumber(results.getInt(4));
                contacts.add(contact);
            }

            return contacts;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private int insertPhoneNumber(int number) throws SQLException {

        queryPhoneNumber.setInt(1, number);
        ResultSet results = queryPhoneNumber.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist
            insertIntoPhoneNumber.setInt(1, number);
            int affectedRows = insertIntoPhoneNumber.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert phone number!");
            }

            ResultSet generatedKeys = insertIntoPhoneNumber.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for phone number");
            }
        }
    }

    private void insertAddress(String name, int phoneNumberId) throws SQLException {

        queryAddresses.setString(1, name);
        ResultSet results = queryAddresses.executeQuery();
        if(results.next()) {
            return;
        } else {
            // Insert the album
            insertIntoAddresses.setString(1, name);
            insertIntoAddresses.setInt(2, phoneNumberId);
            int affectedRows = insertIntoAddresses.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert address!");
            }

            ResultSet generatedKeys = insertIntoAddresses.getGeneratedKeys();
            if(generatedKeys.next()) {
                return;
            } else {
                throw new SQLException("Couldn't get _id for address");
            }
        }
    }

    private int insertContact(int phoneNumber, String address, String name) throws SQLException {

        queryContacts.setString(1, name);
        queryContacts.setString(2, address);
        queryContacts.setInt(3, phoneNumber);
        ResultSet results = queryContacts.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the album
            insertIntoContacts.setString(1, name);
            insertIntoContacts.setString(2, address);
            insertIntoContacts.setInt(3, phoneNumber);
            int affectedRows = insertIntoContacts.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert PhoneNumber!");
            }

            ResultSet generatedKeys = insertIntoContacts.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for PhoneNumber");
            }
        }
    }

    public void insertContactIntoPhonebook(String name, String address, int phoneNumber) {

        try {
            conn.setAutoCommit(false);

            int phoneNumberId = insertPhoneNumber(phoneNumber);
            insertAddress(address, phoneNumberId);
            int contactId = insertContact(phoneNumber, address, name);

            if(contactId > 0) {
                conn.commit();
            } else {
                throw new SQLException("The PhoneNumber insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert contact exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    private boolean updateContactsName(int id, String newName, String newAddress, int newPhoneNumber) throws SQLException {
        try {
            updateContactsName.setString(1, newName);
            updateContactsName.setString(2, newAddress);
            updateContactsName.setInt(3, newPhoneNumber);
            updateContactsName.setInt(4, id);
            int affectedRecords = updateContactsName.executeUpdate();

            return affectedRecords == 1;

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateContactsAddress(String oldAddress, String newAddress) throws SQLException {
        try {
            if (newAddress.isEmpty()){
                updateContactsAddress.setString(1, oldAddress);
                updateContactsAddress.setString(2, oldAddress);
                int affectedRecords = updateContactsAddress.executeUpdate();

                return affectedRecords == 1;
            } else {
                updateContactsAddress.setString(1, newAddress);
                updateContactsAddress.setString(2, oldAddress);
                int affectedRecords = updateContactsAddress.executeUpdate();

                return affectedRecords == 1;
            }

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateContactsNumber(int oldPhoneNumber, int newPhoneNumber) throws SQLException {
        try {
            if (newPhoneNumber > 0 ){
                updateContactsNumber.setInt(1, newPhoneNumber);
                updateContactsNumber.setInt(2, oldPhoneNumber);
                int affectedRecords = updateContactsNumber.executeUpdate();

                return affectedRecords == 1;
            } else {
                updateContactsNumber.setInt(1, oldPhoneNumber);
                updateContactsNumber.setInt(2, oldPhoneNumber);
                int affectedRecords = updateContactsNumber.executeUpdate();

                return affectedRecords == 1;
            }

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public void updateTables(int id , String oldAddress, int oldPhoneNumber, String newName, String newAddress, int newPhoneNumber) {
        try {
            conn.setAutoCommit(false);

            if (updateContactsName(id, newName, newAddress, newPhoneNumber)
                && updateContactsAddress(oldAddress, newAddress)
                && updateContactsNumber(oldPhoneNumber, newPhoneNumber)) {
                conn.commit();
            } else {
                throw new SQLException("The contact failed to update");
            }

            conn.commit();

        } catch(Exception e) {
            System.out.println("Updating contact exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    private void deleteContact(String name, String address) throws SQLException {
        try {
            deleteContact.setString(1, name);
            deleteContact.setString(2, address);
            deleteContact.execute();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    private void deleteAddress(String address) throws SQLException {
        try {
            deleteAddress.setString(1, address);
            deleteAddress.execute();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    private void deletePhoneNumber(int phoneNumber) throws SQLException {
        try {
            deletePhoneNumber.setInt(1, phoneNumber);
            deletePhoneNumber.execute();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    public void deleteContacts(String name, String address, int phoneNumber) {
        try {
            conn.setAutoCommit(false);

            deleteContact(name, address);
            deleteAddress(address);
            deletePhoneNumber(phoneNumber);

            conn.commit();

        } catch(Exception e) {
            System.out.println("Delete contact exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }
}















