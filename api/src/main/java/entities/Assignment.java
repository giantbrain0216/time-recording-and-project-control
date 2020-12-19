package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

/**
 * Class represents the allocation of projects to employees.
 * @version 19.12.2020
 */

@DatabaseTable(tableName = "Assignment")
public class Assignment {

    @DatabaseField(columnName = "ID")
    @NotNull
    private Integer id;

    @DatabaseField(columnName = "ID of the Employee")
    @NotNull
    private Integer employeeID;

    @DatabaseField(columnName = "ID of the Project")
    @NotNull
    private Integer projectID;

    @DatabaseField(columnName = "Hours worked")
    @NotNull
    private Float workedHours;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Float getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(Float workedHours) {
        this.workedHours = workedHours;
    }
}
