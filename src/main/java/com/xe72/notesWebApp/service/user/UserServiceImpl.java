package com.xe72.notesWebApp.service.user;

import com.xe72.notesWebApp.dto.mapper.UserMapper;
import com.xe72.notesWebApp.dto.model.UserDto;
import com.xe72.notesWebApp.entity.Role;
import com.xe72.notesWebApp.entity.User;
import com.xe72.notesWebApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;
//    @Autowired
//    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<UserDto> getCurrentUser() {
        UserDto principal = null;
        try {
            principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException | ClassCastException e) {
            logger.error("getCurrentUser exception", e);
        }
        return Optional.ofNullable(principal);
    }

    @Override
    public UserDto findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userMapper.toUserDto(userFromDb.orElse(new User()));
    }

    @Override
    public UserDto findUserByName(String name) {
        User userFromDb = userRepository.findByUsername(name);
        return userMapper.toUserDto(userFromDb);
    }

    @Override
    public List<UserDto> allUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public boolean saveUser(UserDto userDto) {
        User userFromDB = userRepository.findByUsername(userDto.getUsername());

        if (userFromDB != null) {
            return false;
        }

        User user = userMapper.toUserEntity(userDto);

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
