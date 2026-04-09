package uz.java.spring_boot_application.service;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import uz.java.spring_boot_application.entities.FileEntity;
import uz.java.spring_boot_application.repository.FileEntityRepository;
import uz.java.spring_boot_application.util.MD5Decode;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.leftPad;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    @Value("${project.upload.path.format}")
    private String defaultUploadPath;

    @Value("${project.upload.path.default-bucket}")
    private String defaultBucketName;

    @Value("${root.path}")
    private String rootPath;

    private final FileEntityRepository fileEntityRepository;
    private final MinioClient minioClient;

    @Transactional
    public String saveFile(MultipartFile file, String originalFilename) {
        String res;
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(StringUtils.stripFilenameExtension(Objects.requireNonNull(originalFilename)));
        fileEntity.setSize(file.getSize());
        fileEntity.setContentType(file.getContentType());
        fileEntityRepository.save(fileEntity);
        String uniqueKey = MD5Decode.md5Decode(fileEntity.getId());
        uniqueKey = String.format("%s.%s", uniqueKey, StringUtils.getFilenameExtension(file.getOriginalFilename()));
        StringBuilder wholePath = new StringBuilder(rootPath);
        String realWholePath = prepareUploadPath(wholePath.append(defaultUploadPath).toString());
        char lastChar = realWholePath.charAt(realWholePath.length() - 1);
        String objectName;
        if (lastChar == '/')
            objectName = String.format("%s%s", realWholePath, uniqueKey);
        else
            objectName = String.format("%s/%s", realWholePath, uniqueKey);
        try (InputStream inputStream = file.getInputStream()) {
            res = uploadFile(defaultBucketName, objectName, inputStream);
        } catch (IOException | MinioException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File can not upload", e);
        }
        fileEntity.setObjectName(res);
        fileEntity.setPath(res);
        fileEntityRepository.save(fileEntity);
        return res;
    }

    public void createBucket(String bucketName) throws MinioException {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new MinioException("Error creating bucket: " + bucketName + "; " + e.getMessage());
        }
    }

    public String uploadFile(String bucketName, String objectName, InputStream inputStream) throws MinioException {
        try {
            log.info("Bucket Name: {}, Object Name: {}", bucketName, objectName);
            createBucket(bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            StatObjectArgs statObject = StatObjectArgs.builder().bucket(bucketName).object(objectName).build();
            String etag = minioClient.statObject(statObject).etag();
            return objectName;
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new MinioException("Unexpected error happened: " + e.getMessage());
        }
    }

    public String prepareUploadPath(String uploadPath) {
        if (!StringUtils.hasText(uploadPath))
            uploadPath = defaultUploadPath;

        Calendar now = Calendar.getInstance();
        return uploadPath
                .replace("{YYYY}", String.valueOf(now.get(Calendar.YEAR)))
                .replace("{MM}", leftPad(String.valueOf(now.get(Calendar.MONTH) + 1), 2, "0"))
                .replace("{DD}", leftPad(String.valueOf(now.get(Calendar.DAY_OF_MONTH)), 2, "0"));
    }
}
