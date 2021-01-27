package com.xe72.notesWebApp.security;

import com.xe72.notesWebApp.entity.Note;
import com.xe72.notesWebApp.entity.User;
import com.xe72.notesWebApp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Optional;

//@Component
public class NotePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object obj, Object permission) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            if (obj instanceof Note) {
                return checkNote((User) authentication.getPrincipal(), (Note) obj, String.valueOf(permission));
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication.getPrincipal() instanceof UserDetails && (targetId == null || targetId instanceof Number) && targetType.toLowerCase().contains("note")) {
            if (StringUtils.isEmpty(targetId) && permission.toString().equals("edit")) {
                return true;
            }
            User curUser = (User) authentication.getPrincipal();
            Optional<Note> note = noteRepository.findById(((Number) targetId).longValue());
            if (note.isPresent()) {
                return checkNote(curUser, note.get(), String.valueOf(permission));
            }
        }
        return false;
    }

    private boolean checkNote(User curUser, Note note, String permission) {
        return note.getUser() != null && curUser.getId().equals(note.getUser().getId());
    }
}
