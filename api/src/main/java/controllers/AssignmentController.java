package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignmentDatabase;
import database.methods.ProjectDatabase;
import database.methods.TimeRegistrationDatabase;
import entities.Assignment;
import entities.Project;
import entities.TimeRegistration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

@Tag(name = "Assignments", description = "All implemented operations to manage assignments of the employees in the projects")
@RestController
public class AssignmentController {

    private AssignmentDatabase assignmentDatabase;

    public AssignmentController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            assignmentDatabase = new AssignmentDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL PROJECTS
     *
     * @return List of all assignments
     */
    @ApiResponse(responseCode = "200", description = "Assignments found and returned successfully")
    @Operation(summary = "Get all assignments", description = "Returns a list of all existing assignments in the database")
    @GetMapping("/assignments")
    public List<Assignment> getAssignments() {
        return assignmentDatabase.getAllAssignments();
    }


    /**
     * REST METHOD GET FOR ONE Assignment
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct assignment is returned
     *
     * @param assignmentID - The ID of the assignment
     * @return the assignment corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "Assignment found successfully")
     @Operation(summary = "Finds assignment by ID", description = "Returns a single assignment whose ID was given")
    @GetMapping("/assignments/{id}")
    public Assignment getAssignment(@Parameter(description = "ID of the searched assignment") @PathVariable("id") Integer assignmentID) throws SQLException {
        Assignment assignment = assignmentDatabase.getAssignment(assignmentID);
        return assignment;
    }

    /**
     * REST METHOD DELETE ASSIGNMENT
     * <p>
     * precondition: the given id exists in the database
     * postcondition: assignment no longer exists in the database and the assignment is returned
     *
     * @param assignmentID - id of the assignment to be deleted
     * @return the deleted assignment
     */
    @ApiResponse(responseCode = "200", description = "Assignment found and deleted successfully")
     @Operation(summary = "deletes assignment by ID", description = "Returns the deleted assignment for testing purposes")
    @DeleteMapping("/assignments/{id}")
    public Assignment deleteAssignment(@Parameter(description = "ID of the assignment to delete") @PathVariable("id") Integer assignmentID) throws SQLException {
        Assignment assignment = assignmentDatabase.getAssignment(assignmentID);
        assignmentDatabase.deleteAssignment(assignmentID);
        return assignment;


    }

    /**
     * REST METHOD POST TO ADD ASSIGNMENT
     * <p>
     * precondition: The request body corresponds to the Assignment Class
     * postcondition: The given assignment is added to the database
     *
     * @return
     */
    @Operation(summary = "Adds a new assignment to the database", description = "After adding the assignment returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @PostMapping("/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public int addAssignment(@Parameter(description = "From the automatically JSON parsed Request Body an assignment will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create an assignment ") @Valid @RequestBody Assignment requestBody) {
        try {
            return assignmentDatabase.addAssignment(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE ASSIGNMENT
     * <p>
     * precondition: The request body corresponds to the Assignment Class and the id exists in the database
     * postcondition: The correct assignment has been updated
     */
    @Operation(summary = "Updates assignment by ID", description = "modifies the data of the given assignment")
    @PutMapping("/assignments")
    public void updateAssignment(@Parameter(description = "Contains the ID of the assignment through which the respective assignment will be recognised ") @Valid @RequestBody Assignment requestBody) {
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
     * <p>
     * precondition: the given id exists in the database of employees
     * postcondition: correct list of assignments is returned
     *
     * @param employeeID - The ID of the employee
     * @return the assignments corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "Assignments of the employee with the given ID found and returned successfully")
    @Operation(summary = "Get all assignments of the employee whose ID was given", description = "Returns all assignments of the employee whose ID was given")
    @GetMapping("/assignmentsbyemployee/{id}")
    public List<Assignment> getAssignmentByEmployee(@Parameter(description = "ID of the employee whose assignments should be returned") @PathVariable("id") Integer employeeID) throws SQLException {
        return assignmentDatabase.getAssignmentsByEmployee(employeeID);

    }

    /**
     * deletes all the assignments of one employee
     *
     * @param employeeID of the employee
     * @throws SQLException when the deletion could not be done
     */
    @ApiResponse(responseCode = "200", description = "Assignments of the employee with the given ID found and deleted successfully")
     @Operation(summary = "deletes all assignments of the employee whose ID was given", description = "deletes all assignments of the employee whose ID was given")
    @DeleteMapping("/assignmentsbyemployee/{id}")
    public void deleteAllAssignmentsByEmployee(@Parameter(description = "ID of the employee whose assignments should be deleted") @PathVariable("id") Integer employeeID) throws SQLException {
        assignmentDatabase.deleteAllAssignmentsEmployee(employeeID);
    }


    /**
     * deletes all the assignments of one project
     *
     * @param projectID of the project
     * @throws SQLException when the deletion could not be done
     */
    @ApiResponse(responseCode = "200", description = "Assignments of the project with the given ID found and deleted successfully")
    @Operation(summary = "deletes all assignments of the project whose ID was given", description = "deletes all assignments of the project whose ID was given")
    @DeleteMapping("/assignmentsbyproject/{id}")
    public void deleteAllAssignmentsByProject(@Parameter(description = "ID of the project whose assignments should be deleted") @PathVariable("id") Integer projectID) throws SQLException {
        assignmentDatabase.deleteAllAssignmentsProject(projectID);


    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     * <p>
     * precondition: the given id exists in the database of projects
     * postcondition: correct list of assignments is returned
     *
     * @param projectID - The ID of the project
     */
    @ApiResponse(responseCode = "200", description = "Assignments found successfully")
    @Operation(summary = "Finds assignment of the project whose ID was given", description = "Returns all assignment of a project whose ID was given")
    @GetMapping("/assignmentsbyproject/{id}")
    public List<Assignment> getAssignmentsByProject(@PathVariable("id") Integer projectID) {
      return  assignmentDatabase.getAssignmentsByProject(projectID);


    }
}
