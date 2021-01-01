package database.methods.test;

import controllers.Application;
import database.methods.CompetenceDatabase;
import entities.Competence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link CompetenceDatabase}
 */

public class CompetenceDatabaseTest {
    Application application;
    CompetenceDatabase competenceDatabase;

    @Before
    public void setUp() throws SQLException {
        application = new Application();
        competenceDatabase = new CompetenceDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the ID of the competence
     * occurs in the list of IDs of all competences
     *
     * @throws SQLException if the competence has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Competence competence = new Competence(null, "Test");
        competenceDatabase.addToDatabase(competence);
        Assert.assertTrue(competenceDatabase.getAllCompetences().stream().anyMatch(competence1 -> competence1.getId() == (competence.getId())));
        competenceDatabase.deleteFromDatabase(competence.getId());
        Assert.assertFalse(competenceDatabase.getAllCompetences().stream().anyMatch(competence1 -> competence1.getId() == (competence.getId())));

    }

    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the competence has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyDataTest() throws SQLException {
        Competence competence = new Competence(null, "TEST");
        int id = competenceDatabase.addToDatabase(competence);
        Assert.assertEquals("TEST", competence.getName());
        competence.setName("test");
        competenceDatabase.modifyCompetence(competence);
        Assert.assertEquals("test", competenceDatabase.getCompetence(id).getName());
        competenceDatabase.deleteFromDatabase(id);
    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion the deleted element in the list
     * of all competences still exists.
     *
     * @throws SQLException if the competence has not been deleted
     */
    @Test
    public void deleteFromDatabaseTest() throws SQLException {
        Competence competence = new Competence(null, "Test");
        competenceDatabase.addToDatabase(competence);
        Assert.assertTrue(competenceDatabase.getAllCompetences().stream().anyMatch(competence1 -> competence1.getId() == (competence.getId())));
        competenceDatabase.deleteFromDatabase(competence.getId());
        Assert.assertFalse(competenceDatabase.getAllCompetences().stream().anyMatch(competence1 -> competence1.getId() == (competence.getId())));


    }
}


