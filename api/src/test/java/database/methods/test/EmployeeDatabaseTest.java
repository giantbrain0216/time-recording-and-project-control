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
    }

    /**
     * This method tests whether an object can be successfully added in the
     * database by checking after the insertion whether the employee
     * has been added to the list of all employees whether the data of the added
     * employee is stored in the correct columns.
     *
     * @throws SQLException if the employee has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Employee employeeToAdd = new Employee("Assil", "Tunis", "CSS", "6");
        employeeDatabase.addToDatabase(employeeToAdd);
        Assert.assertEquals("CSS", employeeDatabase.getEmployee(employeeToAdd.getEmployeeID()).getCompetences());
        Assert.assertEquals("6", employeeDatabase.getEmployee(employeeToAdd.getEmployeeID()).getProjectIDs());
        Assert.assertEquals("Assil", employeeDatabase.getEmployee(employeeToAdd.getEmployeeID()).getName());
        Assert.assertEquals("Tunis", employeeDatabase.getEmployee(employeeToAdd.getEmployeeID()).getDomicile());
//        Assert.assertTrue(employeeDatabase.getAllEmployees().contains(employeeToAdd));
        employeeDatabase.deleteFromDatabase(employeeToAdd.getEmployeeID());
    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyEmployeeDataTest() throws SQLException {
        Employee employeeToModify = new Employee("Test", "Tunis", "HTML", "6");
        employeeDatabase.addToDatabase(employeeToModify);
        assertEquals("Tunis", employeeToModify.getDomicile());
        assertTrue(employeeToModify.getCompetences().contains("HTML"));
        employeeToModify.setDomicile("Stuttgart");
        employeeToModify.addProject(5);
        employeeToModify.addCompetence("JAVASCRIPT");
        employeeDatabase.modifyEmployeeData(employeeToModify);
        assertTrue(employeeToModify.getProjectIDs().contains("5"));
        assertEquals("Stuttgart", employeeToModify.getDomicile());
        assertNotEquals("Tunis", employeeToModify.getDomicile());
        assertTrue(employeeToModify.getCompetences().contains("JAVASCRIPT"));
        employeeDatabase.deleteFromDatabase(employeeToModify.getEmployeeID());
    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether the employee
     * exists in the database 
     *
     * @throws SQLException if the employee has not been deleted
     */
    @Test
    public void deleteEmployeeDataTest() throws SQLException {
        Employee employeeToDelete = new Employee("Test", "Tunis", "HTML", "6");
        employeeDatabase.addToDatabase(employeeToDelete);
        assertTrue(employeeDatabase.getAllEmployees().contains(employeeToDelete));
        employeeDatabase.deleteFromDatabase(employeeToDelete.getEmployeeID());
        assertFalse(employeeDatabase.getAllEmployees().contains(employeeToDelete));

    }

}
