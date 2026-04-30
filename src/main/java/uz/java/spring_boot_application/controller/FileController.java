package uz.java.spring_boot_application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.java.spring_boot_application.dto.file.ResourceFileDto;
import uz.java.spring_boot_application.service.FileService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceFileDto> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(required = false, name = "minWidth") Integer minWidth,
                                                      @RequestParam(required = false, name = "minHeight") Integer minHeight) {
        return new ResponseEntity<>(fileService.storeFile(file, minWidth, minHeight), HttpStatus.OK);
    }

    @PostMapping(path = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ResourceFileDto>> uploadFile(@RequestParam("file") List<MultipartFile> files,
                                                            @RequestParam(required = false, name = "minWidth") Integer minWidth,
                                                            @RequestParam(required = false, name = "minHeight") Integer minHeight) {
        List<ResourceFileDto> response = new ArrayList<>();
        files.stream().forEach(file -> {
            ResourceFileDto dto = fileService.storeFile(file, minWidth, minHeight);
            response.add(dto);
        });
        return ResponseEntity.ok(response);
    }
}
