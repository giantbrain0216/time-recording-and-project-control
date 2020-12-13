package database.methods.test;

import controllers.Application;
import database.methods.ClientDatabase;
import entities.Client;
import entities.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Note: after each test or test method, the database is reset, i.e.
 * the changes to the database during the tests are abolished so that
 * the tests always run successfully.
 */
public class ClientDatabaseTest {
    Application application;
    ClientDatabase clientDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        clientDatabase = new ClientDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the size of the list has
     * increased by 1 and whether the data of the added client is stored in the
     * correct columns.
     *
     * @throws SQLException if the client has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Client clientToAdd = new Client("AUDI", "audi@web.de", "1597536482", 2, "1");
        Assert.assertEquals(3, clientDatabase.getAllClients().size());
        clientDatabase.addToDatabase(clientToAdd);
        Assert.assertEquals(4, clientDatabase.getAllClients().size());
        clientDatabase.deleteFromDatabase(clientToAdd.getClientID());
    }

    @Test(expected = NullPointerException.class)
    public void addToDatabaseFailTest() throws SQLException {
        clientDatabase.addToDatabase(null);
    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyClientDataTest() {
        Client amazon = clientDatabase.getClient(1);
        Assert.assertEquals("amazon@web.de", amazon.getEmail());
        Assert.assertEquals("1234567", amazon.getTelephoneNumber());
        Assert.assertEquals(1, amazon.getIDContactPerson());
        amazon.setContactPerson(2);
        amazon.addProject(5);
        amazon.setEmail("amazon2020@web.de");
        amazon.setTelephoneNumber("789987789");
        clientDatabase.modifyClientData(amazon);
        // amazon = clientDatabase.getClient(1);
        Assert.assertEquals("amazon2020@web.de", amazon.getEmail());
        Assert.assertEquals("789987789", amazon.getTelephoneNumber());
        Assert.assertEquals(2, amazon.getIDContactPerson());
        Assert.assertTrue(amazon.getProjectIDs().contains("5"));

        // Reset all data as it was before the test
        amazon.setContactPerson(1);
        amazon.deleteProject(5);
        amazon.setEmail("amazon@web.de");
        amazon.setTelephoneNumber("1234567");
        clientDatabase.modifyClientData(amazon);
    }
    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether the size of the list has
     * decreased by 1.
     *
     * @throws SQLException if the client has not been deleted
     */
    @Test
    public void deleteFromDatabaseTest() throws SQLException {
        assertEquals(3, clientDatabase.getAllClients().size());
        clientDatabase.deleteFromDatabase(2);
        assertEquals(2, clientDatabase.getAllClients().size());
        clientDatabase.addToDatabase(new Client("Teams", "teams@web.de", "7654321", 2,"8"));
    }
}
