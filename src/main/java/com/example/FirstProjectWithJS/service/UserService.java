package com.example.FirstProjectWithJS.service;

import com.example.FirstProjectWithJS.messages.LoginsMessages;
import com.example.FirstProjectWithJS.model.User;
import com.example.FirstProjectWithJS.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email){
        if (userRepository.findByEmail(email).isEmpty()){
            return null;
        }
        else{
            return userRepository.findByEmail(email).get();
        }
    }

    public void updatePhoto(User user, String path){
        userRepository.updatePhoto(user.getId(), path);
    }
    public LoginsMessages login(String email, String password){
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()){
            return LoginsMessages.INVALID_LOGIN;
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return LoginsMessages.INVALID_PASSWORD;
        }

        return LoginsMessages.SUCCESS;
    }
}
