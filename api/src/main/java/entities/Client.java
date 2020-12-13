package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class representing a client which can have projects that will
 * be performed from the itestra society. A client has a name, ID
 * email, telephone number, projects and an employee from the
 * society as a contact person.
 *
 * @version 08.12.2020
 */
@DatabaseTable(tableName = "clients")
public class Client {

    @DatabaseField
    @NotNull
    private int clientID;

    @DatabaseField(columnName = "Name")
    @NotNull
    private String name;

    @DatabaseField(columnName = "E-Mail Address")
    @Size(min = 5, max = 25)
    @NotNull
    private String email;

    @DatabaseField(columnName = "Phone Number")
    @Size(min = 8, max = 40)
    @NotNull
    private String telephoneNumber;

    @DatabaseField(columnName = "ID of the contact person")
    private Integer contactPersonID;

    @DatabaseField(columnName = "IDs of the projects")
    private String projectIDs;

    public Client() {}

    /**
     * Constructor to create an object from type Client who can have
     * {@link Project} within the society.
     *
     * @param name          of the client
     * @param email         to communicate with him. It will be verified before accepting it
     * @param telephoneNumber to contact him
     * @param contactPersonID ID of the  {@link Employee} who will communicate with the client
     * @param projectIDs    which the client has in the society. It will be verified
     *                      because it must match the format "id-id-id-....-id"
     */
    public Client(final String name, final String email, final String telephoneNumber,
                  final Integer contactPersonID, final String projectIDs) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("The E-Mail is invalid ! ");
        }
        if (!projectIDs.isEmpty()&&projectIDs.length()==1) {
            if (StringUtils.countMatches(projectIDs, "-") != projectIDs.length() / 2) {
                throw new IllegalArgumentException("The projectIDs does not match the format! The projectIDs must be " +
                        "seperated through the delimiter -");
            }
        }
        this.name = name;
        this.contactPersonID = contactPersonID;
        this.email = email;
        this.projectIDs = projectIDs;
        this.telephoneNumber = telephoneNumber;
    }

    public Integer getClientID() {
        return clientID;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public int getIDContactPerson() {
        return contactPersonID;
    }

    public void setContactPerson(Integer contactPersonID) {
        this.contactPersonID = contactPersonID;
    }

    public String getProjectIDs() {
        return projectIDs;
    }

    public void addProject(Integer projectID) {
        if (this.projectIDs.equals("")) {
            this.projectIDs = projectID.toString();
        } else {
            this.projectIDs += "-" + projectID;
        }
    }

    /**
     * a project ID  will be removed from the list of projects of the clients.
     * If this competence the first saved competence and the employee has other
     * competences, then both the competence and the delimiter symbol after it will be removed.
     * if the employee has many competences then both the competence and the delimiter
     * symbol before it will be removed, else the employee has only this competence and
     * after removing it he will have no qualities more
     * <p>
     * IMPORTANT : WE MUST CHECK WHETHER THE PROJECT CAN BE DELETED
     *
     * @param projectIDToRemove competence to be removed from the list
     *                          of competences of the employee
     */
    public void deleteProject(Integer projectIDToRemove) {
         if (!this.projectIDs.contains(projectIDToRemove.toString())) {
            System.out.println("This client does not have this project");
        }

        if (this.projectIDs.contains(projectIDToRemove + "-")) {
            this.projectIDs = this.projectIDs.replace(projectIDToRemove + "-", "");
        } else if (this.projectIDs.contains("-" + projectIDToRemove)) {
            this.projectIDs = this.projectIDs.replace("-" + projectIDToRemove, "");
        } else {
            this.projectIDs = "";
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID=" + clientID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", telefonNumber='" + telephoneNumber + '\'' +
                ", contactPersonID='" + contactPersonID + '\'' +
                ", projectIDs='" + projectIDs + '\'' +
                '}' +"\n";
    }

    public void setID(int id) {
        this.clientID = id;
    }
}
