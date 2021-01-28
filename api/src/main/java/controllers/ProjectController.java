package controllers;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ProjectDatabase;
import entities.Project;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;


@Tag(name = "Employees", description = "All implemented operations to manage employees")
@RestController
public class ProjectController {

    private ProjectDatabase projectDatabase;

    public ProjectController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            projectDatabase = new ProjectDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL PROJECTS
     *
     * @return List of all Projects
     */
    @ApiResponse(responseCode = "200", description = "Projects found and returned successfully")
    @Operation(summary = "Get all existing projects", description = "Returns a list of all existing projects in the database")
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectDatabase.getAllProjects();
    }

    /**
     * REST METHOD GET FOR ONE PROJECT
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct Project is returned
     *
     * @param projectID - The ID of the project
     * @return the project corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "Project found successfully")
    @ApiResponse(responseCode = "400", description = "Project with the given ID does not exist")
    @Operation(summary = "Finds project by ID", description = "Returns a single project whose ID was given")
    @GetMapping("/projects/{id}")
    public Project getProject(@Parameter(description = "ID of the searched project") @PathVariable("id") Integer projectID) {
        Project project = projectDatabase.getProject(projectID);
        if (project != null)
            return project;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Project with ID %s not found!", projectID));

    }

    /**
     * REST METHOD DELETE PROJECT
     * <p>
     * precondition: the given id exists in the database
     * postcondition: project no longer exists in the database and the project is returned
     *
     * @param projectID - id of the project to be deleted
     * @return the deleted project for testing ppurposes
     */
    @ApiResponse(responseCode = "200", description = "Project found and deleted successfully")
    @ApiResponse(responseCode = "400", description = "Project with the given ID does not exist")
    @Operation(summary = "deletes project by ID", description = "Returns the deleted project for testing purposes")

    @DeleteMapping("/projects/{id}")
    public Project deleteProject(@Parameter(description = "ID of the project to delete") @PathVariable("id") Integer projectID) {
        Project project = projectDatabase.getProject(projectID);
        if (project != null) {
            projectDatabase.deleteFromDatabase(projectID);
            return project;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Project with ID %s not found!", projectID));

    }

    /**
     * REST METHOD POST TO ADD PROJECT
     * <p>
     * precondition: The request body corresponds to the Project Class
     * postcondition: The given project is added to the database
     *
     * @return ID of the added project for testing purposes
     */

    @Operation(summary = "Adds a new project to the database", description = "After adding the project" +
            " returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public int addProject(@Parameter(description = "From the automatically JSON parsed Request Body a project will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create a project ") @Valid @RequestBody Project requestBody) {
        try {
            return projectDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE PROJECT
     * <p>
     * precondition: The request body corresponds to the Project Class and the id exists in the database
     * postcondition: The correct project has been updated
     */
    @Operation(summary = "Updates project by ID", description = "modifies the data of the given project")
    @PutMapping("/projects")
    public void updateProject(@Parameter(description = "Contains the ID of the project through which the respective project will be recognised ") @Valid @RequestBody Project requestBody) {
        projectDatabase.modifyProjectData(requestBody);

    }
}
