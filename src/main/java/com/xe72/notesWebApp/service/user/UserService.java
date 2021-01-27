package com.xe72.notesWebApp.service.user;

import com.xe72.notesWebApp.dto.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> getCurrentUser();

    UserDto findUserById(Long userId);

    UserDto findUserByName(String name);

    List<UserDto> allUsers();

    boolean saveUser(UserDto userDto);

//    @Secured({"ROLE_ADMIN"})
    boolean deleteUser(Long userId);
}
