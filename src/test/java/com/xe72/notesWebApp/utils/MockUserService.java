package com.xe72.notesWebApp.utils;

import com.xe72.notesWebApp.entities.Role;
import com.xe72.notesWebApp.entities.User;
import com.xe72.notesWebApp.repositories.RoleRepository;
import com.xe72.notesWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Primary
public class MockUserService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername("mockUser");
        user.setPassword("pass");
        user.setRoles(new HashSet<Role>() {{
            add(createRoleAndGet("USER"));
        }});
        userService.saveUser(user);
        return user;
    }

    public Role createRoleAndGet(String roleName) {
        Role role = new Role(1L, roleName);
        roleRepository.save(role);
        return role;
    }
}
