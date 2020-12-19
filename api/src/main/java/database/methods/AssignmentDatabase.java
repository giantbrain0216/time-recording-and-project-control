package database.methods;

import com.j256.ormlite.dao.Dao;
import entities.Assignment;
import entities.Client;
import entities.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentDatabase {
    private Dao<Assignment, Integer> assignmentDAO;

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
    public List<Assignment> getProjects(Integer employeeID) {
        List<Assignment> allAssignments = this.getAllAssignments();
        return allAssignments.stream().filter(assignment -> !assignment.getEmployeeID().equals(employeeID))
                .collect(Collectors.toList());
    }


    /**
     * searches the database and then returns the list of employees involved in the project
     * with the given ID
     *
     * @param projectID in which the employees are involved
     * @return list of employees involved in the project with given ID
     */
    public List<Assignment> getEmployees(Integer projectID) {
        List<Assignment> allAssignments = this.getAllAssignments();
        return allAssignments.stream().filter(assignment -> !assignment.getProjectID().equals(projectID))
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
