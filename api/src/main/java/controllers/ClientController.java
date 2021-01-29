package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import database.methods.ClientDatabase;
import entities.Client;
import entities.Project;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static properties.Properties.*;

@Tag(name = "Clients", description = "All implemented operations to manage clients")
@RestController
public class ClientController {
    private ClientDatabase clientDatabase;

    public ClientController() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME,
                    PASSWORD);
            clientDatabase = new ClientDatabase(connectionSource);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * REST METHOD GET FOR ALL Clients
     *
     * @return List of all Clients
     */
    @Operation(summary = "Get clients", description = "Get list of all available clients in the database")
    @GetMapping("/clients")
    public List<Client> getClients() {
        return clientDatabase.getAllClients();
    }

    /**
     * REST METHOD GET FOR ONE CLIENT
     * <p>
     * precondition: the given id exists in the database
     * postcondition: correct Client returned
     *
     * @param clientID - The ID of the client
     * @return Client corresponding to the id
     */
    @ApiResponse(responseCode = "200", description = "Client found successfully")
    @ApiResponse(responseCode = "400", description = "Client with the given ID does not exist")
    @Operation(summary = "Finds client by ID", description = "Returns a single client whose ID was given")
    @GetMapping("/clients/{id}")
    public Client getClient(@Parameter(description = "ID of the searched Client") @PathVariable("id") Integer clientID) {
             return clientDatabase.getClient(clientID);
    }

    /**
     * REST METHOD DELETE CLIENT
     * <p>
     * precondition: the given id exists in the database
     * postcondition: client no longer exists in the database and the client is returned
     *
     * @param clientID - id of the employee to be deleted
     * @return the deleted client
     */
    @ApiResponse(responseCode = "200", description = "Client found and deleted successfully")
    @ApiResponse(responseCode = "400", description = "Client with the given ID does not exist")
    @Operation(summary = "deletes client by ID", description = "Returns the deleted client for testing purposes")
    @DeleteMapping("/clients/{id}")
    public Client deleteClient(@PathVariable("id") Integer clientID) {
        Client client = clientDatabase.getClient(clientID);
             clientDatabase.deleteFromDatabase(clientID);
            return client;

    }

    /**
     * REST METHOD POST TO ADD CLIENT
     * <p>
     * precondition: The request body corresponds to the Client Class
     * postcondition: The given client is added to the database
     *
     * @return ID of the added client
     */
    @PostMapping("/clients")
    @Operation(summary = "Adds a new Client to the database", description = "After adding the client returns his ID that will be " +
            "needed for testing purposes and other REST Methods")
    @ResponseStatus(HttpStatus.CREATED)
    public int addClient(@Parameter(description = "From the automatically JSON parsed Request Body a client will be created. The " +
            "request Body contains all necessary attributes which are needed to successfully create a client ") @Valid @RequestBody Client requestBody) {
        try {
            return clientDatabase.addToDatabase(requestBody);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    /**
     * REST METHOD UPDATE CLIENT
     * <p>
     * precondition: The request body corresponds to the Client Class and the id exists in the database
     * postcondition: The correct client has been updated
     */
    @Operation(summary = "Updates client by ID", description = "modifies the data of the given client")
    @PutMapping("/clients")
    public void updateClient(@Parameter(description = "Contains the ID of the client through which the respective client will be recognised ") @Valid @RequestBody Client requestBody) {
        clientDatabase.modifyClientData(requestBody);

    }
}
