package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignedCompetencesEmployeeDatabase;
import entities.AssignedCompetencesEmployee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

@Tag(name = "Assigned Competences To The Employees", description = "All implemented operations to manage assignments" +
        " of the competences to the employees")
@RestController
public class AssignedCompetencesEmployeeController {

    private AssignedCompetencesEmployeeDatabase assignedCompetencesEmployeeDatabase;

    public AssignedCompetencesEmployeeController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            assignedCompetencesEmployeeDatabase = new AssignedCompetencesEmployeeDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL Assigned Competences to the employees
     *
     * @return List of all assignedCompetencesEmployee
     */
    @ApiResponse(responseCode = "200", description = "All IDs of the assignments of the competences to the employee found and returned successfully")
    @Operation(summary = "Get all IDs of the assignments of the competences to the employees", description = "Returns all IDs of the assignments of the competences to the employees")
    @GetMapping("/allAssignedCompetencesEmployees")
    public List<Integer> getAssignedCompetences() {
        return assignedCompetencesEmployeeDatabase.getAllAssignedCompetences();
    }

    /**
     * REST METHOD GET FOR ALL Assigned Competences to the employees
     *
     * @return List of all assignedCompetencesEmployee
     */
    @ApiResponse(responseCode = "200", description = "All assigned competences to the employee found and returned successfully")
    @Operation(summary = "Get all assigned competences to the employees", description = "Returns all assigned competences to the employees")
    @GetMapping("/assignedCompetencesEmployee")
    public List<AssignedCompetencesEmployee> getAssignedCompetencesEmployee() {
        return assignedCompetencesEmployeeDatabase.getAllAssignedCompetencesEmployee();
    }

    /**
     * REST METHOD GET FOR ONE assignedCompetencesEmployee
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct assignedCompetencesEmployee returned
     *
     * @param assignmentID - The ID of the assignment
     * @return Assignment corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "The assignment whose ID was given of the competence to the employee found and returned successfully")
    @Operation(summary = "Get The assignment whose ID was given of the competence  to the employee ",
            description = "Returns The assignment whose ID was given of the competence to the employee ")
    @GetMapping("/assignedCompetencesEmployee/{id}")
    public AssignedCompetencesEmployee getAssignedCompetencesEmployee(@Parameter(description = "ID of the assignment") @PathVariable("id") Integer assignmentID) {
        return assignedCompetencesEmployeeDatabase.getAssignedCompetencesEmployee(assignmentID);
    }

    /**
     * REST METHOD DELETE assignedCompetencesEmployee
     * <p>
     * precondition: the given id exists in the database
     * postcondition: assignment no longer exists in the database and the client is returned
     *
     * @param assignmentID - id of the assignment to be deleted
     */
    @ApiResponse(responseCode = "200", description = "All assigned competences to the employee found and deleted successfully")
    @Operation(summary = "Delete all assigned competences to the employees", description = "deletes all assigned competences to the employees")
    @DeleteMapping("/assignedCompetencesEmployee/{id}")
    public AssignedCompetencesEmployee deleteAssignedCompetencesEmployee(@Parameter(description = "ID of the employee") @PathVariable("id") Integer assignmentID) {
        AssignedCompetencesEmployee assignedCompetencesEmployee = assignedCompetencesEmployeeDatabase
                .getAssignedCompetencesEmployee(assignmentID);
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(assignmentID);
        return assignedCompetencesEmployee;
    }

    /**
     * deletes all the assignments of one employee and competences
     *
     * @param employeeID of the project
     * @throws SQLException when the deletion could not be done
     */

    @ApiResponse(responseCode = "200", description = "All assigned competences to the employee found and deleted successfully")
    @Operation(summary = "Delete all assigned competences to the employee whose ID wa given",
            description = "deletes all assigned competences to the employee whose ID was given")
    @DeleteMapping("/allAssignedCompetencesEmployee/{id}")
    public void deleteAllAssignmentsByEmployee(@Parameter(description = "ID of the employee") @PathVariable("id") Integer employeeID) throws SQLException {
        assignedCompetencesEmployeeDatabase.deleteAllAssignedCompetencesByEmployee(employeeID);
    }

    /**
     * REST METHOD POST TO ADD assignedCompetencesEmployee
     * <p>
     * precondition: The request body corresponds to the assignedCompetencesEmployee Class
     * postcondition: The given client is added to the database
     *
     * @return ID of the added assignedCompetencesEmployee
     */
    @PostMapping("/assignedCompetencesEmployee")
    @Operation(summary = "Adds a new assignment to the database", description = "After adding the assigned competence to the employee returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @ResponseStatus(HttpStatus.CREATED)
    public int addAssignment(@Parameter(description = "From the automatically JSON parsed Request Body an assigned competence to the employee object will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create an " +
            "assigned competence to the employee object ") @Valid @RequestBody AssignedCompetencesEmployee requestBody) {
        try {
            return assignedCompetencesEmployeeDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE assignedCompetencesEmployee
     * <p>
     * precondition: The request body corresponds to the assignedCompetencesEmployee Class and the id exists in the database
     * postcondition: The correct assignedCompetencesEmployee has been updated
     */
    @Operation(summary = "Updates the assigned competence to the employee by ID", description = "modifies the data of the given assignment of the competence to the employee")
    @PutMapping("/assignedCompetencesEmployee")
    public void updateAssignment(@Parameter(description = "Contains the ID of the assignment through which the respective assignment will" +
            " be recognised")@Valid @RequestBody AssignedCompetencesEmployee requestBody) {
        assignedCompetencesEmployeeDatabase.modifyAssignedCompetencesEmployee(requestBody);
    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     * <p>
     * precondition: the given id exists in the database of assignedCompetencesEmployee
     * postcondition: correct list of competences is returned
     *
     * @param employeeID - The ID of the employee
     * @return the competences corresponding to the employeeID
     */
    @ApiResponse(responseCode = "200", description = "All assigned competences to the employee whose ID was given found and returned successfully")
    @Operation(summary = "Get all assigned competences to one employee", description = "Returns all assigned competences to the employee whose ID was given ")
    @GetMapping("/competencesByEmployee/{id}")
    public List<Integer> getCompetencesByEmployee( @Parameter(description = "ID of the employee") @PathVariable("id") Integer employeeID) {
        return assignedCompetencesEmployeeDatabase.getCompetences(employeeID);
    }
}


