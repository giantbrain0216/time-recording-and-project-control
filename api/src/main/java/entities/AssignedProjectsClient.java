package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

@DatabaseTable(tableName = "Assigned Projects to the Clients")
public class AssignedProjectsClient {

    @DatabaseField(columnName = "ID", id = true)
    private Integer id;


    @DatabaseField(columnName = "Client ID")
    @NotNull
    private Integer clientID;

    @DatabaseField(columnName = "Project ID")
    @NotNull
    private Integer projectID;

    public AssignedProjectsClient() {
    }

    public AssignedProjectsClient(Integer clientID, Integer projectID) {
        this.projectID = projectID;
        this.clientID = clientID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }
}
