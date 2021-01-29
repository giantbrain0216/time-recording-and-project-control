package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.EmployeeDatabase;
import entities.Employee;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static properties.Properties.*;

@Tag(name = "Employees", description = "All implemented operations to manage employees")
@RestController
public class EmployeeController {

    private EmployeeDatabase employeeDatabase;


    public EmployeeController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            employeeDatabase = new EmployeeDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL EMPLOYEES
     *
     * @return List of all Employees
     */

    @ApiResponse(responseCode = "200", description = "Employees found and returned successfully")
    @Operation(summary = "Get all employees", description = "Returns a list of all existing employees in the database")
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeDatabase.getAllEmployees();
    }

    /**
     * REST METHOD GET FOR ONE EMPLOYEES
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct employee is returned
     *
     * @param employeeID - The ID of the employee
     * @return Employee corresponding to the id
     */

    @ApiResponse(responseCode = "200", description = "Employee found successfully")
    @ApiResponse(responseCode = "404", description = "Employee with the given ID does not exist")
    @Operation(summary = "Finds employee by ID", description = "Returns a single employee whose ID was given")
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@Parameter(description = "ID of the searched employee") @PathVariable("id") Integer employeeID) {

            return employeeDatabase.getEmployee(employeeID);

    }

    /**
     * REST METHOD DELETE EMPLOYEE
     * <p>
     * precondition: the given id exists in the database
     * postcondition: employee no longer exists in the database and the employee is returned
     *
     * @param employeeID - id of the employee to be deleted
     * @return the deleted employee for testing purposes
     */

    @ApiResponse(responseCode = "200", description = "Employee found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "Employee with the given ID does not exist")
    @Operation(summary = "deletes employee by ID", description = "Returns the deleted employee for testing purposes")
    @DeleteMapping("/employees/{id}")
    public Employee deleteEmployee(@Parameter(description = "ID of the employee to delete") @PathVariable("id") Integer employeeID) {
        Employee employee = employeeDatabase.getEmployee(employeeID);

            employeeDatabase.deleteFromDatabase(employeeID);
            return employee;


    }

    /**
     * REST METHOD POST EMPLOYEE TO ADD EMPLOYEE
     * <p>
     * precondition: The request body corresponds to the Employee Class
     * postcondition: The given employee is added to the database
     *
     * @return ID of the new added employee
     */
    @Operation(summary = "Adds a new Employee to the database", description = "After adding the employee returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public int addEmployee(@Parameter(description = "From the automatically JSON parsed Request Body an employee will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create an employee ")@Valid @RequestBody Employee requestBody) {
        try {
            return employeeDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE EMPLOYEE
     * <p>
     * precondition: The request body corresponds to the Employee Class and the id exists in the database
     * postcondition: The correct employee has been updated
     */
    @Operation(summary = "Updates employee by ID", description = "modifies the data of the given employee")
    @PutMapping("/employees")
    public void updateEmployee(@Parameter(description = "Contains the ID of the employee through which the respective employee will be recognised ") @Valid @RequestBody Employee requestBody) {
        employeeDatabase.modifyEmployeeData(requestBody);

    }
}


