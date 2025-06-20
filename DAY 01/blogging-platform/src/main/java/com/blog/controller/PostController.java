package com.blog.controller;

import com.blog.model.Post;
import com.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepo;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepo.save(post);
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable String id, @RequestBody Post updatedPost) {
        return postRepo.findById(id).map(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setAuthor(updatedPost.getAuthor());
            postRepo.save(post);
            return "Post updated successfully!";
        }).orElse("Post not found");
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable String id) {
        postRepo.deleteById(id);
        return "Post deleted!";
    }
}
