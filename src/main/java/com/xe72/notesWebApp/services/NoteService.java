package com.xe72.notesWebApp.services;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.entities.Tag;
import com.xe72.notesWebApp.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private NoteRepository noteRepository;

    @Autowired
    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNote(Long id) {
        return noteRepository.getOne(id);
    }

    public void addNote(String tagLine, String title, String text, Long id) {
        Note note = new Note();
        if (id != null) {
            note.setId(id);
        }
        note.setTitle(title);
        note.setText(text);
        note.setCreate_date(new Date());
        List<Tag> tagList = Arrays.stream(tagLine.split(",")).map(tag -> new Tag(tag.trim())).collect(Collectors.toList());
        note.setTagList(tagList);
        noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
