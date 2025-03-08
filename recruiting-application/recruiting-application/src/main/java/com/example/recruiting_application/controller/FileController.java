package com.example.recruiting_application.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class FileController {
    private final String uploadDir = "uploads/resumes/";

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName){
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (MalformedURLException e){
            return ResponseEntity.badRequest().build();

        }
    }

}
