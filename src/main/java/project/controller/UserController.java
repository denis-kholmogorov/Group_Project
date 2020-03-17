package project.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.models.User;
import project.models.util.entity.ListResponse;
import project.models.util.entity.ResponseDataMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//контроллер для тестирования
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    ResponseEntity<?> me() throws Exception {
        ListResponse listResponse = new ListResponse();
        listResponse.setTotal(100);
        listResponse.setOffset(0);

        return ResponseEntity.ok(listResponse);
    }
}
