package de.unistuttgart.sopra.controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import de.unistuttgart.sopra.databse.AssignedProjectsClientDatabase;
import de.unistuttgart.sopra.entities.AssignedProjectsClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static de.unistuttgart.sopra.properties.Properties.*;

@RestController
public class AssignedProjectsClientController {

    private AssignedProjectsClientDatabase assignedProjectsClientDatabase;

    public AssignedProjectsClientController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            assignedProjectsClientDatabase = new AssignedProjectsClientDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL Assigned projects to the clients
     *
     * @return List of all assignedProjectsClient
     */
    @GetMapping("/assignedProjectsClient")
    @ApiResponse(responseCode = "200", description = "All assignments of projects to clients found and returned successfully")
    @Operation(summary = "Get all assignments of projects to clients ", description = "Returns all assignments of projects to clients ")
    public List<AssignedProjectsClient> getAllAssignedProjectsClient() {
        return assignedProjectsClientDatabase.getAllAssignedProjectsClient();
    }

    /**
     * REST METHOD GET FOR ONE assignedProjectsClient
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct assignedProjectsClient returned
     *
     * @param assignmentID - The ID of the AssignedProjectsClient
     * @return AssignedProjectsClient corresponding to the id
     */
    @GetMapping("/assignedProjectsClient/{id}")
    @ApiResponse(responseCode = "200", description = "The assignment whose ID was given of projects to clients found and returned successfully")
    @Operation(summary = "Get The assignment whose ID was given of projects to clients  ", description = "Returns The assignment whose ID was given of projects to clients  ")
    public AssignedProjectsClient getAssignedProjectsClient(@Parameter(description = "ID of the assignment") @Valid @PathVariable("id") Integer assignmentID) {
        return assignedProjectsClientDatabase.getAssignedProjectsClient(assignmentID);
    }

    /**
     * REST METHOD DELETE assignedProjectsClient
     * <p>
     * precondition: the given id exists in the database
     * postcondition: AssignedProjectsClient no longer exists in the database and the client is returned
     *
     * @param assignmentID - id of the employee to be deleted
     */
    @DeleteMapping("/assignedProjectsClient/{id}")
    @ApiResponse(responseCode = "200", description = "The assignment whose ID was given of projects to clients found and deleted successfully")
    @Operation(summary = "Delete The assignment whose ID was given of projects to clients  ", description = "deleted The assignment whose ID was given of projects to clients  ")
    public AssignedProjectsClient deleteAssignedProjectsClient(@Parameter(description = "ID of the assignment to delete") @Valid @PathVariable("id") Integer assignmentID) {
        AssignedProjectsClient assignedProjectsClient = assignedProjectsClientDatabase.getAssignedProjectsClient(assignmentID);
        assignedProjectsClientDatabase.deleteFromDatabase(assignmentID);
        return assignedProjectsClient;
    }

    /**
     * deletes all the projects of one client
     *
     * @param clientID of the client
     * @return list of the IDs of the deleted projects
     * @throws SQLException when the deletion could not be done
     */
    @DeleteMapping("/allAssignedProjectsClient/{id}")
    @ApiResponse(responseCode = "200", description = "All assignments of the client whose ID was given found and deleted successfully")
    @Operation(summary = "Delete All assignments of the client whose ID was given", description = "deleted All assignments of the client whose ID was given ")
    public List<Integer> deleteAllProjectsByClient(@Parameter(description = "ID of the client whose assignments should be deleted") @Valid @PathVariable("id") Integer clientID) throws SQLException {
        return assignedProjectsClientDatabase.deleteAllProjectsByClient(clientID);
    }

    /**
     * REST METHOD POST TO ADD assignedProjectsClient
     * <p>
     * precondition: The request body corresponds to the assignedProjectsClient Class
     * postcondition: The given AssignedProjectsClient is added to the database
     *
     * @return ID of the added assignedProjectsClient
     */
    @PostMapping("/assignedProjectsClient")
    @Operation(summary = "Adds a new assignment to the database", description = "After adding the assigned project to the client returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @ResponseStatus(HttpStatus.CREATED)
    public int assignNewProjectToClient(@Parameter(description = "From the automatically JSON parsed Request Body an assigned" +
            " project to the client  object will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create an " +
            "assigned project to the client object ") @Valid @RequestBody AssignedProjectsClient requestBody) {
        try {
            return assignedProjectsClientDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE AssignedProjectsClient
     * <p>
     * precondition: The request body corresponds to the assignedProjectsClient Class and the id exists in the database
     * postcondition: The correct assignedProjectsClient has been updated
     */
    @Operation(summary = "Updates the project to the client by ID", description = "modifies the data of the given assignment of the project to the client")
    @PutMapping("/assignedProjectsClient")
    public void updateClient(@Parameter(description = "Contains the ID of the assignment through which the respective assignment will" +
            " be recognised") @Valid @RequestBody AssignedProjectsClient requestBody) {
        assignedProjectsClientDatabase.modifyAssignedProjectsClient(requestBody);
    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     * <p>
     * precondition: the given id exists in the database of assignedProjectsClient
     * postcondition: correct list of IDs is returned
     *
     * @param clientID - The ID of the client
     * @return the IDs of the projects corresponding to the client id
     */
    @GetMapping("/projectsByClient/{id}")
    @ApiResponse(responseCode = "200", description = "All IDs of the assigned project to the client whose ID was given found and returned successfully")
    @Operation(summary = "Get All IDs of the assigned project to the client whose ID was given ", description = "Returns All IDs of the assigned project to the client whose ID was given   ")
    public List<Integer> getProjectsByClient(@Parameter(description = "ID of the client whose project IDs should be returned") @Valid @PathVariable("id") Integer clientID) {
        return assignedProjectsClientDatabase.getProjectIDs(clientID);
    }
}

