package database.methods.test;

import controllers.Application;
import entities.AssignedProjectsClient;
import org.junit.Assert;
import database.methods.AssignedProjectsClientDatabase;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link AssignedProjectsClientDatabase}
 */

public class AssignedProjectsClientDatabaseTest {

    Application application;
    AssignedProjectsClientDatabase assignedProjectsClientDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        assignedProjectsClientDatabase = new AssignedProjectsClientDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the ID of the assignment of
     * the competence to the project  occurs in the list of IDs of all  assigned competences
     *
     * @throws SQLException if the competence has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        AssignedProjectsClient assignedProjectsClient = new AssignedProjectsClient(99, 99);
        assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient);
        Assert.assertTrue(assignedProjectsClientDatabase.getAllAssignedProjectsClient().stream().anyMatch(object -> object.getId() == (assignedProjectsClient.getId())));
        assignedProjectsClientDatabase.deleteFromDatabase(assignedProjectsClient.getId());
        Assert.assertFalse(assignedProjectsClientDatabase.getAllAssignedProjectsClient().stream().anyMatch(object -> object.getId() == (assignedProjectsClient.getId())));

    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the assigned competence has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyDataTest() throws SQLException {
        AssignedProjectsClient assignedProjectsClient = new AssignedProjectsClient(99, 99);
        int id = assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient);
        Assert.assertEquals(99, assignedProjectsClient.getClientID());
        Assert.assertEquals(99, assignedProjectsClient.getProjectID());
        assignedProjectsClient.setClientID(321);
        assignedProjectsClient.setProjectID(321);
        assignedProjectsClientDatabase.modifyAssignedProjectsClient(assignedProjectsClient);
        Assert.assertEquals(321, assignedProjectsClient.getProjectID());
        Assert.assertEquals(321, assignedProjectsClient.getClientID());
        assignedProjectsClientDatabase.deleteFromDatabase(id);
    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion the deleted element in the list
     * of all assigned competences still exists.
     *
     * @throws SQLException if the competence has not been deleted
     */
    @Test
    public void deleteFromDatabaseTest() throws SQLException {
        AssignedProjectsClient assignedProjectsClient = new AssignedProjectsClient(99, 99);
        assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient);
        Assert.assertTrue(assignedProjectsClientDatabase.getAllAssignedProjectsClient().stream().anyMatch(object -> object.getId().equals(assignedProjectsClient.getId())));
        assignedProjectsClientDatabase.deleteFromDatabase(assignedProjectsClient.getId());
        Assert.assertFalse(assignedProjectsClientDatabase.getAllAssignedProjectsClient().stream().anyMatch(object -> object.getId().equals(assignedProjectsClient.getId())));
    }

    @Test
    public void getProjectIDsTest() throws SQLException {
        AssignedProjectsClient assignedProjectsClient1 = new AssignedProjectsClient(99, 1);
        AssignedProjectsClient assignedProjectsClient2 = new AssignedProjectsClient(99, 2);
        AssignedProjectsClient assignedProjectsClient3 = new AssignedProjectsClient(99, 3);
        AssignedProjectsClient assignedProjectsClient4 = new AssignedProjectsClient(99, 4);
        int id1 = assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient1);
        int id2 = assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient2);
        int id3 = assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient3);
        int id4 = assignedProjectsClientDatabase.addToDatabase(assignedProjectsClient4);
        List<Integer> listOfCompetences = assignedProjectsClientDatabase.getProjectIDs(99);
        Assert.assertEquals(4, listOfCompetences.size());
        Collections.sort(listOfCompetences);
        Assert.assertTrue(listOfCompetences.contains(1) && listOfCompetences.contains(2) && listOfCompetences.contains(3) && listOfCompetences.contains(4));
        assignedProjectsClientDatabase.deleteFromDatabase(id1);
        assignedProjectsClientDatabase.deleteFromDatabase(id2);
        assignedProjectsClientDatabase.deleteFromDatabase(id3);
        assignedProjectsClientDatabase.deleteFromDatabase(id4);
    }
}


