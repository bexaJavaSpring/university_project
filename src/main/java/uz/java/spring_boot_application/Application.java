package uz.java.spring_boot_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.java.spring_boot_application.entities.University;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 1. Web layer  --> Controller class lar
    // 2. Model Layer --> entity class lar
    // 3. Data Access Layer --> repository, daos class lar
    // 4. Busines Logic Layer --> service class lar
    // 5. Application layer --> helpers, componentalar

//    CRUD -> Create, Read, Update, Delete
}
