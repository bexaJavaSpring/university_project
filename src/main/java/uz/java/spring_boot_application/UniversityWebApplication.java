package uz.java.spring_boot_application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableCaching
public class UniversityWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniversityWebApplication.class, args);
    }
}
