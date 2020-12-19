package database.methods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import controllers.Application;
import entities.Client;
import entities.Employee;
import org.springframework.boot.ApplicationArguments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to manipulate and manage the database of employees
 *
 * @version 06.12.2020
 */
public class EmployeeDatabase {
    private Dao<Employee, Integer> employeeDao;

    /**
     * creates an object from type DAO which will be used to manage the database.
     * Creates the table of employees in the data base in case it has not been
     * yet created
     *
     * @param connectionSource to set up the connection with the database
     * @throws SQLException if the element cannot be created.
     */
    public EmployeeDatabase(ConnectionSource connectionSource) throws SQLException {
        employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        TableUtils.createTableIfNotExists(connectionSource, Employee.class);
    }

    /**
     * Creates IDs in such a way that after deleting an employee, for example,
     * there are no gaps, but the ID whose employee was deleted is assigned
     * to another employee to be added.
     *
     * @return ID of the employee to be added.
     */
    private int createID() {
        List<Employee> listOfEmployees = this.getAllEmployees();
        //  sort the list by ID
        Collections.sort(listOfEmployees, Comparator.comparing(employee -> employee.getEmployeeID()));
        for (int i = 0; i < listOfEmployees.size(); i++) {
            if (listOfEmployees.get(i).getEmployeeID() != i + 1) {
                return i + 1;
            }
        }
        return listOfEmployees.size() + 1;
    }

    /**
     * Adds the given employee to the table in the database,
     * by storing all attributes in the respective cells.
     * Preconditions : in the table there is no elements with the same ID
     * as the given employee's
     * Postconditions : The employee has been added to the database. Otherwise
     * an exception will be thrown in the case another employee with the same ID
     * occurs in the table.
     *
     * @param employee must not be null and his ID must be unique
     * @return ID of the employee that has been added for testing purposes
     * @throws SQLException if the employee cannot be added.
     */
    public int addToDatabase(Employee employee) throws SQLException {
        if (employee == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        int employeeToAddID = createID();
        ;
        employee.setID(employeeToAddID);
        employeeDao.create(employee);
        return employeeToAddID;
    }

    /**
     * deletes an existing employee from the table in the database.
     * An error message will be printed in case there is no employee
     * with the given ID.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param employeeID of the employee which is unique
     */
    public void deleteFromDatabase(Integer employeeID) {
        if (employeeID == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        try {
            employeeDao.deleteById(employeeID);
        } catch (SQLException e) {
            System.out.println("There is no employees with the ID " + employeeID);
        }
    }

    /**
     * modifies the data of an already existing employee in the table in the
     * database.
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     * <p>
     * WHAT IF THE ID OF THE EMPLOYEE CHANGED AND THE NEW ONE IS LOCATED
     * FOR ANOTHER ONE?
     *
     * @param employeeWithNewData with the modified and updated data
     */
    public void modifyEmployeeData(final Employee employeeWithNewData) {
        if (employeeWithNewData == null) {
            throw new NullPointerException("Please enter a valid Employee");
        }
        try {
            employeeDao.update(employeeWithNewData);
        } catch (SQLException e) {
            System.out.println("there is no element in the database that matches the passed one. ");

        }
    }

    /**
     * searches the database and then returns the employee whose ID matches the given one
     * No exceptions are thrown, but a message is displayed on the
     * console and the execution of the method is stopped.
     *
     * @param employeeID of the employee to return
     * @return employee with the ID {@employeeID}
     */
    public Employee getEmployee(Integer employeeID) {
        try {
            return employeeDao.queryForId(employeeID);
        } catch (SQLException e) {
            System.out.println("there is no element in the database with an ID that matches the passed one. ");
            return null;
        }
    }

    /**
     * return all employees stored in the database
     *
     * @return a list or an arraylist containing all employees. When the database
     * is empty, an empty arrayList will be returned.
     * <p>
     * ARRAY OR LIST ?
     * Hint : Query for all of the items in the object table. For medium sized or large tables, this may load a lot
     * of objects into memory so you should consider using the iterator() method instead.
     */
    public List<Employee> getAllEmployees() {
        try {
            return employeeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there is no elements in the database. ");
            return new ArrayList<Employee>();
        }
    }

}
