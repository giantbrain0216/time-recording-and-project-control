package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import entities.Project;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manipulate and manage the database of projects
 *
 * @version 06.12.2020
 */
public class ProjectDatabase {

    private Dao<Project, Integer> projectDao;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of projects in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the element cannot be created.
     */
    public ProjectDatabase(ConnectionSource connectionSource) throws SQLException {
        projectDao = DaoManager.createDao(connectionSource, Project.class);
        TableUtils.createTableIfNotExists(connectionSource, Project.class);
    }

    /**
     * Adds the given employee to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same number
     * as the given project's
     * Postconditions : The project has been added to the database. Otherwise
     * an exception will be thrown in the case another projets with the same number
     * occurs in the table.
     *
     * @param projectToAdd must not be null and his number must be unique
     * @throws SQLException if the employee cannot be added.
     */
    public void addToDatabase(Project projectToAdd) throws SQLException {
        if (projectToAdd == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        projectDao.create(projectToAdd);
    }

    /**
     * deletes an existing project from the table in the database.
     * An error message will be printed in case there is no project
     * with the given number.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param projectNumber of the employee which is unique
     */
    public void deleteFromDatabase(Integer projectNumber) {
        if (projectNumber == null) {
            throw new NullPointerException("Please enter a valid project number");
        }
        try {
            projectDao.deleteById(projectNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There is no project with the number " + projectNumber);
        }
    }

    /**
     * modifies the data of an already existing project in the table in the
     * database.
     *
     * @param projectWithNewData with the modified and updated data
     */
    public void modifyProjectData(final Project projectWithNewData) {
        if (projectWithNewData == null) {
            throw new NullPointerException("Please enter a valid project");
        }
        try {
            projectDao.update(projectWithNewData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");
            e.printStackTrace();
        }
    }

    /**
     * searches the database and then returns the project whose number matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param projectNumber of the employee to return
     * @return project whose number matches the given one
     */
    public Project getProjects(Integer projectNumber) {
        try {
            return projectDao.queryForId(projectNumber);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return all projects stored in the database
     *
     * @return a list or an arraylist containing all employees. When the database
     * is empty, an empty arrayList will be returned.
     * <p>
     * ARRAY OR LIST ?
     * Hint : Query for all of the items in the object table. For medium sized or large tables, this may load a lot
     * of objects into memory so you should consider using the iterator() method instead.
     */
    public List<Project> getAllProjects() {
        try {
            return projectDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<>();
        }
    }

}

