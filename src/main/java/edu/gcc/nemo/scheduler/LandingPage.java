package edu.gcc.nemo.scheduler;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000") // Update with the URL of the frontend application
public class LandingPage {

    @GetMapping
    public String homeGreeting(){
        return "Greetings user!";
    }
}