package com.xe72.notesWebApp.services;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.repositories.NoteRepository;
import com.xe72.notesWebApp.security.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    UserProvider userProvider;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNote(Long id) {
        return noteRepository.getOne(id);
    }

    public void addNote(Note note) {
        if (note.getCreateDate() == null) {
            note.setCreateDate(new Date());
            note.setUser(userProvider.getCurrentUser().orElseThrow(() -> new RuntimeException("Not authorized")));
        } else {
            note.setModifyDate(new Date());
        }
        noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
