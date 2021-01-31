package databaseTest;

import de.unistuttgart.sopra.Application;
import de.unistuttgart.sopra.databse.AssignedCompetencesEmployeeDatabase;
import de.unistuttgart.sopra.entities.AssignedCompetencesEmployee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link AssignedCompetencesEmployeeDatabase}
 */

public class AssignedCompetencesEmployeeDatabaseTest {


    Application application;
    AssignedCompetencesEmployeeDatabase assignedCompetencesEmployeeDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        assignedCompetencesEmployeeDatabase = new AssignedCompetencesEmployeeDatabase(application.connectionSource);
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
        AssignedCompetencesEmployee assignedCompetencesEmployee = new AssignedCompetencesEmployee( 99, 99);
        assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee);
        Assert.assertTrue(assignedCompetencesEmployeeDatabase.getAllAssignedCompetencesEmployee().stream().anyMatch(object -> object.getId() == (assignedCompetencesEmployee.getId())));
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(assignedCompetencesEmployee.getId());
        Assert.assertFalse(assignedCompetencesEmployeeDatabase.getAllAssignedCompetencesEmployee().stream().anyMatch(object -> object.getId() == (assignedCompetencesEmployee.getId())));

    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the assigned competence has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyDataTest() throws SQLException {
        AssignedCompetencesEmployee assignedCompetencesEmployee = new AssignedCompetencesEmployee( 99, 99);
        int id = assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee);
        Assert.assertEquals(99, assignedCompetencesEmployee.getCompetenceID());
        Assert.assertEquals(99, assignedCompetencesEmployee.getEmployeeID());
        assignedCompetencesEmployee.setCompetenceID(321);
        assignedCompetencesEmployee.setEmployeeID(321);
        assignedCompetencesEmployeeDatabase.modifyAssignedCompetencesEmployee(assignedCompetencesEmployee);
        Assert.assertEquals(321, assignedCompetencesEmployee.getCompetenceID());
        Assert.assertEquals(321, assignedCompetencesEmployee.getEmployeeID());
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(id);
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
        AssignedCompetencesEmployee assignedCompetencesEmployee = new AssignedCompetencesEmployee( 99, 99);
        assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee);
        Assert.assertTrue(assignedCompetencesEmployeeDatabase.getAllAssignedCompetencesEmployee().stream().anyMatch(object -> object.getId() == (assignedCompetencesEmployee.getId())));
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(assignedCompetencesEmployee.getId());
        Assert.assertFalse(assignedCompetencesEmployeeDatabase.getAllAssignedCompetencesEmployee().stream().anyMatch(object -> object.getId() == (assignedCompetencesEmployee.getId())));
    }

    @Test
    public void getCompetencesTest() throws SQLException {
        AssignedCompetencesEmployee assignedCompetencesEmployee1 = new AssignedCompetencesEmployee( 99, 1);
        AssignedCompetencesEmployee assignedCompetencesEmployee2 = new AssignedCompetencesEmployee( 99, 2);
        AssignedCompetencesEmployee assignedCompetencesEmployee3 = new AssignedCompetencesEmployee( 99, 3);
        AssignedCompetencesEmployee assignedCompetencesEmployee4 = new AssignedCompetencesEmployee( 99, 4);
        int id1 = assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee1);
        int id2 = assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee2);
        int id3 = assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee3);
        int id4 = assignedCompetencesEmployeeDatabase.addToDatabase(assignedCompetencesEmployee4);
        List<Integer> listOfCompetences = assignedCompetencesEmployeeDatabase.getCompetences(99);
        Assert.assertEquals(4, listOfCompetences.size());
        Collections.sort(listOfCompetences);
        Assert.assertTrue(listOfCompetences.contains(1) && listOfCompetences.contains(2) && listOfCompetences.contains(3) && listOfCompetences.contains(4));
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(id1);
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(id2);
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(id3);
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(id4);
    }
}


