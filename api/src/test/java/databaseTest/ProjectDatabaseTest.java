package databaseTest;

import de.unistuttgart.sopra.Application;
import de.unistuttgart.sopra.databse.ProjectDatabase;
import de.unistuttgart.sopra.entities.Project;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


/**
 * The objects created for testing are removed from the database immediately after the test.
 * Class that tests all functionalities of the class {@link ProjectDatabase}
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
     * database by checking after the insertion whether the  id of the added
     * project is in the list of all existing projects
     *
     * @throws SQLException if the project has not been added
     */
    @Test
    public void addToDatabaseTest() throws SQLException {
        Project project = new Project("Project Test", new Date(11 - 11 - 2020),
                new Date(2020 - 11 - 11), 100,20);
        projectDatabase.addToDatabase(project);
        Assert.assertTrue(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));
        projectDatabase.deleteFromDatabase(project.getProjectNumber());
        Assert.assertFalse(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));



    }

    /**
     * This method tests whether an object can be successfully deleted from the
     * database by checking after the deletion whether its id does not belong
     * to he ids of the existing projects
     *
     * @throws SQLException if the project has not been deleted
     */
    @Test
    public void deleteProjectTest() throws SQLException {
        Project project = new Project("Project Test", new Date(11 - 11 - 2020),
                new Date(2020 - 11 - 11), 100,40);
        projectDatabase.addToDatabase(project);
        Assert.assertTrue(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));
        projectDatabase.deleteFromDatabase(project.getProjectNumber());
        Assert.assertFalse(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));

    }
    /**
     * This method tests whether an object can be successfully modified in the
     * database by checking after the modification whether the new data of the employee has
     * been saved in the database and the old one has been overwritten.
     */
    @Test
    public void modifyProjectDataTest() throws SQLException {
        Project project = new Project("Project Test", new Date(11 - 11 - 2020),
                new Date(2020 - 11 - 11), 100,15.25);
        projectDatabase.addToDatabase(project);
        Assert.assertTrue(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));

        Assert.assertEquals(100,project.getPlannedEffort());
        Assert.assertEquals(15.25,project.getPricePerHour(),0.0);
        project.setPlannedEffort(250);
        project.setPricePerHour(30.75);
        projectDatabase.modifyProjectData(project);
        Assert.assertEquals(250,project.getPlannedEffort());
        Assert.assertEquals(30.75,project.getPricePerHour(),0.0);
        projectDatabase.deleteFromDatabase(project.getProjectNumber());
        Assert.assertFalse(projectDatabase.getAllProjects().stream().anyMatch(project1 -> project1.getProjectNumber().equals(project.getProjectNumber())));

    }
}
