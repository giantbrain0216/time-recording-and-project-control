package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import database.methods.CompetenceDatabase;
import entities.Competence;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

@Tag(name = "Competences", description = "All implemented operations to manage competences")
@RestController
public class CompetenceController {
    private CompetenceDatabase competenceDatabase;

    public CompetenceController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            competenceDatabase = new CompetenceDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL competences
     *
     * @return List of all competences
     */

    @ApiResponse(responseCode = "200", description = "competences found and returned successfully")
    @Operation(summary = "Get all competences", description = "Returns a list of all existing competences in the database")
    @GetMapping("/competences")
    public List<Competence> getAllCompetences() {
        return competenceDatabase.getAllCompetences();
    }

    /**
     * REST METHOD GET FOR ONE COMPETENCE
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct competence returned
     *
     * @param competenceID - The ID of the competence
     * @return competence corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "competence found successfully")
    @ApiResponse(responseCode = "404", description = "competence with the given ID does not exist")
    @Operation(summary = "Finds competence by ID", description = "Returns a single competence whose ID was given")
    @GetMapping("/competences/{id}")
    public Competence getCompetence(@Parameter(description = "ID of the searched competence") @PathVariable("id") Integer competenceID) {
        Competence competence = competenceDatabase.getCompetence(competenceID);
        if (competence != null) {
            return competence;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Competence with ID %s not found!", competenceID));
    }

    /**
     * REST METHOD DELETE COMPETENCE
     * <p>
     * precondition: the given id exists in the database
     * postcondition: COMPETENCE no longer exists in the database and it is returned
     *
     * @param competenceID - id of the competence to be deleted
     * @return the deleted Competence
     */
    @ApiResponse(responseCode = "200", description = "competence found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "competence with the given ID does not exist")
    @Operation(summary = "deletes competence by ID", description = "Returns the deleted competence for testing purposes")
    @DeleteMapping("/competences/{id}")
    public Competence deleteCompetence(@Parameter(description = "ID of the competence to delete") @PathVariable("id") Integer competenceID) {
        Competence competence = competenceDatabase.getCompetence(competenceID);
        if (competence != null) {
            competenceDatabase.deleteFromDatabase(competenceID);
            return competence;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("competence with ID %s not found!", competenceID));
    }


    /**
     * REST METHOD POST TO ADD COMPETENCE
     * <p>
     * precondition: The request body corresponds to the Client Class
     * postcondition: The given client is added to the database
     *
     * @return ID of the added competence
     */

    @Operation(summary = "Adds a new competence to the database", description = "After adding the competence returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @PostMapping("/competences")
    @ResponseStatus(HttpStatus.CREATED)
    public int addCompetence(@Parameter(description = "From the automatically JSON parsed Request Body a competence will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create a competence ")@Valid @RequestBody Competence requestBody) {
        try {
            return competenceDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE COMPETENCE
     * <p>
     * precondition: The request body corresponds to the Competence Class and the id exists in the database
     * postcondition: The correct Competence has been updated
     */
    @Operation(summary = "Updates competence by ID", description = "modifies the data of the given competence")
    @PutMapping("/competences")
    public void updateCompetence(@Parameter(description = "Contains the ID of the competence through which the respective competence will be recognised") @Valid @RequestBody Competence requestBody) {
        competenceDatabase.modifyCompetence(requestBody);

    }
}
