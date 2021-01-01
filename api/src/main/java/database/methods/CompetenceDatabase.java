package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import entities.Competence;
import entities.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to manage the database of the competences
 *
 * @version 01.01.2021
 */
public class CompetenceDatabase {
    private Dao<Competence, Integer> competenceDao;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of competences in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the dao element cannot be created.
     */
    public CompetenceDatabase(final ConnectionSource connectionSource) throws SQLException {
        competenceDao = DaoManager.createDao(connectionSource, Competence.class);
        TableUtils.createTableIfNotExists(connectionSource, Competence.class);
    }

    /**
     * Creates IDs in such a way that after deleting a competence, for example,
     * there are no gaps, but the ID whose competence was deleted is assigned
     * to another competence to be added.
     *
     * @return ID of the competence to be added.
     */
    private int createID() {
        List<Competence> listOfCompetences = this.getAllCompetences();
        //  sort the list by ID
        listOfCompetences.sort(Comparator.comparing(Competence::getId));
        for (int i = 0; i < listOfCompetences.size(); i++) {
            if (listOfCompetences.get(i).getId() != i + 1) {
                return i + 1;
            }
        }
        return listOfCompetences.size() + 1;
    }

    /**
     * Adds the given competence to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same ID
     * as the given employee's
     * Post conditions : The competence has been added to the database. Otherwise
     * an exception will be thrown in the case another employee with the same ID
     * occurs in the table.
     *
     * @param competence must not be null and its ID must be unique
     * @return ID of the competence that has been added for testing purposes
     * @throws SQLException if the competence cannot be added.
     */
    public int addToDatabase(Competence competence) throws SQLException {
        int employeeToAddID = createID();
        competence.setId(employeeToAddID);
        competenceDao.create(competence);
        return employeeToAddID;
    }

    /**
     * deletes an existing competence from the table in the database.
     * An error message will be printed in case there is no competence
     * with the given ID.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param competenceID of the competence which is unique
     */
    public void deleteFromDatabase(Integer competenceID) {
        try {
            competenceDao.deleteById(competenceID);
        } catch (SQLException e) {
            System.out.println("There is no employees with the ID " + competenceID);
        }
    }

    /**
     * modifies the data of an already existing competence in the table in the
     * database.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param competenceWithData with the modified and updated data
     */
    public void modifyCompetence(final Competence competenceWithData) {
        try {
            competenceDao.update(competenceWithData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");

        }
    }

    /**
     * searches the database and then returns the competence whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param competenceID of the competence to return
     * @return competence with the given ID
     */
    public Competence getCompetence(Integer competenceID) {
        try {
            return competenceDao.queryForId(competenceID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            return null;
        }
    }

    /**
     * return all competences stored in the database
     *
     * @return a list containing all employees. When the database
     * is empty, an empty arrayList will be returned.
     */
    public List<Competence> getAllCompetences() {
        try {
            return competenceDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

}
