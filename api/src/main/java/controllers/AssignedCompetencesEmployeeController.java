package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignedCompetencesEmployeeDatabase;
import entities.AssignedCompetencesEmployee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

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
    @GetMapping("/assignedCompetencesEmployee")
    public List<AssignedCompetencesEmployee> getassignedCompetencesEmployee() {
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
    @GetMapping("/assignedCompetencesEmployee/{id}")
    public AssignedCompetencesEmployee getassignedCompetencesEmployee(@PathVariable("id") Integer assignmentID) {
        AssignedCompetencesEmployee assignedCompetencesEmployee = assignedCompetencesEmployeeDatabase.getAssignedCompetencesEmployee(assignmentID);
        return assignedCompetencesEmployee;
    }

    /**
     * REST METHOD DELETE assignedCompetencesEmployee
     * <p>
     * precondition: the given id exists in the database
     * postcondition: assignment no longer exists in the database and the client is returned
     *
     * @param assignmentID - id of the assignment to be deleted
     */
    @DeleteMapping("/assignedCompetencesEmployee/{id}")
    public AssignedCompetencesEmployee deleteAssignedCompetencesEmployee(@PathVariable("id") Integer assignmentID) {
        AssignedCompetencesEmployee assignedCompetencesEmployee = assignedCompetencesEmployeeDatabase
                .getAssignedCompetencesEmployee(assignmentID);
        assignedCompetencesEmployeeDatabase.deleteFromDatabase(assignmentID);
        return assignedCompetencesEmployee;
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
    @ResponseStatus(HttpStatus.CREATED)
    public int addAssignment(@Valid @RequestBody AssignedCompetencesEmployee requestBody) {
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
    @PutMapping("/assignedCompetencesEmployee")
    public void updateClient(@Valid @RequestBody AssignedCompetencesEmployee requestBody) {
        assignedCompetencesEmployeeDatabase.modifyAssignedCompetencesEmployee(requestBody);
    }

    /**
     * REST METHOD GET FOR ASSIGNMENTS BY PROJECT
     * <p>
     * precondition: the given id exists in the database of assignedCompetencesEmployee
     * postcondition: correct list of competences is returned
     *
     * @param projectID - The ID of the project
     * @return the competences corresponding to the project id
     */
    @GetMapping("/competencesByEmployee/{id}")
    public List<Integer> getCompetencesByEmployee(@PathVariable("id") Integer projectID) {
        return assignedCompetencesEmployeeDatabase.getCompetences(projectID);
    }
}


