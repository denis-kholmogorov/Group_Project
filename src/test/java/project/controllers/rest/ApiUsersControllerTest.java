package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.requestDto.UpdatePersonDto;
import project.security.TokenProvider;
import project.services.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource("/application_test.properties")
class ApiUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();
    private final String token;
    private final String token2;

    @Autowired
    public ApiUsersControllerTest(TokenProvider tokenProvider, PersonService personService) {
        token2 = tokenProvider.createToken("test4@mail.ru");
        token = tokenProvider.createToken("test2@mail.ru");
    }

    @Test
    void getAuthUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)))
                .andExpect(jsonPath("$.data.email", is("test2@mail.ru")));
    }

    @Test
    void personEditBody() throws Exception {
        UpdatePersonDto dto = new UpdatePersonDto();
        dto.setFirstName("firstOne");
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(put("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)))
                .andExpect(jsonPath("$.data.first_name", is("firstOne")));
        //.andExpect(jsonPath("$.data.last_name", is()));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/me")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    void getPersonById() throws Exception {
        mockMvc.perform(get("/api/v1/users/2")
                //.param("id", "2")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)));
    }

    @Test
    void getWallPostsById() throws Exception {  //нужна вставка в Insert.sql
        mockMvc.perform(get("/api/v1/users/2/wall")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token2))
                //.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void addWallPostById() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        PostRequestBodyTagsDto dto = new PostRequestBodyTagsDto("test post", "it's a test!",  tags);
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(post("/api/v1/users/2/wall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("test post")));
    }

    @Test
    void blockPersonById() throws Exception {   //не нужно ли никак проверять поле blocker у юзера?
        mockMvc.perform(put("/api/v1/users/block/2")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    void unblockPersonById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/block/2")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    void search() throws Exception {    //не брался еще
        mockMvc.perform(get("/api/v1/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .param("first_name", "first4")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.data[0].id",is(4)))
                .andExpect(jsonPath("$.data[0].email",is("test4@mail.ru")))
                .andExpect(jsonPath("$.data[0].first_name",is("first4")));
    }
}