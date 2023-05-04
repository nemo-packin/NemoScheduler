package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Admins admins() {
        return new Admins();
    }
    @Bean
    public Students students() {
        return new Students();
    }
    @Bean
    public Courses courses() {
        return new Courses();
    }
}