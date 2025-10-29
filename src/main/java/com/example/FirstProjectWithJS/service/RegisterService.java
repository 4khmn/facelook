package com.example.FirstProjectWithJS.service;

import com.example.FirstProjectWithJS.messages.RegisterMessages;
import com.example.FirstProjectWithJS.model.User;
import com.example.FirstProjectWithJS.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public RegisterMessages register(String first_name,
                                     String last_name,
                                     String birthday_date,
                                     String gender,
                                     String email,
                                     String password1){
        if (userRepository.findByEmail(email).isEmpty()){
            User user = new User(first_name, last_name, gender, birthday_date, email, password1, LocalDate.now());
            userRepository.save(user);
            return RegisterMessages.SUCCESS;
        }
        else{
            return RegisterMessages.EMAIL_ALREADY_USED;
        }
    }
}
