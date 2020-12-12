package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ProjectDatabase;
import database.methods.TimeRegistrationDatabase;
import entities.Project;
import entities.TimeRegistration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

@RestController
public class TimeRegistrationController {

    private TimeRegistrationDatabase timeregistrationDatabase;

    public TimeRegistrationController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                    ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
            timeregistrationDatabase = new TimeRegistrationDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL TIME REGISTRATIONS
     *
     * @return List of all Time Registrations
     */
    @GetMapping("/timeregistrations")
    public List<TimeRegistration> getProjects() {
        return timeregistrationDatabase.getAllTimeRegistrations();
    }

    /**
     * REST METHOD GET FOR ONE TIME REGISTRATION
     *
     *  precondition: the given id exists in the database
     *  postcondition: correct Time Registration is returned
     *
     * @param timeregistrationID - The ID of the time registration
     * @return the time registration corresponding to the id
     */
    @GetMapping("/timeregistrations/{id}")
    public TimeRegistration getTimeRegistration(@PathVariable("id") Integer timeregistrationID) {
        TimeRegistration timeRegistration = timeregistrationDatabase.getTimeRegistration(timeregistrationID);
        return timeRegistration;
    }

    /**
     * REST METHOD DELETE TIME REGISTRATION
     *
     * precondition: the given id exists in the database
     * postcondition: time registration no longer exists in the database and the registration is returned
     *
     * @param timeregistrationID - id of the time registration to be deleted
     * @return
     */
    @DeleteMapping("/timeregistrations/{id}")
    public TimeRegistration deleteTimeRegistration(@PathVariable("id") Integer timeregistrationID) {
        TimeRegistration timeRegistration = timeregistrationDatabase.getTimeRegistration(timeregistrationID);
        timeregistrationDatabase.deleteFromDatabase(timeregistrationID);
        return timeRegistration;
    }

    /**
     * REST METHOD POST TO ADD TIME REGISTRATION
     *
     * precondition: The request body corresponds to the TimeRegistration Class
     * postcondition: The given Time Registration is added to the database
     *
     * @return
     */
    @PostMapping("/timeregistrations")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTimeRegistration(@Valid @RequestBody TimeRegistration timeRegistration){
            timeregistrationDatabase.addToDataBase(timeRegistration);
    }

    /**
     * REST METHOD UPDATE TIME REGISTRATION
     *
     * precondition: The request body corresponds to the TimeRegistration Class and the id exists in the database
     * postcondition: The correct time registration has been updated
     *
     * @return
     */
    @PutMapping("/timeregistrations")
    public void updateTimeRegistration(@Valid @RequestBody TimeRegistration requestBody){
        timeregistrationDatabase.modifyTimeRegistrationData(requestBody);
    }
}
