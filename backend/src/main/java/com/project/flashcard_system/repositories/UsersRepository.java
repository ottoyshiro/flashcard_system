package com.project.flashcard_system.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.flashcard_system.models.User;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UsersRepository {

    private final Path dirPath = Paths.get("data");
    private final Path filePath = dirPath.resolve("users.json");
    private final ObjectMapper mapper = new ObjectMapper();


    public UsersRepository() {
        try {
            if(!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            if(!Files.exists(filePath)) {
                Files.createFile(filePath);

                mapper.writeValue(filePath.toFile(), new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar o repositório", e);
        }
    }

    public List<User> list() {
        try {
            return Arrays.asList(mapper.readValue(filePath.toFile(), User[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(User user) {
        List<User> users = new ArrayList<>(list());
        users.add(user);
        write(users);
    }

    public void write(List<User> users) {
        try {
            mapper.writeValue(filePath.toFile(), users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        return list().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
