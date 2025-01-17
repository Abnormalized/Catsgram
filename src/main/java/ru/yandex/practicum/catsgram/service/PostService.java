package ru.yandex.practicum.catsgram.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.*;

import java.time.Instant;
import java.util.*;

@Data
@Service
public class PostService {

    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(SortOrder sort, int from, int size) {
        return switch (sort) {
            case DESCENDING -> getPosts().values()
                    .stream()
                    .skip(from)
                    .limit(size)
                    .sorted(Comparator.comparing(Post::getPostDate).reversed())
                    .toList();
            case ASCENDING -> getPosts().values()
                    .stream()
                    .skip(from)
                    .limit(size)
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .toList();
            case null -> getPosts().values()
                    .stream()
                    .skip(from)
                    .limit(size)
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .toList();
        };
    }

    public Post getPostById(long id) {
        return getPosts().values()
                .stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("404: Заданный id не найден"));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        Optional<User> optUser = userService.findUserById(post.getAuthorId());
        if (optUser.isEmpty()) {
            throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId() + " не найден»");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}