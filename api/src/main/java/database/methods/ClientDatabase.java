package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import entities.Client;
import entities.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to manipulate and manage the database of clients
 *
 * @version 06.12.2020
 */
public class ClientDatabase {
    private Dao<Client, Integer> clientDAO;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of clients in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the element cannot be created.
     */
    public ClientDatabase(ConnectionSource connectionSource) throws SQLException {
        clientDAO = DaoManager.createDao(connectionSource, Client.class);
        TableUtils.createTableIfNotExists(connectionSource, Client.class);
    }

    /**
     * Adds the given client to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same ID
     * as the given client's
     * Postconditions : The client has been added to the database. Otherwise
     * an exception will be thrown in the case another client with the same ID
     * occurs in the table.
     *
     * @param clientToAdd must not be null and his ID must be unique
     * @return ID of the client that has been added for testing purposes
     * @throws SQLException if the client cannot be added.
     */
    public int addToDatabase(Client clientToAdd) throws SQLException {
        if (clientToAdd == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        int clientToAddID = createID();
        clientToAdd.setID(clientToAddID);
        clientDAO.create(clientToAdd);
        return clientToAddID;
    }

    /**
     * Creates IDs in such a way that after deleting a client, for example,
     * there are no gaps, but the ID whose client was deleted is assigned
     * to another client to be added.
     *
     * @return ID of the client to be added.
     */
    private int createID() {
        List<Client> listOfClients = this.getAllClients();
        //  sort the list by ID
        Collections.sort(listOfClients, Comparator.comparing(Client::getClientID));
        for (int i = 0; i < listOfClients.size(); i++) {
            if (listOfClients.get(i).getClientID() != i + 1) {
                return i + 1;
            }
        }
        return listOfClients.size() + 1;
    }

    /**
     * deletes an existing client from the table in the database.
     * An error message will be printed in case there is no client
     * with the given ID.
     *
     * @param clientID of the employee which is unique
     */
    public void deleteFromDatabase(Integer clientID) {
        if (clientID == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        try {
            clientDAO.deleteById(clientID);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is no client with the ID " + clientID);
        }
    }

    /**
     * modifies the data of an already existing employee in the table in the
     * database.
     * <p>
     * WHAT IF THE ID OF THE EMPLOYEE CHANGED AND THE NEW ONE IS LOCATED
     * FOR ANOTHER ONE?
     *
     * @param clientWithNewData with the modified and updated data
     */
    public void modifyClientData(final Client clientWithNewData) {
        if (clientWithNewData == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        try {
            clientDAO.update(clientWithNewData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");

        }
    }

    /**
     * searches the database and then returns the client whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped and null will be returned
     *
     * @param clientID of the client to return
     * @return client with the ID
     */
    public Client getClient(Integer clientID) {
        try {
            return clientDAO.queryForId(clientID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return all clients stored in the database
     *
     * @return a list or an arraylist containing all employees. When the database
     * is empty, an empty arrayList will be returned.
     * <p>
     * ARRAY OR LIST ?
     * Hint : Query for all of the items in the object table. For medium sized or large tables, this may load a lot
     * of objects into memory so you should consider using the iterator() method instead.
     */
    public List<Client> getAllClients() {
        try {
            return clientDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }


}
