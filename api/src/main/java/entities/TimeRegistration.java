package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@DatabaseTable(tableName = "Time Registration")
public class TimeRegistration {

    @DatabaseField(columnName = "ID",id = true)
    private int id;

    @DatabaseField(columnName = "ID of Employee")
    @NotNull
    private int employeeID;

    @DatabaseField(columnName = "ID of Project")
    @NotNull
    private int projectID;

    @DatabaseField(columnName = "Start")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date start;

    @DatabaseField(columnName = "End")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date end;

    @DatabaseField(columnName = "Brief Description")
    @NotNull
    @Size(min = 1 , max = 100)
    private String description;

    public TimeRegistration(){}
    public TimeRegistration(final int employeeID, final int projectID, final Date start, final Date end, final String description) {
        this.employeeID = employeeID;
        this.projectID = projectID;
        this.start = start;
        this.end = end;
        this.description = description;
    }


    public Integer getID() {
        return id;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
