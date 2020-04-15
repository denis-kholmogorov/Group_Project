package project;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class FirstTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("ROLE_USER")
    public void getAllEmployeesAPI() throws Exception
    {

        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/post/10")//.header("Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW5kaWVzZWw4ODg4QHlhbmRleC5ydSIsImlhdCI6MTU4Njk3MjQ4MiwiZXhwIjoxNTkwNTcyNDgyfQ.aEiACZ6CFcxQ8X913dCLt_NGCjC6zmtHjvU0im0CzDQ")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.id", is(10)))
                .andExpect(jsonPath("$.data.title", is("dsfsdfsdfsdf")))
                .andExpect(jsonPath("$.data.author").exists())
                .andExpect(jsonPath("$.data.author.id", is(4)))
                .andExpect(jsonPath("$.data.comments[0].id",is(3)));

               /* .andExpect(MockMvcResultMatchers.jsonPath("$.data.id.author.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id.author.id", is(4)));*/


    }


}
