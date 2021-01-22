package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * Class representing a project which will be performed from the
 * itestra society. It belongs one client and has number, planned start ,
 * planned end, planned effort, performed effort, competences that are
 * needed to achieve it and technologies.
 *
 * @version 16.12.2020
 */

@DatabaseTable(tableName = "Projects")
public class Project {

    @DatabaseField(columnName = "Project number", id = true)
    private Integer projectNumber;

    @DatabaseField(columnName = "Project Name")
    @NotNull
    private String projectName;

    @DatabaseField(columnName = "Planned Start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date plannedStart;

    @DatabaseField(columnName = "Planned End")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date plannedEnd;

    @DatabaseField(columnName = "Planned Effort")
    private Integer plannedEffort;


    @DatabaseField(columnName = "Performed Effort")
    private float performedEffort;

    @DatabaseField(columnName = "Price Per Hour")
    private double pricePerHour;

    public Project() {
    }

    /**
     * Constructor to create an object from type Project who belongs a
     * {@link Client} and should be performed from the society.
     *
     * @param projectName   Name of the project
     * @param plannedStart  date of start
     * @param plannedEnd    date of end
     * @param plannedEffort effort to achieve
     * @param pricePerHour  the price that should be paid per Hour
     */
    public Project(final String projectName, final Date plannedStart,
                   final Date plannedEnd, final Integer plannedEffort, final double pricePerHour) {
        this.setProjectName(projectName);
        this.setPerformedEffort(0);
        this.setPlannedStart(plannedStart);
        this.setPlannedEffort(plannedEffort);
        this.setPlannedEnd(plannedEnd);
        this.setPricePerHour(pricePerHour);
    }

    public Integer getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public int getPlannedEffort() {
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

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}