package com.xe72.notesWebApp.dto.mapper;

import com.xe72.notesWebApp.dto.model.RoleDto;
import com.xe72.notesWebApp.dto.model.UserDto;
import com.xe72.notesWebApp.entity.Role;
import com.xe72.notesWebApp.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        UserDto result = new UserDto();
        result.setId(user.getId());
        result.setUsername(user.getUsername());

        // Пароль не передаем??
//        result.setPassword(user.getPassword());
        result.setRoles(user.getRoles().stream().map(it -> new RoleDto(it.getId(), it.getName())).collect(Collectors.toSet()));

        return result;
    }

    public User toUserEntity(UserDto userDto) {
        User result = new User();
        result.setId(userDto.getId());
        result.setUsername(userDto.getUsername());

        // Пароль нужен, т.к. это передача инфы
        result.setPassword(userDto.getPassword());
        result.setRoles(userDto.getRoles().stream().map(it -> new Role(it.getId(), it.getName())).collect(Collectors.toSet()));

        return result;
    }
}
