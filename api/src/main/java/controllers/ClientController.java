package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ClientDatabase;
import entities.Client;
import entities.Project;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ClientController {
    private ClientDatabase clientDatabase;

    public ClientController(){
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                    ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
            clientDatabase = new ClientDatabase(connectionSource);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL Clients
     *
     * @return List of all Clients
     */
    @GetMapping("/clients")
    public List<Client> getClients() {
        return clientDatabase.getAllClients();
    }

    /**
     * REST METHOD GET FOR ONE CLIENT
     *
     *  precondition: the given id exists in the database
     *  postcondition: correct Client returned
     *
     * @param clientID - The ID of the client
     * @return Client corresponding to the id
     */
    @GetMapping("/clients/{id}")
    public Client getClient(@PathVariable("id") Integer clientID) {
        Client client = clientDatabase.getClient(clientID);
        return client;
    }

    /**
     * REST METHOD DELETE CLIENT
     *
     * precondition: the given id exists in the database
     * postcondition: client no longer exists in the database and the client is returned
     *
     * @param clientID - id of the employee to be deleted
     * @return
     */
    @DeleteMapping("/clients/{id}")
    public Client deleteClient(@PathVariable("id") Integer clientID) {
        Client client = clientDatabase.getClient(clientID);
        clientDatabase.deleteFromDatabase(clientID);
        return client;
    }

    /**
     * REST METHOD POST TO ADD CLIENT
     *
     * precondition: The request body corresponds to the Client Class
     * postcondition: The given client is added to the database
     *
     * @return
     */
    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public int addClient(@Valid @RequestBody Client requestBody){
        try {
            return clientDatabase.addToDatabase(requestBody);
        }catch(SQLException e){
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE CLIENT
     *
     * precondition: The request body corresponds to the Client Class and the id exists in the database
     * postcondition: The correct client has been updated
     *
     * @return
     */
    @PutMapping("/clients")
    public void updateProject( @Valid @RequestBody Client requestBody){
        clientDatabase.modifyClientData(requestBody);

    }
}
