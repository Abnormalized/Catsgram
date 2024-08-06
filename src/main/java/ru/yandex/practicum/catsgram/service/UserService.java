package ru.yandex.practicum.catsgram.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.*;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Data
@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    public User getUserById(long id) {
        return getUsers().values()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("404: Заданный id не найден"));
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        } else if (users.containsValue(user)){
            throw new DuplicatedDataException("Этот email уже занят");
        }  else if (user.getUsername().isEmpty() ||
                user.getPassword().isEmpty()) {
            throw new ConditionsNotMetException("Остались пустые поля");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        } else if (users.containsValue(newUser)){
            throw new DuplicatedDataException("Этот email уже занят");
        }
        User oldUser = users.get(newUser.getId());
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }
        return oldUser;
    }

    Optional<User> findUserById(Long id) {
        return Optional.ofNullable(getUsers().get(id));
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
