package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import entities.Assignment;
import entities.Client;
import entities.Employee;
import entities.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentDatabase {
    private Dao<Assignment, Integer> assignmentDAO;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of assignments in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the element cannot be created.
     */
    public AssignmentDatabase(ConnectionSource connectionSource) throws SQLException {
        assignmentDAO = DaoManager.createDao(connectionSource, Assignment.class);
        TableUtils.createTableIfNotExists(connectionSource, Assignment.class);
    }


    /**
     * Creates IDs in such a way that after deleting an employee, for example,
     * there are no gaps, but the ID whose assignment was deleted is assigned
     * to another assignment to be added.
     *
     * @return ID of the assignment to be added.
     */
    private int createID() {
        List<Assignment> allAssignments = this.getAllAssignments();
        //  sort the list by ID
        allAssignments.sort(Comparator.comparing(Assignment::getId));
        for (int i = 0; i < allAssignments.size(); i++) {
            if (allAssignments.get(i).getId() != i + 1) {
                return i + 1;
            }
        }
        return allAssignments.size() + 1;
    }

    /**
     * adds an assignment to the data base after setting its id.
     *
     * @param assignmentToAdd to be added to the database
     * @return the id of the added assignmentToAdd
     * @throws SQLException if the assignmentToAdd could not be added!
     */
    public int addAssignment(Assignment assignmentToAdd) throws SQLException {
        int assignmentID = this.createID();
        assignmentToAdd.setId(assignmentID);
        assignmentDAO.create(assignmentToAdd);
        return assignmentID;
    }

    /**
     * deletes the assignment  whose ID was given from the database
     *
     * @param assignmentIDToDelete of the assignment to delete
     * @return the deleted assignment
     * @throws SQLException if the assignment was not deleted successfully
     */
    public Assignment deleteAssignment(Integer assignmentIDToDelete) throws SQLException {
        Assignment assignmentToDelete = getAssignment(assignmentIDToDelete);
        assignmentDAO.delete(assignmentToDelete);
        return assignmentToDelete;
    }

    /**
     * searches the database and then returns the object with the given ID
     *
     * @param assignmentID of the assignment to search
     * @return the assignment whose ID was given
     * @throws SQLException if there is no element with the given ID
     */
    public Assignment getAssignment(Integer assignmentID) throws SQLException {
        return assignmentDAO.queryForId(assignmentID);
    }

    /**
     * modifies the data of an already existing assignment in the table in the
     * database.
     *
     * @param assignmentWithNewData to override the existing one
     * @throws SQLException if the edit could not be done successfulyy
     */
    public void modifyAssignmentData(Assignment assignmentWithNewData) throws SQLException {
        assignmentDAO.update(assignmentWithNewData);
    }


    /**
     * searches the database and then returns the list of projects in which the employee is involved
     *
     * @param employeeID of the employee involved in the projects
     * @return list of projects in which the employee is involved
     */
    public List<Assignment> getAssignmentsByEmployee(Integer employeeID) {
        List<Assignment> allAssignments = this.getAllAssignments();
        return allAssignments.stream().filter(assignment -> assignment.getEmployeeID().equals(employeeID))
                .collect(Collectors.toList());
    }


    /**
     * searches the database and then returns the list of employees involved in the project
     * with the given ID
     *
     * @param projectID in which the employees are involved
     * @return list of employees involved in the project with given ID
     */
    public List<Assignment> getAssignmentsByProject(int projectID) {
        List<Assignment> allAssignments = this.getAllAssignments();
        return allAssignments.stream().filter(assignment -> assignment.getProjectID().equals(projectID))
                .collect(Collectors.toList());
    }


    /**
     * return all assignments stored in the database
     */
    public List<Assignment> getAllAssignments() {
        try {
            return assignmentDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

}
