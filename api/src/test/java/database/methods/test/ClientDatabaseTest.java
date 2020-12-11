package database.methods.test;

import controllers.Application;
import database.methods.ClientDatabase;
import entities.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class ClientDatabaseTest {
    Application application;
    ClientDatabase clientDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        clientDatabase = new ClientDatabase(application.connectionSource);
    }

    @Test
    public void addToDatabaseTest() throws SQLException {
        Client clientToAdd = new Client( "AUDI", "audi@web.de", "1597536482", 2, "1");
        Assert.assertEquals(3, clientDatabase.getAllClients().size());
        clientDatabase.addToDatabase(clientToAdd);
        Assert.assertEquals(4, clientDatabase.getAllClients().size());
        clientDatabase.deleteFromDatabase(clientToAdd.getClientID());
    }

    @Test(expected = NullPointerException.class)
    public void addToDatabaseFailTest() throws SQLException {
        clientDatabase.addToDatabase(null);
    }

    @Test
    public void modifyClientDataTest() {
        Client amazon = clientDatabase.getClient(1);
        Assert.assertEquals("amazon@web.de", amazon.getEmail());
        Assert.assertEquals("12345515667", amazon.getTelephoneNumber());
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
}
