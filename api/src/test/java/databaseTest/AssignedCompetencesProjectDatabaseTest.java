package databaseTest;

import de.unistuttgart.sopra.Application;
import de.unistuttgart.sopra.databse.AssignedCompetencesProjectDatabase;
import de.unistuttgart.sopra.entities.AssignedCompetencesProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject( 99, 99);
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
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject( 99, 99);
        int id = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        Assert.assertEquals(99, assignedCompetencesProject.getCompetenceID());
        Assert.assertEquals(99, assignedCompetencesProject.getProjectID());
        assignedCompetencesProject.setCompetenceID(321);
        assignedCompetencesProject.setProjectID(321);
        assignedCompetencesProjectDatabase.modifyAssignedCompetencesProject(assignedCompetencesProject);
        Assert.assertEquals(321, assignedCompetencesProject.getCompetenceID());
        Assert.assertEquals(321, assignedCompetencesProject.getProjectID());
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
        AssignedCompetencesProject assignedCompetencesProject = new AssignedCompetencesProject( 99, 99);
        assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject);
        Assert.assertTrue(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
        assignedCompetencesProjectDatabase.deleteFromDatabase(assignedCompetencesProject.getId());
        Assert.assertFalse(assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject().stream().anyMatch(object -> object.getId() == (assignedCompetencesProject.getId())));
    }

    @Test
    public void getCompetencesTest() throws SQLException {
        AssignedCompetencesProject assignedCompetencesProject1 = new AssignedCompetencesProject( 99, 1);
        AssignedCompetencesProject assignedCompetencesProject2 = new AssignedCompetencesProject( 99, 2);
        AssignedCompetencesProject assignedCompetencesProject3 = new AssignedCompetencesProject( 99, 3);
        AssignedCompetencesProject assignedCompetencesProject4 = new AssignedCompetencesProject( 99, 4);
        int id1 = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject1);
        int id2 = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject2);
        int id3 = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject3);
        int id4 = assignedCompetencesProjectDatabase.addToDatabase(assignedCompetencesProject4);
        List<Integer> listOfCompetences = assignedCompetencesProjectDatabase.getCompetences(99);
        Assert.assertEquals(4, listOfCompetences.size());
        Collections.sort(listOfCompetences);
        Assert.assertTrue(listOfCompetences.contains(1) && listOfCompetences.contains(2) && listOfCompetences.contains(3) && listOfCompetences.contains(4));
        assignedCompetencesProjectDatabase.deleteFromDatabase(id1);
        assignedCompetencesProjectDatabase.deleteFromDatabase(id2);
        assignedCompetencesProjectDatabase.deleteFromDatabase(id3);
        assignedCompetencesProjectDatabase.deleteFromDatabase(id4);
    }
}


