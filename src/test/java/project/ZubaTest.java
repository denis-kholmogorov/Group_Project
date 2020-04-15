package project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.responseDto.ResponseDto;
import project.repositories.DialogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc()
@ActiveProfiles("dev")
public class ZubaTest {




        private TestRestTemplate testRestTemplate;

        @Autowired
        private HttpHeaders headers = new HttpHeaders();

        @Autowired
        private DialogRepository dialogRepository;


    @Test
        public void sentMessage() throws JsonProcessingException {


            String url = "/api/v1/post/10";


            ResponseDto responseDto = new ResponseDto("privet");

            String inputInJson = mapToJson(responseDto);

            HttpEntity<ResponseDto> entity = new HttpEntity<>(responseDto, headers);
            ResponseEntity<String> response = testRestTemplate.exchange(
                    "/api/v1/post/10",
                    HttpMethod.GET, entity, String.class);

            String responseInJson = response.getBody();
            System.out.println(responseInJson);



        }

        private String mapToJson(Object object) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        }
/*

        private String formFullURLWithPort(String uri) {
            return "http://localhost:" + port + uri;
        }
*/

}
