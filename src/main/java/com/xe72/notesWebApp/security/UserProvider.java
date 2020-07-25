package com.xe72.notesWebApp.security;

import com.xe72.notesWebApp.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProvider {

    public Optional<User> getCurrentUser() {
        User principal = null;
        try {
            principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException  | ClassCastException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(principal);
    }
}
