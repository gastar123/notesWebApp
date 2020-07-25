package com.xe72.notesWebApp.security;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.entities.User;
import com.xe72.notesWebApp.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Optional;

//@Component
public class NotePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication.getPrincipal() instanceof User && targetId instanceof Number && targetType.toLowerCase().contains("note")) {
            User curUser = (User) authentication.getPrincipal();
            Optional<Note> note = noteRepository.findById(((Number) targetId).longValue());
            if (note.isPresent()) {
                User noteUser = note.get().getUser();
                if (noteUser != null && curUser.getUsername().equals(noteUser.getUsername())) {
                    return true;
                }
            }
        }
        return false;
    }
}
