package com.example.FirstProjectWithJS.controller;

import com.example.FirstProjectWithJS.model.User;
import com.example.FirstProjectWithJS.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class GreetingsPageController {

    private final UserService userService;

    public GreetingsPageController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/profile/upload")
    public String uploadPhoto(@RequestParam("photo") MultipartFile photo,
                              Model model,
                              HttpSession session) throws IOException {

        if (photo.isEmpty()) {
            model.addAttribute("error", "Файл не выбран");
            return "profile";
        }

        // путь к статической папке
        String uploadDir = "uploads/";
        String filename = photo.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);

        Files.createDirectories(path.getParent());
        Files.write(path, photo.getBytes());

        // обновляем путь для пользователя (в базе или в сессии)
        User currentUser = (User) session.getAttribute("currentUser");
        currentUser.setPhotoUrl("/uploads/" + filename);
        userService.updatePhoto(currentUser, currentUser.getPhotoUrl());
        System.out.println(currentUser.getPhotoUrl());
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Фото успешно загружено");
        return "profile";
    }
}