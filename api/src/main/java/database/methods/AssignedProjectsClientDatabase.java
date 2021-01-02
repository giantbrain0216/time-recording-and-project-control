package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import entities.AssignedProjectsClient;

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
public class AssignedProjectsClientDatabase {
    private final Dao<AssignedProjectsClient, Integer> assignedProjectsClients;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of competences in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the dao element cannot be created.
     */
    public AssignedProjectsClientDatabase(ConnectionSource connectionSource) throws SQLException {
        assignedProjectsClients = DaoManager.createDao(connectionSource, AssignedProjectsClient.class);
        TableUtils.createTableIfNotExists(connectionSource, AssignedProjectsClient.class);
    }

    /**
     * Creates IDs in such a way that after deleting an assignment, for example,
     * there are no gaps, but the ID whose assignment was deleted is assigned
     * to another assignment to be added.
     *
     * @return ID of the assignment to be added.
     */
    private int createID() {
        List<AssignedProjectsClient> listAllAssignedProjectsClient = this.getAllAssignedProjectsClient();
        //  sort the list by ID
        listAllAssignedProjectsClient.sort(Comparator.comparing(AssignedProjectsClient::getId));
        for (int i = 0; i < listAllAssignedProjectsClient.size(); i++) {
            if (listAllAssignedProjectsClient.get(i).getId() != i + 1) {
                return i + 1;
            }
        }
        return listAllAssignedProjectsClient.size() + 1;
    }

    /**
     * Adds the given assignment to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same ID
     * as the given employee's
     * Post conditions : The assignment has been added to the database. Otherwise
     * an exception will be thrown in the case another employee with the same ID
     * occurs in the table.
     *
     * @param assignment must not be null and its ID must be unique
     * @return ID of the assignment that has been added for testing purposes
     * @throws SQLException if the assignment cannot be added.
     */
    public int addToDatabase(AssignedProjectsClient assignment) throws SQLException {
        int employeeToAddID = createID();
        assignment.setId(employeeToAddID);
        assignedProjectsClients.create(assignment);
        return employeeToAddID;
    }

    /**
     * deletes an existing AssignedProjectsClient from the table in the database.
     * An error message will be printed in case there is no competence
     * with the given ID.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param assignmentID of the AssignedProjectsClient which is unique
     */
    public void deleteFromDatabase(Integer assignmentID) {
        try {
            assignedProjectsClients.deleteById(assignmentID);
        } catch (SQLException e) {
            System.out.println("There is no employees with the ID " + assignmentID);
        }
    }

    /**
     * modifies the data of an already existing AssignedProjectsClient in the table in the
     * database.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param assignment with the modified and updated data
     */
    public void modifyAssignedProjectsClient(final AssignedProjectsClient assignment) {
        try {
            assignedProjectsClients.update(assignment);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");

        }
    }

    /**
     * searches the database and then returns the AssignedProjectsClient whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param assignmentID of the competence to return
     * @return AssignedProjectsClient with the given ID
     */
    public AssignedProjectsClient getAssignedProjectsClient(Integer assignmentID) {
        try {
            return assignedProjectsClients.queryForId(assignmentID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            return null;
        }
    }

    /**
     * return all AssignedProjectsClient stored in the database
     *
     * @return a list containing all AssignedProjectsClient . When the database
     * is empty, an empty arrayList will be returned.
     */
    public List<AssignedProjectsClient> getAllAssignedProjectsClient() {
        try {
            return assignedProjectsClients.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

    /**
     * searches the database and then returns the list with the  IDs  of the
     * projects of the client whose ID was given
     *
     * @param clientID of the project
     * @return list of IDs of projects of the client whose ID was given
     */
    public List<Integer> getProjectIDs(Integer clientID) {
        List<AssignedProjectsClient> allAssignedProjectsClient = this.getAllAssignedProjectsClient();
        return allAssignedProjectsClient.stream().map(AssignedProjectsClient::getClientID)
                .filter(integer -> integer.equals(clientID))
                .collect(Collectors.toList());
    }


}

