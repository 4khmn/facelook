package com.example.FirstProjectWithJS.controller;

import com.example.FirstProjectWithJS.messages.LoginsMessages;
import com.example.FirstProjectWithJS.messages.RegisterMessages;
import com.example.FirstProjectWithJS.model.User;
import com.example.FirstProjectWithJS.service.RegisterService;
import com.example.FirstProjectWithJS.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@Controller
public class LogInController {
    private final RegisterService registerService;
    private final UserService userService;

    public LogInController(RegisterService registerService, UserService userService) {
        this.registerService = registerService;
        this.userService=userService;
    }

    @GetMapping("/profile")
    public String mainPage(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null){
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("currentUser")!= null) {
            return "redirect:/profile";
        }
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpSession session){
        LoginsMessages loginsMessages = userService.login(user.getEmail(), user.getPassword());
        if (loginsMessages == LoginsMessages.SUCCESS) {
            user = userService.getUserByEmail(user.getEmail());

            session.setAttribute("currentUser", user);
            model.addAttribute("user", user);
            if (user.getPhotoUrl() == null){
                return "greetingsPage";

            }


            System.out.println(user);
            return "profile";
        }
        else if (loginsMessages == LoginsMessages.INVALID_LOGIN){
            model.addAttribute("error_email", "User with this login not found");
            return "login";
        }
        else{
            model.addAttribute("email", user.getEmail());
            model.addAttribute("error_password", "Incorrect password");
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam(required = true) String first_name,
                           @RequestParam(required = true) String last_name,
                           @RequestParam(required = true) String birthday_date_day,
                           @RequestParam(required = true) String birthday_date_month,
                           @RequestParam(required = true) String birthday_date_year,
                           @RequestParam(required = false) String gender,
                           @RequestParam(required = true) String email,
                           @RequestParam(required = true) String password1,
                           @RequestParam(required = true) String password2,
                           Model model){

        if (!password1.equals(password2)){

            model.addAttribute("error_password", "The passwords don't match");
            model.addAttribute("first_name", first_name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("birthday_date_day", birthday_date_day);
            model.addAttribute("birthday_date_month", birthday_date_month);
            model.addAttribute("birthday_date_year", birthday_date_year);
            model.addAttribute("gender", gender);
            model.addAttribute("email", email);
            model.addAttribute("password1", password1);
            return "register";
        }

        try {
            int day = Integer.parseInt(birthday_date_day);
            int month = Integer.parseInt(birthday_date_month);
            int year = Integer.parseInt(birthday_date_year);
            LocalDate date = LocalDate.of(year, month, day);
        }catch (DateTimeException e) {
            // Некорректная дата (например, 31 февраля)
            model.addAttribute("error_date", "This date does not exist");
            return "register";
        }

        String birthday_date = birthday_date_day+"."+birthday_date_month+"."+birthday_date_year;
        RegisterMessages registerMessages = registerService.register(first_name, last_name, birthday_date, gender, email, password1);
        if (registerMessages == RegisterMessages.EMAIL_ALREADY_USED){
            model.addAttribute("error_email", "This email is already used");
            model.addAttribute("first_name", first_name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("birthday_date_day", birthday_date_day);
            model.addAttribute("birthday_date_month", birthday_date_month);
            model.addAttribute("birthday_date_year", birthday_date_year);
            model.addAttribute("gender", gender);
            model.addAttribute("password1", password1);
            model.addAttribute("password2", password2);
            return "register";
        }

        return "login";
    }
}
