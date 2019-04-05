package com.example.demo.controler;


import com.example.demo.model.Post;
import com.example.demo.model.ResponceModel;
import com.example.demo.repositories.PostRepository;
import com.example.demo.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostControler {

    private static final Logger logger = LoggerFactory.getLogger(PostControler.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    PostRepository repository;

    @GetMapping("/post")
    public ResponceModel<List<Post>> getAllPost() {
        List<Post> all = repository.findAll();
        ResponceModel<List<Post>> resp = new ResponceModel<>();
        resp.setCode(200);
        resp.setMessage(all);
        return resp;
    }

    @GetMapping("/pp")
    public Page<Post> getALl(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping("/post")
    public ResponceModel<Post> createPost(@RequestParam("file") MultipartFile file, String title, String content) {
        String p = fileStorageService.storeFile(file);
        Post post = new Post();
        post.setContent(content);
        post.setTitle(title);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/downloadFile/")
                .path(p)
                .toUriString();
        post.setFileName(fileDownloadUri);
        Post save = repository.save(post);
        ResponceModel<Post> resp = new ResponceModel<>();
        resp.setCode(200);
        resp.setMessage(save);
        return resp;
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
