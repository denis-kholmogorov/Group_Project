package project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage
}
