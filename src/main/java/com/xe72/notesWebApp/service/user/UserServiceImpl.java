package com.xe72.notesWebApp.service.user;

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

@Component
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;
//    @Autowired
//    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<User> getCurrentUser() {
        User principal = null;
        try {
            principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException | ClassCastException e) {
            logger.error("getCurrentUser exception", e);
        }
        return Optional.ofNullable(principal);
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    @Override
    public User findUserByName(String name) {
        User userFromDb = userRepository.findByUsername(name);
        return userFromDb;
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean saveUser(User userDto) {
        User userFromDB = userRepository.findByUsername(userDto.getUsername());

        if (userFromDB != null) {
            return false;
        }

        User user = userDto;

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
