package uz.java.spring_boot_application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.java.spring_boot_application.dto.file.ResourceFileDto;
import uz.java.spring_boot_application.exception.FileStorageException;
import uz.java.spring_boot_application.exception.ValidationException;
import uz.java.spring_boot_application.util.BaseUtils;
import uz.java.spring_boot_application.util.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class FileService {

    private final BaseUtils baseUtils;
    private final ImageUtils imageUtils;
    private final MinioService minioService;

    @Value("${root.path}")
    private String rootPath;

    public FileService(BaseUtils baseUtils, ImageUtils imageUtils, MinioService minioService) {
        this.baseUtils = baseUtils;
        this.imageUtils = imageUtils;
        this.minioService = minioService;
    }

    public ResourceFileDto storeFile(MultipartFile file, Integer minWidth, Integer minHeight) {
        Path rootLocation = Path.of(rootPath);
        if (file.isEmpty())
            throw new FileStorageException("file.invalid.path");

        String originalFilename = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename())
        );

        if (originalFilename.contains(".."))
            throw new ValidationException("Failed to store file with relative path");

        String contentType = Objects.requireNonNull(file.getContentType());
        String fileNamePrefix = Objects.requireNonNull(StringUtils.split(originalFilename, "."))[0];
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String newFileName = baseUtils.encodeToMd5(fileNamePrefix) + new Date().getTime() + "." + fileExtension;

        String path;
        if (contentType.startsWith("image") && !contentType.contains("svg+xml")) {
            try {
                MultipartFile fileToUpload = file;
                if (!baseUtils.isEmpty(fileExtension) && (!"png".equals(fileExtension))) {
                    if (minWidth != null && minHeight != null)
                        imageUtils.compressImage(rootLocation.resolve(newFileName).toString(),
                                rootLocation.resolve(newFileName).toString(), minWidth, minHeight);
                }
                path = minioService.saveFile(fileToUpload, originalFilename);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            path = minioService.saveFile(file, originalFilename);
        }
        return new ResourceFileDto(path);
    }
}
