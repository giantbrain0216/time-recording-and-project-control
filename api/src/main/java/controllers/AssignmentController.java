package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.AssignmentDatabase;
import database.methods.ProjectDatabase;
import entities.Assignment;
import entities.Project;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AssignmentController {

    private AssignmentDatabase assignmentDatabase;

    public AssignmentController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                    ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
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





}
