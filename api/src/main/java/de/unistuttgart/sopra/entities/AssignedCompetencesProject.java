package de.unistuttgart.sopra.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

@DatabaseTable(tableName = "Assigned Competences to the projects")
public class AssignedCompetencesProject {
    @DatabaseField(columnName = "ID", id = true)
    private Integer id;

    @DatabaseField(columnName = "Project ID")
    @NotNull
    private Integer projectID;

    @DatabaseField(columnName = "Competence ID")
    private Integer competenceID;


    public AssignedCompetencesProject() {
    }

    public AssignedCompetencesProject(Integer projectID, Integer competences) {
        this.projectID = projectID;
        this.competenceID = competences;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public int getCompetenceID() {
        return competenceID;
    }

    public void setCompetenceID(Integer competenceID) {
        this.competenceID = competenceID;
    }

}
