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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int createMockObject(){

        String requestBody = " {\"clientID\": 1, \"plannedStart\": \"2012-04-23T18:25:43.511Z\" ,"+
                " \"plannedEnd\": \"2012-08-23T18:25:43.511Z\", \"plannedEffort\": 1000, \"performedEffort\":0," +
                        "\"competences\":\"SEARCHTOKEN\"} ";

        try {
            MvcResult result =
                    this.mockMvc.perform(post("/projects").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            Integer id = Integer.parseInt(result.getResponse().getContentAsString());
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/projects/" + String.valueOf(id)));
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
    public void getAllProjectsTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/projects")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("SEARCHTOKEN")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one client
     *
     * @throws Exception
     */
    @Test
    public void getProjectTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/projects/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("SEARCHTOKEN")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     *
     * First deletes the client and after it adds the deleted client so that further testing is possible
     *
     * @throws Exception
     */
    @Test
    public void deleteProjectTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/projects/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("SEARCHTOKEN")));

    }

    /**
     * Test the Rest API POST Method
     *
     * First adds the client and then deletes it. If the add was successfull, then the delete will work fine
     *
     * @throws Exception
     */
    @Test
    public void addProjectTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/projects/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("SEARCHTOKEN")));

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
    public void modifyProjectTest() throws Exception {
        int id = createMockObject();
        String requestBody = " {\"projectNumber\":" + String.valueOf(id) + ",\"clientID\": 1, \"plannedStart\": " +
                "\"2012-04-23T18:25:43" +
                ".511Z\" ,"+
                " \"plannedEnd\": \"2012-08-23T18:25:43.511Z\", \"plannedEffort\": 1000, \"performedEffort\":0," +
                "\"competences\":\"AZNBNENEN\"} ";
        this.mockMvc.perform(put("/projects").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/projects")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("AZNBNENEN")));
        deleteMockObject(id);

    }


}