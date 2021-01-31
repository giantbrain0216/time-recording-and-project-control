package de.unistuttgart.sopra.controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import de.unistuttgart.sopra.databse.AssignedCompetencesProjectDatabase;
import de.unistuttgart.sopra.entities.AssignedCompetencesProject;
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
public class AssignedCompetencesProjectController {
    private AssignedCompetencesProjectDatabase assignedCompetencesProjectDatabase;

    public AssignedCompetencesProjectController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            assignedCompetencesProjectDatabase = new AssignedCompetencesProjectDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL Assigned Competences to the projects
     *
     * @return List of all assignedCompetencesProject
     */
    @GetMapping("/assignedCompetencesProject")
    @ApiResponse(responseCode = "200", description = "All assigned competences to the projects found and returned successfully")
    @Operation(summary = "Get allassigned competences to the projects", description = "Returns all assigned competences to the" +
            " projects")
    public List<AssignedCompetencesProject> getAssignedCompetencesProject() {
        return assignedCompetencesProjectDatabase.getAllAssignedCompetencesProject();
    }

    /**
     * REST METHOD GET FOR ONE AssignedCompetencesProject
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct AssignedCompetencesProject returned
     *
     * @param assignmentID - The ID of the assignment
     * @return Assignment corresponding to the id
     */
     @ApiResponse(responseCode = "200", description = "The assignment whose ID was given of the competence to the project found and returned successfully")
    @Operation(summary = "Get The assignment whose ID was given of the competence to the project ",
            description = "Returns The assignment whose ID was given of the competence to the project ")

    @GetMapping("/assignedCompetencesProject/{id}")
    public AssignedCompetencesProject getAssignedCompetencesProject(@Parameter(description = "ID of the assignment") @PathVariable("id") Integer assignmentID) {
        AssignedCompetencesProject assignedCompetencesProject = assignedCompetencesProjectDatabase.getAssignedCompetencesProject(assignmentID);
        return assignedCompetencesProject;
    }

    /**
     * REST METHOD DELETE AssignedCompetencesProject
     * <p>
     * precondition: the given id exists in the database
     * postcondition: client no longer exists in the database and the client is returned
     *
     * @param assignmentID - id of the employee to be deleted
     */
    @DeleteMapping("/assignedCompetencesProject/{id}")
    @ApiResponse(responseCode = "200", description = "The assignment whose ID was given of the competence to the project found and deleted successfully")
    @Operation(summary = "Deleted The assignment whose ID was given of the competence to the project ",
            description = "Deletes The assignment whose ID was given of the competence to the project ")
    public AssignedCompetencesProject deleteAssignedCompetencesProject(@Parameter(description = "ID of the assignment to delete")@PathVariable("id") Integer assignmentID) {
        AssignedCompetencesProject AssignedCompetencesProject = assignedCompetencesProjectDatabase.getAssignedCompetencesProject(assignmentID);
        assignedCompetencesProjectDatabase.deleteFromDatabase(assignmentID);
        return AssignedCompetencesProject;
    }

    /**
     * REST METHOD GET FOR ALL Assigned Competences to the employees
     *
     * @return List of all assignedCompetencesEmployee
     */
    @GetMapping("/allAssignedCompetencesProject")
    @ApiResponse(responseCode = "200", description = "All IDs of all assignment of  competences to the projects found and returned successfully")
    @Operation(summary = "Get all IDsof all assignment of  competences to the projects", description = "Returns all IDs of all assignment of  competences to the" +
            " projects")
    public List<Integer> getAssignedCompetences() {
        return assignedCompetencesProjectDatabase.getAllAssignedCompetences();
    }

    /**
     * deletes all the assignments of one project and competences
     *
     * @param projectID of the project
     * @throws SQLException when the deletion could not be done
     */
    @ApiResponse(responseCode = "200", description = "The assignments of the competences to the project whose ID was given found and deleted successfully")
    @Operation(summary = "Deletes The assignments of the competences to the project whose ID was given",
            description = "deletesThe assignments of the competences to the project whose ID was given")
    @DeleteMapping("/allAssignedCompetencesProject/{id}")
    public void deleteAllAssignmentsByProject(@Parameter(description = "ID of the project whose assignments of competences should be deleted") @Valid @PathVariable("id") Integer projectID) throws SQLException {
        assignedCompetencesProjectDatabase.deleteAllAssignedCompetencesByProject(projectID);
    }

    /**
     * REST METHOD POST TO ADD AssignedCompetencesProject
     * <p>
     * precondition: The request body corresponds to the AssignedCompetencesProject Class
     * postcondition: The given client is added to the database
     *
     * @return ID of the added AssignedCompetencesProject
     */
    @Operation(summary = "Adds a new assignment to the database", description = "After adding the assigned competence to the employee returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @PostMapping("/assignedCompetencesProject")
    @ResponseStatus(HttpStatus.CREATED)
    public int assignNewCompetenceToTheProject(@Parameter(description = "From the automatically JSON parsed Request Body an assigned" +
            " competence to the project object will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create an " +
            "assigned competence to the project object ") @Valid @RequestBody AssignedCompetencesProject requestBody) {
        try {
            return assignedCompetencesProjectDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE CLIENT
     * <p>
     * precondition: The request body corresponds to the AssignedCompetencesProject Class and the id exists in the database
     * postcondition: The correct AssignedCompetencesProject has been updated
     */
    @Operation(summary = "Updates the assigned competence to the project by ID", description = "modifies the data of the given assignment of the competence to the project")
    @PutMapping("/assignedCompetencesProject")
    public void updateAssignment(@Parameter(description = "Contains the ID of the assignment through which the respective assignment will" +
            " be recognised")@Valid @RequestBody AssignedCompetencesProject requestBody) {
        assignedCompetencesProjectDatabase.modifyAssignedCompetencesProject(requestBody);
    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     * <p>
     * precondition: the given id exists in the database of AssignedCompetencesProject
     * postcondition: correct list of competences is returned
     *
     * @param projectID - The ID of the project
     * @return the competences corresponding to the project id
     */
    @ApiResponse(responseCode = "200", description = "All IDs of assigned competences to the projects found and returned successfully")
    @Operation(summary = "Get all IDs of the assigned competences to the projects", description = "Returns all IDs of the assigned competences to the" +
            " projects")
    @GetMapping("/competencesByProject/{id}")
    public List<Integer> getCompetencesByProject(@Parameter(description = "ID of the project") @Valid@PathVariable("id") Integer projectID) {
        return assignedCompetencesProjectDatabase.getCompetences(projectID);
    }
}

