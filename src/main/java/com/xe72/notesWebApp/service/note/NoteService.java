package com.xe72.notesWebApp.service.note;

import com.xe72.notesWebApp.entity.Note;

import java.util.List;

// TODO: Проверить, эти аннотации на реализации должны быть?
public interface NoteService {
    List<Note> getAllNotes();

    List<Note> getNotesByVersion(Long version);

    // TODO: Обрабатывать EntityNotFoundException
    Note getNote(Long id);

    // TODO: Обрабатывать EntityNotFoundException
//    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
//    @Transactional
    Note addNote(Note note);

    // TODO: Проверить что Transactional работает как надо, и теги в случае ошибки не сохранятся
//    @Transactional
    List<Note> addNotesBatch(List<Note> notes);

//    @PreAuthorize("hasPermission(#id, 'note', 'delete')")
    void deleteNote(Long id);

    // Сделать проверку списка целиком
    void deleteNotes(List<Long> ids);
}
