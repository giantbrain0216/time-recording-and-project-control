package controllers;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ProjectDatabase;
import entities.Project;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import static properties.Properties.*;

@RestController
public class ProjectController {

    private ProjectDatabase projectDatabase;

    public ProjectController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            projectDatabase = new ProjectDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL PROJECTS
     *
     * @return List of all Projects
     */
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectDatabase.getAllProjects();
    }

    /**
     * REST METHOD GET FOR ONE PROJECT
     *
     *  precondition: the given id exists in the database
     *  postcondition: correct Project is returned
     *
     * @param employeeID - The ID of the project
     * @return the project corresponding to the id
     */
    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable("id") Integer employeeID) {
        Project project = projectDatabase.getProject(employeeID);
        return project;
    }

    /**
     * REST METHOD DELETE PROJECT
     *
     * precondition: the given id exists in the database
     * postcondition: project no longer exists in the database and the project is returned
     *
     * @param projectID - id of the project to be deleted
     * @return
     */
    @DeleteMapping("/projects/{id}")
    public Project deleteProject(@PathVariable("id") Integer projectID) {
        Project project = projectDatabase.getProject(projectID);
        projectDatabase.deleteFromDatabase(projectID);
        return project;
    }

    /**
     * REST METHOD POST TO ADD PROJECT
     *
     * precondition: The request body corresponds to the Project Class
     * postcondition: The given project is added to the database
     *
     * @return
     */
    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public int addProject(@Valid @RequestBody Project requestBody){
        try {
            return projectDatabase.addToDatabase(requestBody);
        }catch(SQLException e){
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE PROJECT
     *
     * precondition: The request body corresponds to the Project Class and the id exists in the database
     * postcondition: The correct project has been updated
     *
     * @return
     */
    @PutMapping("/projects")
    public void updateProject(@Valid @RequestBody Project requestBody){
       // requestBody.setProjectNumber(id);
        projectDatabase.modifyProjectData(requestBody);

    }
}
