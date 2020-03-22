package project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/platform")
public class PlatformController {

    @GetMapping("/languages")
    public String getLanguage(HttpServletRequest req){
        return req.getHeader("accept-language");
    }
}
