package databaseTest;


import de.unistuttgart.sopra.Application;
import de.unistuttgart.sopra.databse.TimeRegistrationDatabase;

import de.unistuttgart.sopra.entities.TimeRegistration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link TimeRegistrationDatabase}
 */
public class TimeRegistrationDatabaseTest {
    Application application;
    TimeRegistrationDatabase timeRegistrationDatabase;


    @Before
    public void setUp() throws SQLException {
        application = new Application();
        timeRegistrationDatabase = new TimeRegistrationDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the  id of the added
     * TimeRegistration is in the list of all existing TimeRegistrations
     *
     * @throws SQLException if the TimeRegistration has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        TimeRegistration timeRegistration = new TimeRegistration(1, 2,
              new Date(2002-04-23), new Date(2002-04-23),"I fixed one bug");
        timeRegistrationDatabase.addToDataBase(timeRegistration);
        Assert.assertTrue(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(timeRegistration.getID())));
        timeRegistrationDatabase.deleteFromDatabase(timeRegistration.getID());
        Assert.assertFalse(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(timeRegistration.getID())));

    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether its id does not belong
     * to he ids of the existing TimeRegistrations
     *
     * @throws SQLException if the TimeRegistration has not been deleted
     */
    @Test
    public void deleteTimeRegistrationTest() throws SQLException {
        TimeRegistration timeRegistration = new TimeRegistration(1, 2,
                new Date(2002-04-23), new Date(2002-04-23),"I fixed one bug");
        timeRegistrationDatabase.addToDataBase(timeRegistration);
        Assert.assertTrue(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(timeRegistration.getID())));
        timeRegistrationDatabase.deleteFromDatabase(timeRegistration.getID());
        Assert.assertFalse(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(timeRegistration.getID())));
    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyTimeRegistrationDataTest() throws SQLException {
        TimeRegistration timeRegistration = new TimeRegistration(1, 2,
                new Date(2002-04-23), new Date(2002-04-23),"I fixed one bug");
        timeRegistrationDatabase.addToDataBase(timeRegistration);
        TimeRegistration finalTimeRegistration = timeRegistration;
        Assert.assertTrue(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(finalTimeRegistration.getID())));
        Assert.assertEquals(1, timeRegistration.getEmployeeID());
       timeRegistration.setEmployeeID(2);
       timeRegistrationDatabase.modifyTimeRegistrationData(timeRegistration);
       timeRegistration = timeRegistrationDatabase.getTimeRegistration(timeRegistration.getID());
        Assert.assertNotEquals(1, timeRegistration.getEmployeeID());
        Assert.assertEquals(2, timeRegistration.getEmployeeID());
        timeRegistrationDatabase.deleteFromDatabase(timeRegistration.getID());
        TimeRegistration finalTimeRegistration1 = timeRegistration;
        Assert.assertFalse(timeRegistrationDatabase.getAllTimeRegistrations().stream().
                anyMatch(timeRegistration1 -> timeRegistration1.getID()==(finalTimeRegistration1.getID())));



    }
}
