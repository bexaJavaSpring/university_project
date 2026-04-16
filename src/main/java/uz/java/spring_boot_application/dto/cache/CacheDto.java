package uz.java.spring_boot_application.dto.cache;

import lombok.Data;

import java.util.List;
@Data
public class CacheDto<T> {
    private List<T> object;
    private Long total;
}
