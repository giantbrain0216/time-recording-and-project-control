package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import controllers.Application;
import entities.AssignedCompetencesEmployee;
import entities.AssignedCompetencesProject;
import entities.Assignment;
import entities.Competence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class to manage the database of the competences
 *
 * @version 01.01.2021
 */
public class AssignedCompetencesProjectDatabase {
    private Dao<AssignedCompetencesProject, Integer> assignedCompetencesProjectDao;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of competences in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the dao element cannot be created.
     */
    public AssignedCompetencesProjectDatabase(ConnectionSource connectionSource) throws SQLException {
        assignedCompetencesProjectDao = DaoManager.createDao(connectionSource, AssignedCompetencesProject.class);
        TableUtils.createTableIfNotExists(connectionSource, AssignedCompetencesProject.class);
    }

    /**
     * Creates IDs in such a way that after deleting a competence, for example,
     * there are no gaps, but the ID whose competence was deleted is assigned
     * to another competence to be added.
     *
     * @return ID of the competence to be added.
     */
    private int createID() {
        List<AssignedCompetencesProject> listOfCompetences = this.getAllAssignedCompetencesProject();
        //  sort the list by ID
        listOfCompetences.sort(Comparator.comparing(AssignedCompetencesProject::getId));
        for (int i = 0; i < listOfCompetences.size(); i++) {
            if (listOfCompetences.get(i).getId() != i + 1) {
                return i + 1;
            }
        }
        return listOfCompetences.size() + 1;
    }

    /**
     * @return all IDs of assigned competences
     */
    public List<Integer> getAllAssignedCompetences(){
        List<AssignedCompetencesProject> listOfAllAssignments = getAllAssignedCompetencesProject();
        return listOfAllAssignments.stream().map(AssignedCompetencesProject::getCompetenceID).collect(Collectors.toList());
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
    public int addToDatabase(AssignedCompetencesProject competence) throws SQLException {
        int employeeToAddID = createID();
        competence.setId(employeeToAddID);
        assignedCompetencesProjectDao.create(competence);
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
            assignedCompetencesProjectDao.deleteById(competenceID);
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
    public void modifyAssignedCompetencesProject(final AssignedCompetencesProject competenceWithData) {
        try {
            assignedCompetencesProjectDao.update(competenceWithData);
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
    public AssignedCompetencesProject getAssignedCompetencesProject(Integer assignmentID) {
        try {
            return assignedCompetencesProjectDao.queryForId(assignmentID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            return null;
        }
    }

    /**
     * return all AssignedCompetencesProject stored in the database
     *
     * @return a list containing all AssignedCompetencesProject . When the database
     * is empty, an empty arrayList will be returned.
     */
    public List<AssignedCompetencesProject> getAllAssignedCompetencesProject() {
        try {
            return assignedCompetencesProjectDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

    /**
     * searches the table in the database and returns all assignments whose Project ID is equal to the given one
     *
     * @param projectID of the project
     * @return list of the assignment of competences to this project
     */
    public List<AssignedCompetencesProject> getAllAssignedCompetencesByProject(Integer projectID) {
        List<AssignedCompetencesProject> allAssignedCompetencesProject = getAllAssignedCompetencesProject();
        return allAssignedCompetencesProject.stream().filter(competence -> competence.getProjectID() == projectID).collect(Collectors.toList());
    }

    /**
     * searches the table in the database and deletes all assignments whose Project ID is equal to the given one
     *
     * @param projectID of the project
     * @throws SQLException if at least one assignment could not been deleted
     */

    public void deleteAllAssignedCompetencesByProject(Integer projectID) throws SQLException {
        List<AssignedCompetencesProject> allAssignedCompetencesProject = getAllAssignedCompetencesByProject(projectID);
        for (AssignedCompetencesProject assignedCompetencesProject : allAssignedCompetencesProject) {
            assignedCompetencesProjectDao.deleteById(assignedCompetencesProject.getId());
        }
    }

    /**
     * searches the database and then returns the list with the competences IDs  of the
     * project whose ID was given
     *
     * @param projectID of the project
     * @return list of IDs of competences of the project whose ID was given
     */
    public List<Integer> getCompetences(Integer projectID) {
        List<AssignedCompetencesProject> allAssignedCompetencesProject = this.getAllAssignedCompetencesProject();
        return allAssignedCompetencesProject.stream().filter(assignment -> assignment.getProjectID() == (projectID))
                .map(AssignedCompetencesProject::getCompetenceID)
                .collect(Collectors.toList());
    }


}

