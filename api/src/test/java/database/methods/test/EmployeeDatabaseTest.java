package database.methods.test;

import controllers.Application;
import database.methods.EmployeeDatabase;
import entities.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.SQLException;

/**
 * Note: after each test or test method, the database is reset, i.e.
 * the changes to the database during the tests are abolished so that
 * the tests always run successfully.
 */
public class EmployeeDatabaseTest {
    Application application;
    EmployeeDatabase employeeDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        employeeDatabase = new EmployeeDatabase(application.connectionSource);
//        Assert.assertEquals(4,employeeDatabase.getAllEmployees().size());

    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the size of the list has
     * increased by 1 and whether the data of the added employee is stored in the
     * correct columns.
     *
     * @throws SQLException if the employee has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Employee kevin = new Employee( "Assil", "Tunis", "CSS", "6");
        employeeDatabase.addToDatabase(kevin);
        Assert.assertEquals(6, employeeDatabase.getAllEmployees().size());
        Assert.assertEquals("CSS", employeeDatabase.getEmployee(6).getCompetences());
        Assert.assertEquals("6", employeeDatabase.getEmployee(6).getProjectIDs());
        Assert.assertEquals("Assil", employeeDatabase.getEmployee(6).getName());
        Assert.assertEquals("Tunis", employeeDatabase.getEmployee(6).getDomicile());
        employeeDatabase.deleteFromDatabase(6);
    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyEmployeeDataTest() {
        Employee mohamed = employeeDatabase.getEmployee(2);
        assertEquals("Stuttgart", mohamed.getDomicile());
        assertTrue(mohamed.getCompetences().contains("JAVASCRIPT"));
        mohamed.setDomicile("Tunis");
        mohamed.addProject(5);
        mohamed.removeCompetence("JAVASCRIPT");
        employeeDatabase.modifyEmployeeData(mohamed);
        assertTrue(mohamed.getProjectIDs().contains("5"));
        assertNotEquals("Stuttgart", mohamed.getDomicile());
        assertEquals("Tunis", mohamed.getDomicile());
        assertFalse(mohamed.getCompetences().contains("JAVASCRIPT"));
        mohamed.setDomicile("Stuttgart");
        mohamed.addCompetence("JAVASCRIPT");
        mohamed.removeProject(5);
    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether the size of the list has
     * decreased by 1.
     *
     * @throws SQLException if the employee has not been deleted
     */
    @Test
    public void deleteEmployeeDataTest() throws SQLException {
        assertEquals(5, employeeDatabase.getAllEmployees().size());
        employeeDatabase.deleteFromDatabase(2);
        assertEquals(4, employeeDatabase.getAllEmployees().size());
        employeeDatabase.addToDatabase(new Employee( "Mohamed", "Stuttgart", "JAVA-JAVASCRIPT", "3"));
    }

}
