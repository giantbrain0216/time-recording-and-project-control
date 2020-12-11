package controllersTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import controllers.Application;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * Test the Rest API GET Method for all employees
     *
     * @throws Exception
     */
    @Test
    public void getAllEmployeesTest() throws Exception {
        this.mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));
    }

    /**
     * Test the Rest API GET Method for one employee
     *
     * @throws Exception
     */
    @Test
    public void getEmployeeTest() throws Exception {
        this.mockMvc.perform(get("/employees/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));
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
        this.mockMvc.perform(delete("/employees/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Max Mustermann")));
        String requestBody = " {\"employeeID\": 1, \"name\": \"Max Mustermann\", \"domicile\": \"Please dont " +
                "delete\"," +
                " \"competences\": \"UNIT-TESTS\", \"projectIDs\": \"1-2-3\"} ";
        this.mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());

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
        String requestBody = " {\"employeeID\": 9999, \"name\": \"Test Employee\", \"domicile\": \"Made to be " +
                "deleted\"," +
                " \"competences\": \"UNIT-TESTS\", \"projectIDs\": \"1-3\"} ";
        this.mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isCreated());
        this.mockMvc.perform(delete("/employees/9999")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Test Employee")));

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
        String requestBody = " {\"employeeID\": 1, \"name\": \"DeleteTest#927292\", \"domicile\": \"Please dont " +
                "delete\"," +
                " \"competences\": \"UNIT-TESTS\", \"projectIDs\": \"1-2-3\"} ";
        this.mockMvc.perform(put("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("DeleteTest#927292")));
        this.mockMvc.perform(put("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody.replace(
                "DeleteTest#927292","Max Mustermann"))).andDo(print()).andExpect(status().isOk());

    }


}