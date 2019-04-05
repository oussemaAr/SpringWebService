package com.example.demo.controler;


import com.example.demo.model.Post;
import com.example.demo.model.ResponceModel;
import com.example.demo.repositories.PostRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostControler {

    @Autowired
    PostRepository repository;

    @GetMapping("/post")
    public ResponceModel<List<Post>> getAllPost(){
        List<Post> all = repository.findAll();
        ResponceModel<List<Post>> resp = new ResponceModel<>();
        resp.setCode(200);
        resp.setMessage(all);
        return resp;
    }

    @GetMapping("/pp")
    public Page<Post> getALl(Pageable pageable){
        return repository.findAll(pageable);
    }

    @PostMapping("/post")
    public ResponceModel<Post> createPost( @RequestBody Post post){
         Post save = repository.save(post);
         ResponceModel<Post> resp = new ResponceModel<>();
         resp.setCode(200);
         resp.setMessage(save);
         return resp;
    }


}
