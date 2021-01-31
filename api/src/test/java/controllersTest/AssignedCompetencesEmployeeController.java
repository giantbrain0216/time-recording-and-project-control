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
public class AssignedCompetencesEmployeeController {

    @Autowired
    private MockMvc mockMvc;

    private int createMockObject(){

        String requestBody = "{\"employeeID\":56373,\"competenceID\":1}";

        try {
            MvcResult result =
                    this.mockMvc.perform(post("/assignedCompetencesEmployee").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            Integer id = Integer.parseInt(result.getResponse().getContentAsString());
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/assignedCompetencesEmployee/" + String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test the Rest API GET Method for all assigned competences to employees
     *
     * @throws Exception
     */
    @Test
    public void getAllAssignedCompetencesEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/assignedCompetencesEmployee")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one assigned competence to employees
     *
     * @throws Exception
     */
    @Test
    public void getAssignedCompetencesEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/assignedCompetencesEmployee/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     *
     * First creates the competence assignment, then deletes it
     * @throws Exception
     */
    @Test
    public void deleteAssignedCompetencesEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/assignedCompetencesEmployee/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("56373")));

    }

    /**
     * Test the Rest API POST Method
     *
     * First creates the competence assignment, then deletes it
     *
     * @throws Exception
     */
    @Test
    public void addAssignedCompetencesEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/assignedCompetencesEmployee/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("56373")));

    }

    /**
     * Test the Rest API PUT Method
     *
     * Modifies the employeeID of the assignment
     *
     * @throws Exception
     */
    @Test
    public void modifyAssignedCompetencesEmployeeTest() throws Exception {
        int id = createMockObject();
        String requestBody = "{\"id\":"+id+",\"employeeID\":19191991,\"competenceID\":1}";
        this.mockMvc.perform(put("/assignedCompetencesEmployee").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/assignedCompetencesEmployee")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("19191991")));
        deleteMockObject(id);

    }


}