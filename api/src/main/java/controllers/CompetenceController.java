package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import database.methods.CompetenceDatabase;
import entities.Competence;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

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
    @GetMapping("/competences/{id}")
    public Competence getCompetence(@PathVariable("id") Integer competenceID) {
        return competenceDatabase.getCompetence(competenceID);
    }

    /**
     * REST METHOD DELETE COMPETENCE
     * <p>
     * precondition: the given id exists in the database
     * postcondition: COMPETENCE no longer exists in the database and it is returned
     *
     * @param competenceID - id of the competence to be deleted
     * @return
     */
    @DeleteMapping("/competences/{id}")
    public Competence deleteCompetence(@PathVariable("id") Integer competenceID) {
        Competence competence = competenceDatabase.getCompetence(competenceID);
        competenceDatabase.deleteFromDatabase(competenceID);
        return competence;
    }

    /**
     * REST METHOD POST TO ADD COMPETENCE
     * <p>
     * precondition: The request body corresponds to the Client Class
     * postcondition: The given client is added to the database
     *
     * @return
     */
    @PostMapping("/competences")
    @ResponseStatus(HttpStatus.CREATED)
    public int addCompetence(@Valid @RequestBody Competence requestBody) {
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
    @PutMapping("/competences")
    public void updateCompetence(@Valid @RequestBody Competence requestBody) {
        competenceDatabase.modifyCompetence(requestBody);

    }
}
