package project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
@AllArgsConstructor
public class DefaultController {
    public String index() {
        return "index";
    }
}
