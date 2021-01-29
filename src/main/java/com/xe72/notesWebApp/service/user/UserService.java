package com.xe72.notesWebApp.service.user;

import com.xe72.notesWebApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getCurrentUser();

    User findUserById(Long userId);

    User findUserByName(String name);

    List<User> allUsers();

    boolean saveUser(User userDto);

//    @Secured({"ROLE_ADMIN"})
    boolean deleteUser(Long userId);
}
