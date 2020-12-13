package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import controllers.Application;
import entities.Employee;
import entities.Project;
import entities.TimeRegistration;
import entities.Client;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeRegistrationDatabase {
    private final Dao<TimeRegistration, Integer> timeRegistrationDAO;

    public TimeRegistrationDatabase(final ConnectionSource connectionSource) throws SQLException {
        timeRegistrationDAO = DaoManager.createDao(connectionSource, TimeRegistration.class);
        TableUtils.createTableIfNotExists(connectionSource, TimeRegistration.class);
    }

    /**
     * Adds the given time registration to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same number
     * as the given project's
     * Postconditions : The time registration has been added to the database.      *
     *
     * @param timeRegistrationToAdd to be added to the database
     */

    /**
     * Creates IDs in such a way that after deleting a TimeRegistratio, for example,
     * there are no gaps, but the ID whose TimeRegistratio was deleted is assigned
     * to another TimeRegistratio to be added.
     *
     * @return ID of the TimeRegistratio to be added.
     */
    private int createID() {
        List<TimeRegistration> listOfTimeRegistration = this.getAllTimeRegistrations();
        //  sort the list by ID
        Collections.sort(listOfTimeRegistration, Comparator.comparing(TimeRegistration::getID));
        for (int i = 0; i < listOfTimeRegistration.size(); i++) {
            if (listOfTimeRegistration.get(i).getID() != i + 1) {
                return i + 1;
            }
        }
        return listOfTimeRegistration.size() + 1;
    }

    public void addToDataBase(final TimeRegistration timeRegistrationToAdd) {
        try {
            timeRegistrationToAdd.setID(createID());
            timeRegistrationDAO.create(timeRegistrationToAdd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * deletes an existing time registration from the table in the database.
     * An error message will be printed in case there is no time registration
     * with the given number.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     * Preconditions : The time registration exists in the database
     * Post conditions : The time registration does not in the database
     *
     * @param timeRegistrationID to be deleted from the database
     */
    public void deleteFromDatabase(Integer timeRegistrationID) {
        try {
            timeRegistrationDAO.deleteById(timeRegistrationID);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is no time registration with the ID " + timeRegistrationID);
        }
    }

    /**
     * modifies the data of an already existing project in the table in the
     * database.
     *
     * @param timeRegistrationWithNewData with the modified and updated data
     */
    public void modifyTimeRegistrationData(final TimeRegistration timeRegistrationWithNewData) {
        if (timeRegistrationWithNewData == null) {
            throw new NullPointerException("Please enter a valid project");
        }
        try {
            timeRegistrationDAO.update(timeRegistrationWithNewData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");
            e.printStackTrace();
        }
    }

    /**
     * searches the database and then returns the time registration whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param timeRegistrationID of the employee to return
     * @return project whose number matches the given one
     */
    public TimeRegistration getTimeRegistration(Integer timeRegistrationID) {
        try {
            return timeRegistrationDAO.queryForId(timeRegistrationID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return all time registration stored in the database
     *
     * @return a list or an arraylist containing all employees. When the database
     * is empty, an empty arrayList will be returned.
     * <p>
     * ARRAY OR LIST ?
     * Hint : Query for all of the items in the object table. For medium sized or large tables, this may load a lot
     * of objects into memory so you should consider using the iterator() method instead.
     */
    public List<TimeRegistration> getAllTimeRegistrations() {
        try {
            return timeRegistrationDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }
}
