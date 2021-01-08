package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
@DatabaseTable(tableName = "Clients")
public class Client {

    @DatabaseField(columnName = "ID of Client", id = true)
    private Integer clientID;

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


    @DatabaseField(columnName = "Address")
    private String address;


    public Client() {
    }

    /**
     * Constructor to create an object from type Client who can have
     * {@link Project} within the society.
     *
     * @param name            of the client
     * @param email           to communicate with him. It will be verified before accepting it
     * @param telephoneNumber to contact him
     * @param contactPersonID ID of the  {@link Employee} who will communicate with the client
     * @param address         where the client is located
     */
    public Client(final String name, final String email, final String telephoneNumber,
                  final Integer contactPersonID, final String address) {
        this.name = name;
        this.contactPersonID = contactPersonID;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }


    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getContactPersonID() {
        return contactPersonID;
    }

    public void setContactPersonID(Integer contactPersonID) {
        this.contactPersonID = contactPersonID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
