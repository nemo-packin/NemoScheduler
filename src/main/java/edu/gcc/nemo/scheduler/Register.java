package edu.gcc.nemo.scheduler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Update with the URL of the frontend application
public class Register {
    @PostMapping("/register")
    public ResponseEntity<String> postData(@RequestBody Map<String, Object> data) {
        String username = data.get("username").toString();
        String password = data.get("password").toString();

        return ResponseEntity.ok("Your password is: " + username + " : " + password);
    }
}
