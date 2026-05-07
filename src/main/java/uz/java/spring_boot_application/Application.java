package uz.java.spring_boot_application;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import uz.java.spring_boot_application.service.CascadeTypeTestService;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class Application {
    private final CascadeTypeTestService cascadeTypeTestService;

    public Application(CascadeTypeTestService cascadeTypeTestService) {
        this.cascadeTypeTestService = cascadeTypeTestService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0 48 11 * * *")
//    1- 0 soni bu sekund
//    2- 48 soni bu minut
//    3- 11 soni bu soat
//    4dagi 1-*  bu har oyning kuni
//    5dagi 2-*  bu har oy
//    6dagi 3-*  bu har yil
//    @PostConstruct
    public void start() {
        cascadeTypeTestService.createGroup();
    }
}
