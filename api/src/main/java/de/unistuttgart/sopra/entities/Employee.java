package de.unistuttgart.sopra.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class represents a worker in the itestra society, which is used to add
 * and remove employees from the database and edit them.
 * <p>
 * The database is accessed via OR Mapper
 */

@DatabaseTable(tableName = "Employees")
public class Employee {

    @DatabaseField(columnName = "ID of the Employee", id = true)
    private Integer employeeID;

    @Size(min = 1, max = 40)
    @NotNull
    @DatabaseField(columnName = "Name")
    private String name;

    @NotNull
    @Size(min = 1, max = 40)
    @DatabaseField(columnName = "Domicile")
    private String domicile;


    @DatabaseField(columnName = "Working hours per week")
    @NotNull
    private Integer workingHoursPerWeek;

    @DatabaseField(columnName = "Remaining working hours per week")
    private Integer remainingWorkingHoursPerWeek;


    public Employee() {
    }

    public Employee(String name, String domicile, Integer workingHoursPerWeek) {
        this.setName(name);
        this.setDomicile(domicile);
        this.setWorkingHoursPerWeek(workingHoursPerWeek);
        this.setRemainingWorkingHoursPerWeek(workingHoursPerWeek);
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public Integer getWorkingHoursPerWeek() {
        return workingHoursPerWeek;
    }

    public void setWorkingHoursPerWeek(Integer workingHoursPerWeek) {
        this.workingHoursPerWeek = workingHoursPerWeek;
    }

    public Integer getRemainingWorkingHoursPerWeek() {
        return remainingWorkingHoursPerWeek;
    }

    public void setRemainingWorkingHoursPerWeek(Integer remainingWorkingHoursPerWeek) {
        this.remainingWorkingHoursPerWeek = remainingWorkingHoursPerWeek;
    }
}