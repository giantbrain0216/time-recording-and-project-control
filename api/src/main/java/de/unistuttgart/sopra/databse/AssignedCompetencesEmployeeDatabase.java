package de.unistuttgart.sopra.databse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.unistuttgart.sopra.entities.AssignedCompetencesEmployee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class to manage the database of the competences
 *
 * @version 02.01.2021
 */
public class AssignedCompetencesEmployeeDatabase {
    private Dao<AssignedCompetencesEmployee, Integer> assignedCompetencesEmployeeDao;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of competences in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the dao element cannot be created.
     */
    public AssignedCompetencesEmployeeDatabase(ConnectionSource connectionSource) throws SQLException {
        assignedCompetencesEmployeeDao = DaoManager.createDao(connectionSource, AssignedCompetencesEmployee.class);
        TableUtils.createTableIfNotExists(connectionSource, AssignedCompetencesEmployee.class);
    }

    /**
     * Creates IDs in such a way that after deleting a competence, for example,
     * there are no gaps, but the ID whose competence was deleted is assigned
     * to another competence to be added.
     *
     * @return ID of the competence to be added.
     */
    private int createID() {
        List<AssignedCompetencesEmployee> listOfCompetences = this.getAllAssignedCompetencesEmployee();
        //  sort the list by ID
        listOfCompetences.sort(Comparator.comparing(AssignedCompetencesEmployee::getId));
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
    public int addToDatabase(AssignedCompetencesEmployee competence) throws SQLException {
        int employeeToAddID = createID();
        competence.setId(employeeToAddID);
        assignedCompetencesEmployeeDao.create(competence);
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
            assignedCompetencesEmployeeDao.deleteById(competenceID);
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
    public void modifyAssignedCompetencesEmployee(final AssignedCompetencesEmployee competenceWithData) {
        try {
            assignedCompetencesEmployeeDao.update(competenceWithData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");

        }
    }

    /**
     * searches the database and then returns the competence whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param assignmentID of the competence to return
     * @return competence with the given ID
     */
    public AssignedCompetencesEmployee getAssignedCompetencesEmployee(Integer assignmentID) {
        try {
            return assignedCompetencesEmployeeDao.queryForId(assignmentID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            return null;
        }
    }

    /**
     * return all AssignedCompetencesEmployee stored in the database
     *
     * @return a list containing all AssignedCompetencesEmployee . When the database
     * is empty, an empty arrayList will be returned.
     */
    public List<AssignedCompetencesEmployee> getAllAssignedCompetencesEmployee() {
        try {
            return assignedCompetencesEmployeeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

    /**
     * searches the table in the database and returns all assignments whose employee ID is equal to the given one
     *
     * @param employeeID of the project
     * @return list of the assignment of competences to this project
     */
    public List<AssignedCompetencesEmployee> getAllAssignedCompetencesByEmployee(Integer employeeID) {
        List<AssignedCompetencesEmployee> allAssignedCompetencesProject = getAllAssignedCompetencesEmployee();
        return allAssignedCompetencesProject.stream().filter(competence -> competence.getEmployeeID() == employeeID).collect(Collectors.toList());
    }

    /**
     * searches the table in the database and deletes all assignments whose emplyoee ID is equal to the given one
     *
     * @param employeeID of the project
     * @throws SQLException if at least one assignment could not been deleted
     */

    public void deleteAllAssignedCompetencesByEmployee(Integer employeeID) throws SQLException {
        List<AssignedCompetencesEmployee> allAssignedCompetencesProject = getAllAssignedCompetencesByEmployee(employeeID);
        for (AssignedCompetencesEmployee assignedCompetencesEmployee : allAssignedCompetencesProject) {
            assignedCompetencesEmployeeDao.deleteById(assignedCompetencesEmployee.getId());
        }
    }

    /**
     * @return all IDs of assigned competences
     */
    public List<Integer> getAllAssignedCompetences(){
        List<AssignedCompetencesEmployee> listOfAllAssignments = getAllAssignedCompetencesEmployee();
        return listOfAllAssignments.stream().map(AssignedCompetencesEmployee::getCompetenceID).collect(Collectors.toList());
    }

    /**
     * searches the database and then returns the list with the competences IDs  of the
     * employee whose ID was given
     *
     * @param employeeID of the employee
     * @return list of IDs of competences of the employee whose ID was given
     */
    public List<Integer> getCompetences(Integer employeeID) {
        List<AssignedCompetencesEmployee> allAssignedCompetencesEmployee = this.getAllAssignedCompetencesEmployee();
        return allAssignedCompetencesEmployee.stream().filter(assignment -> assignment.getEmployeeID() == (employeeID))
                .map(AssignedCompetencesEmployee::getCompetenceID)
                .collect(Collectors.toList());
    }




}

