package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.EmployeeDatabase;
import entities.Employee;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import static properties.Properties.*;

@RestController
public class EmployeeController {

    private EmployeeDatabase employeeDatabase;


    public EmployeeController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            employeeDatabase = new EmployeeDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
           }

    /**
     * REST METHOD GET FOR ALL EMPLOYEES
     *
     * @return List of all Employees
     */
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeDatabase.getAllEmployees();
    }

    /**
     * REST METHOD GET FOR ONE EMPLOYEES
     *
     *  precondition: the given id exists in the database
     *  postcondition: correct employee is returned
     *
     * @param employeeID - The ID of the employee
     * @return  Employee corresponding to the id
     */
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable("id") Integer employeeID) {
        Employee employee = employeeDatabase.getEmployee(employeeID);
        return employee;
    }

    /**
     * REST METHOD DELETE EMPLOYEE
     *
     * precondition: the given id exists in the database
     * postcondition: employee no longer exists in the database and the employee is returned
     *
     * @param employeeID - id of the employee to be deleted
     * @return
     */
    @DeleteMapping("/employees/{id}")
    public Employee deleteEmployee(@PathVariable("id") Integer employeeID) {
        Employee employee = employeeDatabase.getEmployee(employeeID);
        employeeDatabase.deleteFromDatabase(employeeID);
        return employee;
    }

    /**
     * REST METHOD POST EMPLOYEE TO ADD EMPLOYEE
     *
     * precondition: The request body corresponds to the Employee Class
     * postcondition: The given employee is added to the database
     *
     * @return
     */
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public int addEmployee(@Valid @RequestBody Employee requestBody){
        try {
            return employeeDatabase.addToDatabase(requestBody);
        }catch(SQLException e){
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE EMPLOYEE
     *
     * precondition: The request body corresponds to the Employee Class and the id exists in the database
     * postcondition: The correct employee has been updated
     *
     * @return
     */
    @PutMapping("/employees")
    public void updateEmployee(@Valid @RequestBody Employee requestBody){
            employeeDatabase.modifyEmployeeData(requestBody);

    }
}


