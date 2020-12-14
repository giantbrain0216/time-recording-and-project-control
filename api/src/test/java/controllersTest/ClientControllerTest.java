package controllersTest;

import controllers.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * Test the Rest API GET Method for all clients
     *
     * @throws Exception
     */
    @Test
    public void getAllClientsTest() throws Exception {
        this.mockMvc.perform(get("/clients")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
    }

    /**
     * Test the Rest API GET Method for one client
     *
     * @throws Exception
     */
    @Test
    public void getClientTest() throws Exception {

        this.mockMvc.perform(get("/clients/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
    }

    /**
     * Test the Rest API DELETE Method
     *
     * First deletes the client and after it adds the deleted client so that further testing is possible
     *
     * @throws Exception
     */
    @Test
    public void deleteClientTest() throws Exception {
        this.mockMvc.perform(delete("/clients/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
        String requestBody = " {\"name\": \"Muster Firma\",\"email\": \"muster@web.de\",  \"telephoneNumber\": " +
                "\"1111111111," +
                " " + " \"contactPersonID\": 2, \"projectIDs\": \"3\"} ";
        this.mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());

    }

    /**
     * Test the Rest API POST Method
     *
     * First adds the client and then deletes it. If the add was successfull, then the delete will work fine
     *
     * @throws Exception
     */
    @Test
    public void addClientTest() throws Exception {
        String requestBody = " {\"name\": \"Muster Firma 2\" , \"email\": \"muster@web.de\", \"telephoneNumber\": " +
                "\"1111222111\"," + " \"contactPersonID\": 2, \"projectIDs\": \"1-3\"} ";
        this.mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isCreated());
        this.mockMvc.perform(delete("/employees/9999")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Test Employee")));

    }

    /**
     * Test the Rest API PUT Method
     *
     * Modifies the name attribute of one client, then checks if the change was made and then changes it back to
     * normal
     *
     * @throws Exception
     */
    @Test
    public void modifyClientTest() throws Exception {
        String requestBody = " {\"email\": \"muster@web.de\", \"name\": \"ModifyTest#2383832\", \"telephoneNumber\": " +
                "\"1111111111\"," +
                " \"contactPersonID\": 2, \"projectIDs\": \"3\"} ";
        this.mockMvc.perform(put("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/clients")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("ModifyTest#2383832")));
        this.mockMvc.perform(put("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody.replace(
                "ModifyTest#2383832","Muster Firma"))).andDo(print()).andExpect(status().isOk());

    }


}