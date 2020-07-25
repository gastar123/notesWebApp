package com.xe72.notesWebApp.services;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.repositories.NoteRepository;
import com.xe72.notesWebApp.security.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // TODO: Обрабатывать EntityNotFoundException
    public Note getNote(Long id) {
        return noteRepository.getOne(id);
    }

    // TODO: Обрабатывать EntityNotFoundException
    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
    public void addNote(Note note) {
        Note newNote;
        if (note.getId() == null) {
            newNote = note;
            note.setCreateDate(new Date());
            note.setUser(userProvider.getCurrentUser().orElseThrow(() -> new RuntimeException("Not authorized")));
        } else {
            newNote = noteRepository.getOne(note.getId());
            newNote.setTitle(note.getTitle());
            newNote.setText(note.getText());
            newNote.setTagList(note.getTagList());
            newNote.setModifyDate(new Date());
        }
        noteRepository.save(newNote);
    }

    @PreAuthorize("hasPermission(#id, 'note', 'delete')")
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
