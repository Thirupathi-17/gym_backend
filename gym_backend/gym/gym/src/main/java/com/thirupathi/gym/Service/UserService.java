package com.thirupathi.gym.Service;


import com.thirupathi.gym.Entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    ResponseEntity<?> createUser(User user);

    User updateUser(Long id, User updatedUser);

    User getUserById(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    ResponseEntity<?> login(User user, HttpServletResponse response);

    ResponseEntity<?> logout(HttpServletResponse response);


}
