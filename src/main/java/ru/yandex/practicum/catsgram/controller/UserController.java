package ru.yandex.practicum.catsgram.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping({"","{id}"})
    public Collection<User> findAll(@PathVariable(required = false) Long id) {
        if (id == null) {
            return userService.getUsers().values();
        } else {
            return userService.getUsers().values()
                    .stream()
                    .filter(user -> user.getId().equals(id))
                    .toList();
        }
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.update(newUser);
    }

}