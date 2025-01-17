package ru.yandex.practicum.catsgram.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping()
    public Collection<Post> findAll(@RequestParam(defaultValue = "asc") String sort,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        SortOrder sortOrder = SortOrder.from(sort);
        if (from < 0) {
            from = 0;
        }
        if (size < 0) {
            size = 0;
        }
            return postService.findAll(sortOrder, from, size);
    }

    @GetMapping("{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
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