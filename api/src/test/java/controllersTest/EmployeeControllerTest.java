package controllersTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.unistuttgart.sopra.Application;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int createMockObject(){
        String requestBody = " {\"name\": \"Max Mustermann\", \"domicile\": \"Made to be " +
                "deleted\"," +
                "  \"workingHoursPerWeek\": 40} ";
        try {
            MvcResult result =
                    this.mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andReturn();
            return Integer.parseInt(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void deleteMockObject(int id){
        try {
            this.mockMvc.perform(delete("/employees/" + String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Test the Rest API GET Method for all employees
     *
     * @throws Exception
     */
    @Test
    public void getAllEmployeesTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API GET Method for one employee
     *
     * @throws Exception
     */
    @Test
    public void getEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(get("/employees/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));
        deleteMockObject(id);
    }

    /**
     * Test the Rest API DELETE Method
     *
     * First deletes the employee and after it adds the deleted employee so that further testing is possible
     *
     * @throws Exception
     */
    @Test
    public void deleteEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/employees/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));

    }

    /**
     * Test the Rest API POST Method
     *
     * First adds the employee and then deletes it. If the add was successfull, then the delete will work fine
     *
     * @throws Exception
     */
    @Test
    public void addEmployeeTest() throws Exception {
        int id = createMockObject();
        this.mockMvc.perform(delete("/employees/" + String.valueOf(id))).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Max Mustermann")));

    }

    /**
     * Test the Rest API PUT Method
     *
     * Modifies the name attribute of one employee, then checks if the change was made and then changes it back to
     * normal
     *
     * @throws Exception
     */
    @Test
    public void modifyEmployeeTest() throws Exception {
        int id = createMockObject();
        String requestBody = " {\"employeeID\": " + String.valueOf(id) + ", \"name\": \"DeleteTest#927292\", " +
                "\"domicile\": \"Please" +
                " " +
                "dont" +
                " " +
                "delete\"," +
                " \"workingHoursPerWeek\": 40} ";
        this.mockMvc.perform(put("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("DeleteTest#927292")));
        deleteMockObject(id);

    }


}