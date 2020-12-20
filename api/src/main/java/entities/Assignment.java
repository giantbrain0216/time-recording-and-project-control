package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

/**
 * Class represents the allocation of projects to employees.
 *
 * @version 19.12.2020
 */

@DatabaseTable(tableName = "Assignments")
public class Assignment {

    @DatabaseField(columnName = "ID", id = true)
    private Integer id;

    @DatabaseField(columnName = "ID of the Employee")
    @NotNull
    private Integer employeeID;

    @DatabaseField(columnName = "ID of the Project")
    @NotNull
    private Integer projectID;

    @DatabaseField(columnName = "Planned Working Hours")
    @NotNull
    private int plannedWorkingHours;

    public Assignment() {
    }

    public Assignment(Integer employeeID, Integer projectID, Integer workedHours) {
        this.projectID = projectID;
        this.employeeID = employeeID;
        this.plannedWorkingHours = workedHours;
    }


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


    public int getPlannedWorkingHours() {
        return plannedWorkingHours;
    }

    public void setPlannedWorkingHours(int plannedWorkingHours) {
        this.plannedWorkingHours = plannedWorkingHours;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", employeeID=" + employeeID +
                ", projectID=" + projectID +
                ", plannedWorkingHours=" + plannedWorkingHours +
                '}';
    }
}
