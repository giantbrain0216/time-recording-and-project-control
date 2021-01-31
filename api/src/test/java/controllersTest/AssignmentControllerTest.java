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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int createMockObject(){

        String requestBody = "{\"employeeID\":1,\"projectID\":1,\"plannedWorkingHours\":6969}";

        try {
            MvcResult result =
                    this.mockMvc.perform(post("/assignments").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            Integer id = Integer.parseInt(result.getResponse().getContentAsString());
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/assignments/" + String.valueOf(id)));
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
        this.mockMvc.perform(get("/assignments")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("6969")));
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
        this.mockMvc.perform(get("/assignments/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("6969")));
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
        this.mockMvc.perform(delete("/assignments/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("6969")));

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
        this.mockMvc.perform(delete("/assignments/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("6969")));

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
        String requestBody = "{\"id\":"+id+",\"employeeID\":1,\"projectID\":1,\"plannedWorkingHours\":6969}";
        this.mockMvc.perform(put("/assignments").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/assignments")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("6969")));
        deleteMockObject(id);

    }


}