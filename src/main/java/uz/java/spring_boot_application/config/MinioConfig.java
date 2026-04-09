package uz.java.spring_boot_application.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint:http://localhost:9001/}")
    private String minioEndpoint;

    @Value("${minio.accessKey:IcRQWTumktZaUhV8}")
    private String minioAccessKey;

    @Value("${minio.secretKey:h8QlZFqRb1gqIyjRj1kXhTuFrIX0GLFw}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
}
