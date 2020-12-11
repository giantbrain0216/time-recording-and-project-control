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
    ِ
    ClientDatabase clientDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        clientDatabase = new ClientDatabase(application.connectionSource);
    }

    @Test
    public void addToDatabaseTest() throws SQLException {
        Client clientToAdd = new Client(4, "AUDI", "audi@web.de", "1597536482", 2, "1");
        Assert.assertEquals(3, clientDatabase.getAllClients().size());
        clientDatabase.addToDatabase(clientToAdd);
        Assert.assertEquals(4, clientDatabase.getAllClients().size());
        clientDatabase.deleteFromDatabase(4);
    }

    @Test(expected = NullPointerException.class)
    public void addToDatabaseFailTest() throws SQLException {
        clientDatabase.addToDatabase(null);
    }

    @Test
    public void deleteClientFromDatabaseTest() {
        Assert.assertEquals(3, clientDatabase.getAllClients().size());
        clientDatabase.deleteFromDatabase(1);
        Assert.assertEquals(2, clientDatabase.getAllClients().size());
        //TODO : Client with ID 1 must be added to the database.
    }

    @Test(expected = SQLException.class)
    public void deleteClientFromDatabaseFailTest() {
        clientDatabase.deleteFromDatabase(140);
    }

    @Test
    public void modifyClientDataTest(){
        Client amazon = clientDatabase.getClient(1);
        Assert.assertEquals("amazon@web.de",amazon.getEmail());
        Assert.assertEquals("",amazon.getTelephoneNumber());
        Assert.assertEquals(1,amazon.getIDContactPerson());
        amazon.setContactPerson(2);
        amazon.setEmail("amazon2020@web.de");
        amazon.setTelephoneNumber("789987789");
        clientDatabase.modifyClientData(amazon);
        amazon = clientDatabase.getClient(1);
        Assert.assertEquals("amazon2020@web.de",amazon.getEmail());
        Assert.assertEquals("789987789",amazon.getTelephoneNumber());
        Assert.assertEquals(2,amazon.getIDContactPerson());
        amazon.setContactPerson(1);
        amazon.setEmail("amazon@web.de");
        amazon.setTelephoneNumber("");

    }
}
