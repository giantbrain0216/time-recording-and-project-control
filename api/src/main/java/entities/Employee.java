package entities;

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

    @DatabaseField(columnName = "ID of the Employee",id=true)
    private Integer employeeID;

    @Size(min = 1, max = 40)
    @NotNull
    @DatabaseField(columnName = "Name")
    private String name;

    @NotNull
    @Size(min = 1, max = 40)
    @DatabaseField(columnName = "Domicile")
    private String domicile;

    @DatabaseField(columnName = "Competences")
    private String competences;

    @DatabaseField(columnName = "Working hours per week")
    @NotNull
    private Integer workingHoursPerWeek;

    @DatabaseField(columnName = "Remaining working hours per week")
    private Integer stillRemainingWorkingHoursPerWeek;


    public Employee() {}

    public Employee(String name, String domicile, String competences, Integer workingHoursPerWeek) {
         this.name = name;
        this.domicile = domicile;
        this.workingHoursPerWeek = (workingHoursPerWeek);
        this.competences = competences.toUpperCase();
        this.stillRemainingWorkingHoursPerWeek = (0);
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getCompetences() {
        return competences;
    }

    public void addCompetence(String competence) {
        if (!this.competences.equals("")) {
            this.competences += "-" + competence.toUpperCase();
        } else {
            this.competences = competence.toUpperCase();
        }

    }

    /**
     * a competence will be removed from the list of competences of the employee.
     * If this competence the first saved competence and the employee has other
     * competences, then both the competence and the delimiter symbol after it will be removed.
     * if the employee has many competences then both the competence and the delimiter
     * symbol before it will be removed, else the employee has only this competence and
     * after removing it he will have no qualities more
     *
     * @param competenceToRemove competence to be removed from the list
     *                           of competences of the employee
     */
    public void removeCompetence(String competenceToRemove) {
        competenceToRemove = competenceToRemove.toUpperCase();
        if (!this.competences.contains(competenceToRemove)) {
            System.out.println("This employee does not have this competence");
        }

        if (this.competences.contains(competenceToRemove + "-")) {
            this.competences = this.competences.replace(competenceToRemove + "-", "");
        } else if (this.competences.contains("-" + competenceToRemove)) {
            this.competences = this.competences.replace("-" + competenceToRemove, "");
        } else {
            this.competences = "";
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", name='" + name + '\'' +
                ", domicile='" + domicile + '\'' +
                ", competences='" + competences + '\'' +
                ", workingHoursPerWeek=" + workingHoursPerWeek +
                ", stillRemainingWorkingHoursPerWeek=" + stillRemainingWorkingHoursPerWeek +
                '}';
    }

    public void setID(int id) {
        this.employeeID = id;
    }

    public Integer getWorkingHoursPerWeek() {
        return workingHoursPerWeek;
    }

    public void setWorkingHoursPerWeek(Integer workingHoursPerWeek) {
        this.workingHoursPerWeek = workingHoursPerWeek;
    }

    public Integer getStillRemainingWorkingHoursPerWeek() {
        return stillRemainingWorkingHoursPerWeek;
    }

    public void setStillRemainingWorkingHoursPerWeek(Integer stillRemainingWorkingHoursPerWeek) {
        this.stillRemainingWorkingHoursPerWeek = stillRemainingWorkingHoursPerWeek;
    }
}
