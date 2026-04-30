package uz.java.spring_boot_application.dto.file;

import lombok.Data;

@Data
public class AttachmentDto {
    private Long fileId;
    private String fileUrl;
}
