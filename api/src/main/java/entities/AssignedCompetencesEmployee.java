package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

@DatabaseTable(tableName = "Assigned Competences of the employees")
public class AssignedCompetencesEmployee {

    @DatabaseField(columnName = "ID", id = true)
    private Integer id;

    @DatabaseField(columnName = "Employee ID")
    @NotNull
    private Integer employeeID;

    @DatabaseField(columnName = "Competence ID")
    private Integer competenceID;


    public AssignedCompetencesEmployee() {
    }

    public AssignedCompetencesEmployee(Integer employeeID, Integer competences) {
        this.employeeID = employeeID;
        this.competenceID = competences;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer projectID) {
        this.employeeID = projectID;
    }

    public int getCompetenceID() {
        return competenceID;
    }

    public void setCompetenceID(Integer competenceID) {
        this.competenceID = competenceID;
    }

}
