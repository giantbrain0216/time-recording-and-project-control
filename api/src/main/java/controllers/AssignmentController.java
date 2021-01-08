package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignmentDatabase;
import database.methods.ProjectDatabase;
import database.methods.TimeRegistrationDatabase;
import entities.Assignment;
import entities.Project;
import entities.TimeRegistration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import static properties.Properties.*;

@RestController
public class AssignmentController {

    private AssignmentDatabase assignmentDatabase;

    public AssignmentController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            assignmentDatabase = new AssignmentDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL PROJECTS
     *
     * @return List of all assignments
     */
    @GetMapping("/assignments")
    public List<Assignment> getAssignments() {
        return assignmentDatabase.getAllAssignments();
    }


    /**
     * REST METHOD GET FOR ONE Assignment
     *
     *  precondition: the given id exists in the database
     *  postcondition: correct assignment is returned
     *
     * @param assignmentID - The ID of the assignment
     * @return the assignment corresponding to the id
     */
    @GetMapping("/assignments/{id}")
    public Assignment getAssignment(@PathVariable("id") Integer assignmentID) throws SQLException {
        Assignment assignment = assignmentDatabase.getAssignment(assignmentID);
        return assignment;
    }

    /**
     * REST METHOD DELETE ASSIGNMENT
     *
     * precondition: the given id exists in the database
     * postcondition: assignment no longer exists in the database and the assignment is returned
     *
     * @param assignmentID - id of the assignment to be deleted
     * @return the deleted assignment
     */
    @DeleteMapping("/assignments/{id}")
    public Assignment deleteAssignment(@PathVariable("id") Integer assignmentID) throws SQLException {
        Assignment assignment = assignmentDatabase.getAssignment(assignmentID);
        assignmentDatabase.deleteAssignment(assignmentID);
        return assignment;
    }

    /**
     * REST METHOD POST TO ADD ASSIGNMENT
     *
     * precondition: The request body corresponds to the Assignment Class
     * postcondition: The given assignment is added to the database
     *
     * @return
     */
    @PostMapping("/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public int addAssignment(@Valid @RequestBody Assignment requestBody){
        try {
            return assignmentDatabase.addAssignment(requestBody);
        }catch(SQLException e){
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE ASSIGNMENT
     *
     * precondition: The request body corresponds to the Assignment Class and the id exists in the database
     * postcondition: The correct assignment has been updated
     *
     */
    @PutMapping("/assignments")
    public void updateAssignment(@Valid @RequestBody Assignment requestBody) {
        // requestBody.setProjectNumber(id);
        try {
            assignmentDatabase.modifyAssignmentData(requestBody);
        } catch (SQLException throwables) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "Precondition Failed ERROR CODE 412");
        }

    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY EMPLOYEE
     *
     *  precondition: the given id exists in the database of employees
     *  postcondition: correct list of assignments is returned
     *
     * @param employeeID - The ID of the employee
     * @return the assignments corresponding to the id
     */
    @GetMapping("/assignmentsbyemployee/{id}")
    public List<Assignment> getAssignmentByEmployee(@PathVariable("id") Integer employeeID) throws SQLException {
        return assignmentDatabase.getAssignmentsByEmployee(employeeID);
    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     *
     *  precondition: the given id exists in the database of projects
     *  postcondition: correct list of assignments is returned
     *
     * @param projectID - The ID of the project
     * @return the assignments corresponding to the project id
     */
    @GetMapping("/assignmentsbyproject/{id}")
    public List<Assignment> getAssignmentsByProject(@PathVariable("id") Integer projectID) throws SQLException {
        return assignmentDatabase.getAssignmentsByProject(projectID);
    }
}
