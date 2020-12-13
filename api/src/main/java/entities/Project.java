package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.sql.Date;

/**
 * Class representing a project which will be performed from the
 * itestra society. It belongs one client and has number, planned start ,
 * planned end, planned effort, performed effort, competences that are
 * needed to achieve it and technologies.
 *
 * @version 08.12.2020
 */

@DatabaseTable(tableName = "projects")
public class Project {

    @DatabaseField(columnName = "Project number", id=true)
    private Integer projectNumber;

    @DatabaseField(columnName = "ID of the Client")
    @NotNull
    private Integer clientID;

    @DatabaseField(columnName = "planned Start")
    private Date plannedStart;

    @DatabaseField(columnName = "planned End")
    private Date plannedEnd;

    @DatabaseField(columnName = "planned Effort")
    private Integer plannedEffort;


    @DatabaseField(columnName = "performed Effort")
    private Integer performedEffort;

    @DatabaseField(columnName = "Needed Competences")
    private String competences;

    public Project() {
    }

    /**
     * Constructor to create an object from type Project who belongs a
     * {@link Client} and should be performed from the society.
     *
     * @param clientID        the owner of this project
     * @param plannedStart    date of start
     * @param plannedEnd      date of end
     * @param plannedEffort   effort to achieve
     * @param performedEffort effort that has been achieved
     * @param competences     needed to achieve this project
     */
    public Project(final Integer clientID, final Date plannedStart,
                   final Date plannedEnd, final Integer plannedEffort, final Integer performedEffort, final String competences) {
        this.clientID = clientID;
        this.competences = competences;
        this.performedEffort = performedEffort;
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

    public Integer getPerformedEffort() {
        return performedEffort;
    }

    public void setPerformedEffort(Integer performedEffort) {
        this.performedEffort = performedEffort;
    }

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }
}
