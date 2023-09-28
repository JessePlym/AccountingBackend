package p.jesse.accountor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin
public class HomeController {

    @GetMapping
    public String index() {
        return "App is running!";
    }

    @GetMapping("/connect")
    public void isAwake() {}
}
