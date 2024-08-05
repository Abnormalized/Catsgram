package ru.yandex.practicum.catsgram.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(value = {"", "{id}"})
    public Collection<Post> findAll(@PathVariable(required = false) Long id) {
        if (id == null) {
            return postService.getPosts().values();
        } else {
            return postService.getPosts().values()
                    .stream()
                    .filter(post -> post.getId().equals(id))
                    .toList();
        }
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}