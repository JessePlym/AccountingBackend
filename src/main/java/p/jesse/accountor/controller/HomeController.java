package p.jesse.accountor.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/index")
    public String home(Authentication authentication) {
        return "Home " + authentication.getName() + "\nRole " + authentication.getAuthorities();
    }

    @GetMapping
    public String index() {
        return "App is running!";
    }
}
