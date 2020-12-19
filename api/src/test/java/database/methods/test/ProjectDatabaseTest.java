package database.methods.test;

import controllers.Application;
import database.methods.ProjectDatabase;
import entities.Employee;
import entities.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


/**
 * Note: after each test or test method, the database is reset, i.e.
 * the changes to the database during the tests are abolished so that
 * the tests always run successfully.
 */
public class ProjectDatabaseTest {
    Application application;
    ProjectDatabase projectDatabase;


    @Before
    public void setUp() throws SQLException {
        application = new Application();
        projectDatabase = new ProjectDatabase(application.connectionSource);
    }

    /**
     * This method tests whether an object can be successfully created in the
     * database by checking after the insertion whether the size of the list has
     * increased by 1 and whether the data of the added project is stored in the
     * correct columns.
     *
     * @throws SQLException if the project has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Project project = new Project("Project Test",21, new Date(11 - 11 - 2020),
                new Date(2020 - 11 - 11), 100,  "HTML-CSS");
        projectDatabase.addToDatabase(project);

        Assert.assertTrue(projectDatabase.getAllProjects().contains(project));
        System.out.println(project.getProjectNumber());
        projectDatabase.deleteFromDatabase(project.getProjectNumber());
        Assert.assertFalse( projectDatabase.getAllProjects().contains(project));

    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether the size of the list has
     * decreased by 1.
     *
     * @throws SQLException if the project has not been deleted
     */
    @Test
    public void deleteProjectTest() throws SQLException {
        assertEquals(1, projectDatabase.getAllProjects().size());
        projectDatabase.deleteFromDatabase(1);
        assertEquals(0, projectDatabase.getAllProjects().size());
        projectDatabase.addToDatabase(new Project("Project Test",21, new Date(11 - 11 - 2020),
                new Date(2020 - 11 - 11), 268,  "HTML"));
    }
    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyProjectDataTest(){
        Project project = projectDatabase.getProject(1);
        Assert.assertEquals("HTML",project.getCompetences());
        Assert.assertEquals(14  , project.getPerformedEffort());
        project.addCompetence("ANGULAR");
        project.setPerformedEffort(90);
        projectDatabase.modifyProjectData(project);
        Assert.assertEquals("HTML-ANGULAR",project.getCompetences());
        Assert.assertEquals(90  , project.getPerformedEffort());
        project.setPerformedEffort(14);
        project.removeCompetence("ANGULAR");
        projectDatabase.modifyProjectData(project);

    }
}
