package edu.gcc.nemo.scheduler;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = {"http://localhost:3000", "http://172.29.48.1:3000"}, allowCredentials = "true", allowedHeaders = "*", maxAge = 3600) // Update with the URL of the frontend application
public class LandingPage {

    @GetMapping
    public String homeGreeting(){
        return "Greetings user!";
    }

}
