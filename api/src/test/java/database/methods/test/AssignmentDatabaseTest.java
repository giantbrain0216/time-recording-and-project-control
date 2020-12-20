package database.methods.test;

import controllers.Application;
import database.methods.AssignmentDatabase;
import entities.Assignment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link AssignmentDatabase}
 */
public class AssignmentDatabaseTest {
    Application application;
    AssignmentDatabase assignmentDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        assignmentDatabase = new AssignmentDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully added in the
     * database by checking after the insertion whether the Assignment
     * has been added to the list of all Assignments whether the data of the added
     * Assignment is stored in the correct columns.
     *
     * @throws SQLException if the Assignment has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Assignment assignment = new Assignment(1, 1, 3);
        assignmentDatabase.addAssignment(assignment);
        Assert.assertTrue(assignmentDatabase.getAllAssignments().stream().
                anyMatch(assignment1 -> assignment1.getId().equals(assignment.getId())));
        assignmentDatabase.deleteAssignment(assignment.getId());
        Assert.assertFalse(assignmentDatabase.getAllAssignments().stream().
                anyMatch(assignment1 -> assignment1.getId().equals(assignment.getId())));

    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the Assignment has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyAssignmentDataTest() throws SQLException {
        Assignment assignment = new Assignment(1, 1, 3);
        assignmentDatabase.addAssignment(assignment);
        Assert.assertEquals(3,assignment.getPlannedWorkingHours());
        assignment.setPlannedWorkingHours(9);
        assignmentDatabase.modifyAssignmentData(assignment);
        Assert.assertNotEquals(3,assignmentDatabase.getAssignment(assignment.getId()).getPlannedWorkingHours());
        Assert.assertEquals(9,assignmentDatabase.getAssignment(assignment.getId()).getPlannedWorkingHours());
        assignmentDatabase.deleteAssignment(assignment.getId());
    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether the Assignment
     * exists in the database
     *
     * @throws SQLException if the Assignment has not been deleted
     */
    @Test
    public void deleteAssignmentDataTest() throws SQLException {
        Assignment assignment = new Assignment(1, 1, 3);
        assignmentDatabase.addAssignment(assignment);
        Assert.assertTrue(assignmentDatabase.getAllAssignments().stream().
                anyMatch(assignment1 -> assignment1.getId().equals(assignment.getId())));
        assignmentDatabase.deleteAssignment(assignment.getId());
        Assert.assertFalse(assignmentDatabase.getAllAssignments().stream().
                anyMatch(assignment1 -> assignment1.getId().equals(assignment.getId())));
    }

    @Test
    public void getAssignmentsByEmployeeTest() throws SQLException {
        Assignment assignment = new Assignment(999, 1, 3);
        Assignment assignment1 = new Assignment(999, 2, 3);
        Assignment assignment2 = new Assignment(999, 3, 3);
        Assignment assignment3 = new Assignment(999, 4, 3);
        Assignment assignment4 = new Assignment(999, 5, 3);
        assignmentDatabase.addAssignment(assignment);
        assignmentDatabase.addAssignment(assignment1);
        assignmentDatabase.addAssignment(assignment2);
        assignmentDatabase.addAssignment(assignment3);
        assignmentDatabase.addAssignment(assignment4);
        Assert.assertEquals(5,assignmentDatabase.getAssignmentsByEmployee(999).size());
        assignmentDatabase.deleteAssignment(assignment.getId());
        assignmentDatabase.deleteAssignment(assignment1.getId());
        assignmentDatabase.deleteAssignment(assignment2.getId());
        assignmentDatabase.deleteAssignment(assignment3.getId());
        assignmentDatabase.deleteAssignment(assignment4.getId());

    }
    @Test
    public void getAssignmentsByProjectTest() throws SQLException {
        Assignment assignment = new Assignment(1, 999, 3);
        Assignment assignment1 = new Assignment(2, 999, 3);
        Assignment assignment2 = new Assignment(3, 999, 3);
        Assignment assignment3 = new Assignment(4, 999, 3);
        Assignment assignment4 = new Assignment(5, 999, 3);
        assignmentDatabase.addAssignment(assignment);
        assignmentDatabase.addAssignment(assignment1);
        assignmentDatabase.addAssignment(assignment2);
        assignmentDatabase.addAssignment(assignment3);
        assignmentDatabase.addAssignment(assignment4);
        Assert.assertEquals(5,assignmentDatabase.getAssignmentsByProject(999).size());
        assignmentDatabase.deleteAssignment(assignment.getId());
        assignmentDatabase.deleteAssignment(assignment1.getId());
        assignmentDatabase.deleteAssignment(assignment2.getId());
        assignmentDatabase.deleteAssignment(assignment3.getId());
        assignmentDatabase.deleteAssignment(assignment4.getId());

    }

}
