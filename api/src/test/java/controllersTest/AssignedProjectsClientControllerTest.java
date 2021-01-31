package controllersTest;

import de.unistuttgart.sopra.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AssignedProjectsClientControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private int createMockObject() {

        String requestBody = "{\"projectID\":56373,\"clientID\":1}";

        try {
            MvcResult result =
                    this.mockMvc.perform(post("/assignedProjectsClient").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            return Integer.parseInt(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id) {
        try {
            this.mockMvc.perform(delete("/assignedProjectsClient/" + id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test the Rest API GET Method for all assignments of projects to clients
     *
     * @throws Exception
     */
    @Test
    public void getAllAssignedProjectsClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/assignedProjectsClient")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one assignment of a project to a client
     *
     * @throws Exception
     */
    @Test
    public void getAssignedProjectsClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/assignedProjectsClient/" + id)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     * <p>
     * First creates assignment, then deletes it
     *
     * @throws Exception
     */
    @Test
    public void deleteAssignedProjectsClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/assignedProjectsClient/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));

    }

    /**
     * Test the Rest API POST Method
     * <p>
     * First creates assignment, then deletes it
     *
     * @throws Exception
     */
    @Test
    public void addAssignedProjectsClientTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/assignedProjectsClient/" + id)).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("56373")));

    }

    /**
     * Test the Rest API PUT Method
     * <p>
     * Modifies the projectID then checks if it was changed
     *
     * @throws Exception
     */
    @Test
    public void modifyAssignedProjectsClientTest() throws Exception {
        int id = createMockObject();
        String requestBody = "{\"id\":" + id + ",\"projectID\":19191991,\"clientID\":1}";
        this.mockMvc.perform(put("/assignedProjectsClient").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/assignedProjectsClient")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("19191991")));
        deleteMockObject(id);

    }


}

