package controllersTest;

import controllers.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
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



    private int createMockObject(){
        String requestBody = " {\"name\": \"Muster Firma\" , \"email\": \"muster@web.de\", " +
                "\"telephoneNumber\": " +
                "\"1111222111\"," + " \"contactPersonID\": 2, \"address\": \"Berlin\"} ";
        try {
            MvcResult result =
                    this.mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            Integer id = Integer.parseInt(result.getResponse().getContentAsString());
           return id;

        }catch(Exception e){
            System.out.println("Mock Object could not be created");
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/clients/" + String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test the Rest API GET Method for all clients
     *
     * @throws Exception
     */
    @Test
    public void getAllClientsTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/clients")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one client
     *
     * @throws Exception
     */
    @Test
    public void getClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/clients/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     *
     * Tries to delete the mock object
     *
     * @throws Exception
     */
    @Test
    public void deleteClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/clients/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Muster Firma")));
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
        int id = createMockObject();
        assertTrue(0!=id);
        deleteMockObject(id);

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
        int id =createMockObject();
        String requestBody = " {\"clientID\":"+ String.valueOf(id) +",\"email\": \"muster@web.de\", \"name\": " +
                "\"ModifyTest#2383832\", " +
                "\"telephoneNumber\": " +
                "\"1111222111\"," +
                " \"contactPersonID\": 2, \"address\": \"Berlin\"} ";
        this.mockMvc.perform(put("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/clients")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("ModifyTest#2383832")));
        deleteMockObject(id);
    }


}