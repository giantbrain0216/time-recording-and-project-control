package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import controllers.Application;
import database.methods.ClientDatabase;
import database.methods.EmployeeDatabase;
import database.methods.ProjectDatabase;

import javax.validation.constraints.NotNull;
import java.awt.desktop.AppForegroundListener;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Class representing a project which will be performed from the
 * itestra society. It belongs one client and has number, planned start ,
 * planned end, planned effort, performed effort, competences that are
 * needed to achieve it and technologies.
 *
 * @version 16.12.2020
 */

@DatabaseTable(tableName = "projects")
public class Project {

    @DatabaseField(columnName = "Project number", id = true)
    private Integer projectNumber;

    @DatabaseField(columnName = "Project name")
    @NotNull
    private String projectName;

    @DatabaseField(columnName = "ID of the Client")
    @NotNull
    private Integer clientID;

    @DatabaseField(columnName = "planned Start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date plannedStart;

    @DatabaseField(columnName = "planned End")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date plannedEnd;

    @DatabaseField(columnName = "planned Effort")
    private Integer plannedEffort;


    @DatabaseField(columnName = "performed Effort")
    private float performedEffort;

    @DatabaseField(columnName = "Needed Competences")
    private String competences;

    public Project() {
    }

    /**
     * Constructor to create an object from type Project who belongs a
     * {@link Client} and should be performed from the society.
     *
     * @param projectName     Name of the project
     * @param clientID        the owner of this project
     * @param plannedStart    date of start
     * @param plannedEnd      date of end
     * @param plannedEffort   effort to achieve
     * @param competences     needed to achieve this project
     */
    public Project(final String projectName, final Integer clientID, final Date plannedStart,
                   final Date plannedEnd, final Integer plannedEffort, final String competences) {
        this.clientID = clientID;
        this.projectName = projectName;
        this.competences = competences;
        this.performedEffort = 0;
        this.plannedStart = plannedStart;
        this.plannedEffort = plannedEffort;
        this.plannedEnd = plannedEnd;
    }

    public Integer getProjectNumber() {
        return projectNumber;
    }

    public Integer getClientID() {
        return clientID;
    }

    public Date getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(Date plannedStart) {
        this.plannedStart = plannedStart;
    }

    public Date getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(Date plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public Integer getPlannedEffort() {
        return plannedEffort;
    }

    public void setPlannedEffort(Integer plannedEffort) {
        this.plannedEffort = plannedEffort;
    }

    public float getPerformedEffort() {
        return performedEffort;
    }

    public void setPerformedEffort(float performedEffort) {
        this.performedEffort = performedEffort;
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
     * a competence will be removed from the list of competences of the project.
     * If this competence the first saved competence and the project has other
     * competences, then both the competence and the delimiter symbol after it will be removed.
     * if the project has many competences then both the competence and the delimiter
     * symbol before it will be removed, else the project has only this competence and
     * after removing it he will have no qualities more
     *
     * @param competenceToRemove competence to be removed from the list
     *                           of competences of the project
     */
    public void removeCompetence(String competenceToRemove) {
        competenceToRemove = competenceToRemove.toUpperCase();
        if (!this.competences.contains(competenceToRemove)) {
            System.out.println("This project does not have this competence");
        }

        if (this.competences.contains(competenceToRemove + "-")) {
            this.competences = this.competences.replace(competenceToRemove + "-", "");
        } else if (this.competences.contains("-" + competenceToRemove)) {
            this.competences = this.competences.replace("-" + competenceToRemove, "");
        } else {
            this.competences = "";
        }
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
