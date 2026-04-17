package uz.java.spring_boot_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.spring_boot_application.dto.group.GroupResponse;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDto<T> implements Serializable {
    protected T data;
    protected boolean success;

    public DataDto(T response) {
        this.data = response;
        this.success = true;
    }
}
