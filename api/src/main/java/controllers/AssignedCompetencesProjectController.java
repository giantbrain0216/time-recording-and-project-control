package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignedCompetencesProjectDatabase;
import database.methods.ClientDatabase;
import entities.AssignedCompetencesProject;
import entities.Client;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

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
    @GetMapping("/assignedCompetencesProject/{id}")
    public AssignedCompetencesProject getAssignedCompetencesProject(@PathVariable("id") Integer assignmentID) {
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
    public AssignedCompetencesProject deleteAssignedCompetencesProject(@PathVariable("id") Integer assignmentID) {
        AssignedCompetencesProject AssignedCompetencesProject = assignedCompetencesProjectDatabase.getAssignedCompetencesProject(assignmentID);
        assignedCompetencesProjectDatabase.deleteFromDatabase(assignmentID);
        return AssignedCompetencesProject;
    }

    /**
     * REST METHOD POST TO ADD AssignedCompetencesProject
     * <p>
     * precondition: The request body corresponds to the AssignedCompetencesProject Class
     * postcondition: The given client is added to the database
     *
     * @return ID of the added AssignedCompetencesProject
     */
    @PostMapping("/assignedCompetencesProject")
    @ResponseStatus(HttpStatus.CREATED)
    public int addClient(@Valid @RequestBody AssignedCompetencesProject requestBody) {
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
    @PutMapping("/assignedCompetencesProject")
    public void updateClient(@Valid @RequestBody AssignedCompetencesProject requestBody) {
        assignedCompetencesProjectDatabase.modifyAssignedCompetencesProject(requestBody);

    }
}

