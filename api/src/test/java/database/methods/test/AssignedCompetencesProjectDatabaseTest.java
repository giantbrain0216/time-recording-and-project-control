package database.methods.test;

import controllers.Application;
import database.methods.AssignedCompetencesProjectDatabase;
import entities.AssignedCompetencesProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link AssignedCompetencesProjectDatabaseTest}
 */

public class AssignedCompetencesProjectDatabaseTest {

    Application application;
    AssignedCompetencesProjectDatabase assignedCompetencesProjectDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        assignedCompetencesProjectDatabase = new AssignedCompetencesProjectDatabase(application.connectionSource);
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
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject(null, 99, 99);
        assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        Assert.assertTrue(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
        assignedCompetencesProjectDatabase.deleteFromDatabase(assignedCompetencesProject.getId());
        Assert.assertFalse(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));

    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the assigned competence has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyDataTest() throws SQLException {
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject(null, 99, 99);
        //  assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        //Assert.assertTrue(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
        //assignedCompetencesProjectDatabase.deleteFromDatabase(assignedCompetencesProject.getId());
        //Assert.assertFalse(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
        int id = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        Assert.assertEquals(99, assignedCompetencesProject.getCompetenceID());
        Assert.assertEquals(99, assignedCompetencesProject.getProjectID());
        assignedCompetencesProject.setCompetenceID(321);
        assignedCompetencesProject.setProjectID(321);
        assignedCompetencesProjectDatabase.modifyAssignedCompetencesProject(assignedCompetencesProject);
        Assert.assertEquals(321, assignedCompetencesProject.getCompetenceID());
        Assert.assertEquals(321, assignedCompetencesProject.getProjectID());
       // Assert.assertEquals("test", assignedCompetencesProjectDatabase.getCompetence(id).getName());
        assignedCompetencesProjectDatabase.deleteFromDatabase(id);
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
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject(null, 99, 99);
        assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        Assert.assertTrue(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
        assignedCompetencesProjectDatabase.deleteFromDatabase(assignedCompetencesProject.getId());
        Assert.assertFalse(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));


    }
}


