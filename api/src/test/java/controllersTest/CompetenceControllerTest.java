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
public class CompetenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int createMockObject(){

        String requestBody = "{\"name\":\"MockCompetence\"}";

        try {
            MvcResult result =
                    this.mockMvc.perform(post("/competences").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            Integer id = Integer.parseInt(result.getResponse().getContentAsString());
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/competences/" + String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test the Rest API GET Method for all competences
     *
     * @throws Exception
     */
    @Test
    public void getAllCompetencesTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/competences")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("MockCompetence")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one competence
     *
     * @throws Exception
     */
    @Test
    public void getCompetenceTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/competences/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("MockCompetence")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     *
     * Creates Competence and then deletes it
     *
     * @throws Exception
     */
    @Test
    public void deleteCompetenceTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/competences/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("MockCompetence")));

    }

    /**
     * Test the Rest API POST Method
     *
     * Creates Competence and the deletes it
     *
     * @throws Exception
     */
    @Test
    public void addCompetenceTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/competences/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("MockCompetence")));

    }

    /**
     * Test the Rest API PUT Method
     *
     * Modifies the name attribute of one competence, then checks if the change was made and then changes it back to
     * normal
     *
     * @throws Exception
     */
    @Test
    public void modifyCompetenceTest() throws Exception {
        int id = createMockObject();
        String requestBody = "{\"id\":"+id+",\"name\":\"MockCompetence62232\"}";
        this.mockMvc.perform(put("/competences").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/competences")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("MockCompetence62232")));
        deleteMockObject(id);

    }


}