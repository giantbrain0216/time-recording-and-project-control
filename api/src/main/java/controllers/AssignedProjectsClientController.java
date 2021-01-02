package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignedProjectsClientDatabase;
import entities.AssignedProjectsClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

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
    public AssignedProjectsClient getassignedProjectsClient(@PathVariable("id") Integer assignmentID) {
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
    public AssignedProjectsClient deleteassignedProjectsClient(@PathVariable("id") Integer assignmentID) {
        AssignedProjectsClient assignedProjectsClient = assignedProjectsClientDatabase.getAssignedProjectsClient(assignmentID);
        assignedProjectsClientDatabase.deleteFromDatabase(assignmentID);
        return assignedProjectsClient;
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
    @ResponseStatus(HttpStatus.CREATED)
    public int addClient(@Valid @RequestBody AssignedProjectsClient requestBody) {
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
    @PutMapping("/assignedProjectsClient")
    public void updateClient(@Valid @RequestBody AssignedProjectsClient requestBody) {
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
    public List<Integer> getProjectsByClient(@PathVariable("id") Integer clientID) {
        return assignedProjectsClientDatabase.getProjectIDs(clientID);
    }
}

