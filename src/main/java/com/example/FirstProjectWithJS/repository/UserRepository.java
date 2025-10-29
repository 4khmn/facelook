package com.example.FirstProjectWithJS.repository;

import com.example.FirstProjectWithJS.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.photoUrl = :path WHERE u.id = :id")
    void updatePhoto(long id, String path);
}
