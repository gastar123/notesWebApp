package com.xe72.notesWebApp.utils;

import com.xe72.notesWebApp.dto.model.RoleDto;
import com.xe72.notesWebApp.dto.model.UserDto;
import com.xe72.notesWebApp.entity.Role;
import com.xe72.notesWebApp.repository.RoleRepository;
import com.xe72.notesWebApp.service.user.UserService;
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
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = new UserDto();
        user.setUsername("mockUser");
        user.setPassword("pass");
        user.setRoles(new HashSet<RoleDto>() {{
            add(createRoleAndGet("USER"));
        }});
        userService.saveUser(user);
        return user;
    }

    public RoleDto createRoleAndGet(String roleName) {
//        Role role = new Role(1L, roleName);
        Role role = roleRepository.save(new Role(1L, roleName));
        RoleDto roleDto = new RoleDto(role.getId(), role.getName());
        return roleDto;
    }
}
