package project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Данный контроллер просто пустышка на url("/") */
@Controller
public class StarterController {

    @RequestMapping("/")
    public String index(Model model)
    {
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(Model model)
    {
        return "hello";
    }
}
