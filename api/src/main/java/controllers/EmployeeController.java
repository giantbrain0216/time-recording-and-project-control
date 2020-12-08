package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.EmployeeDatabase;
import entities.Employee;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class EmployeeController {

    private EmployeeDatabase employeeDatabase;


    public EmployeeController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                    ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
            employeeDatabase = new EmployeeDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
           }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        JSONArray employeeArray = new JSONArray();
        List<Employee> listOfEmployees = employeeDatabase.getAllEmployees();
        for(Employee employee: listOfEmployees){
            JSONObject jsonEmployee = new JSONObject();
            jsonEmployee.put("name", employee.getName());
            jsonEmployee.put("id", employee.getEmployeeID());
            jsonEmployee.put("domicile", employee.getDomicile());
            jsonEmployee.put("projects", employee.getProjectIDs());
            jsonEmployee.put("competences", employee.getCompetences());
            employeeArray.put(jsonEmployee);

        }

        return employeeDatabase.getAllEmployees();
    }

    @DeleteMapping("/employees/{id}")
    public Employee deleteEmployee(@PathVariable("id") Integer employeeID) {
        Employee employee = employeeDatabase.getEmployee(employeeID);
        employeeDatabase.deleteFromDatabase(employeeID);
        return employee;
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody Employee requestBody){
        try {
            employeeDatabase.addToDatabase(requestBody);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    @PutMapping("/employees/{id}")
    public void updateEmployee(@RequestBody Employee requestBody){
            employeeDatabase.modifyEmployeeData(requestBody);

    }
}


