package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ProjectDatabase;
import database.methods.TimeRegistrationDatabase;
import entities.Project;
import entities.TimeRegistration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import static properties.Properties.*;

@Tag(name = "Time Registrations", description = "All implemented operations to manage time registrations")
@RestController
public class TimeRegistrationController {

    private TimeRegistrationDatabase timeregistrationDatabase;

    public TimeRegistrationController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            timeregistrationDatabase = new TimeRegistrationDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL TIME REGISTRATIONS
     *
     * @return List of all Time Registrations
     */
    @ApiResponse(responseCode = "200", description = "Time Registrations found and returned successfully")
    @Operation(summary = "Get all Time Registrations", description = "Returns a list of all existing time registrations in the database")
    @GetMapping("/timeregistrations")
    public List<TimeRegistration> getTimeRegistrations() {
        return timeregistrationDatabase.getAllTimeRegistrations();
    }

    /**
     * REST METHOD GET FOR ALL TIME REGISTRATIONS OF ONE EMPLOYEE
     *
     * @param employeeID whose time registrations should be searched
     * @return List of all Time Registrations of the employee
     */
    @ApiResponse(responseCode = "200", description = "Time Registrations for the employee whose ID was given are  found successfully")
    @ApiResponse(responseCode = "404", description = "The Database does not contain any Time Registrations for the employee whose ID was given ")
    @Operation(summary = "Finds Time Registrations for the employee whose ID was given", description = "Returns a list of the time registrations of the employee whose ID was given")
    @GetMapping("/timeregistrationsEmployee/{id}")
    public List<TimeRegistration> getTimeRegistrationsEmployee(@Parameter(description = "ID of the employee whose time registrations are being searched") @PathVariable("id") Integer employeeID) {
        List<TimeRegistration> timeRegistrationsOfEmployee = timeregistrationDatabase.getTimeRegistrationsOfEmployee(employeeID);

            return timeRegistrationsOfEmployee;
    }


    /**
     * REST METHOD DELETE FOR ALL TIME REGISTRATIONS OF ONE EMPLOYEE
     *
     * @param employeeID whose time registrations should be searched
     */
    @ApiResponse(responseCode = "200", description = "Time Registrations for the employee whose ID was given are  found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "The Database does not contain any Time Registrations for the employee whose ID was given. Therefore nothing will be deleted ")
    @Operation(summary = "Deletes Time Registrations for the employee whose ID was given", description = "Deletes all time registrations of the employee whose ID was given")
    @DeleteMapping("/timeregistrationsEmployee/{id}")
    public void deleteTimeRegistrationsEmployee(@Parameter(description = "ID of the employee whose time registrations should be deleted") @PathVariable("id") Integer employeeID) throws SQLException {
         timeregistrationDatabase.deleteTimeRegistrationsOfEmployee(employeeID);


    }

    /**
     * REST METHOD GET FOR ONE TIME REGISTRATION
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct Time Registration is returned
     *
     * @param timeregistrationID - The ID of the time registration
     * @return the time registration corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "Time Registration found successfully")
    @ApiResponse(responseCode = "400", description = "Time Registration with the given ID does not exist")
    @Operation(summary = "Finds Time Registration by ID", description = "Returns a single Time Registration whose ID was given")
    @GetMapping("/timeregistrations/{id}")
    public TimeRegistration getTimeRegistration(@Parameter(description = "ID of the searched time registration") @PathVariable("id") Integer timeregistrationID) {
        TimeRegistration timeRegistration = timeregistrationDatabase.getTimeRegistration(timeregistrationID);

            return timeRegistration;

    }

    /**
     * REST METHOD DELETE TIME REGISTRATION
     * <p>
     * precondition: the given id exists in the database
     * postcondition: time registration no longer exists in the database and the registration is returned
     *
     * @param timeregistrationID - id of the time registration to be deleted
     * @return the deleted time registration for testing purposes
     */


    @ApiResponse(responseCode = "200", description = "Time Registration  found and deleted successfully")
    @ApiResponse(responseCode = "400", description = "Time Registration  with the given ID does not exist")
    @Operation(summary = "deletes Time Registration  by ID", description = "Returns the deleted Time Registration  for testing purposes")
    @DeleteMapping("/timeregistrations/{id}")
    public TimeRegistration deleteTimeRegistration(@Parameter(description = "ID of the time registration to deleted") @PathVariable("id") Integer timeregistrationID) {
        TimeRegistration timeRegistration = timeregistrationDatabase.getTimeRegistration(timeregistrationID);

            timeregistrationDatabase.deleteFromDatabase(timeregistrationID);
            return timeRegistration;

    }

    /**
     * REST METHOD POST TO ADD TIME REGISTRATION
     * <p>
     * precondition: The request body corresponds to the TimeRegistration Class
     * postcondition: The given Time Registration is added to the database
     *
     * @return ID of the added time registration for testing purposes
     */
    @PostMapping("/timeregistrations")
    @Operation(summary = "Adds a new Time Registration  to the database", description = "After adding the Time Registration  returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @ResponseStatus(HttpStatus.CREATED)
    public int addTimeRegistration(@Parameter(description = "From the automatically JSON parsed Request Body a time registration will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create a time registration " )@Valid @RequestBody TimeRegistration timeRegistration) {
        try {
            return timeregistrationDatabase.addToDataBase(timeRegistration);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE TIME REGISTRATION
     * <p>
     * precondition: The request body corresponds to the TimeRegistration Class and the id exists in the database
     * postcondition: The correct time registration has been updated
     */
    @Operation(summary = "Updates time registration  by ID", description = "modifies the data of the given time registration ")
    @PutMapping("/timeregistrations")
    public void updateTimeRegistration(@Parameter(description = "Contains the ID of the time registration through which the respective time registration will be recognised ") @Valid @RequestBody TimeRegistration requestBody) {
        timeregistrationDatabase.modifyTimeRegistrationData(requestBody);
    }
}
