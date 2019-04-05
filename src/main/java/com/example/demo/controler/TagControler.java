package com.example.demo.controler;


import com.example.demo.model.Post;
import com.example.demo.model.ResponceModel;
import com.example.demo.model.Tag;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TagControler {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/tag")
    public ResponceModel<List<Tag>> getAllTags(){
        List<Tag> all = tagRepository.findAll();
        ResponceModel<List<Tag>> resp = new ResponceModel<>();
        resp.setCode(200);
        resp.setMessage(all);
        return resp;
    }

    @GetMapping("/posts/{postId}/comments")
    public Page<Tag> getAllCommentsByPostId(@PathVariable (value = "postId") Long postId, Pageable pageable) {
        return tagRepository.findPostId(postId);
    }

    @PostMapping("/tag")
    public ResponceModel<Tag> createPost( @RequestBody Tag tag){
         Tag save = tagRepository.save(tag);
         ResponceModel<Tag> resp = new ResponceModel<>();
         resp.setCode(200);
         resp.setMessage(save);
         return resp;
    }


}
