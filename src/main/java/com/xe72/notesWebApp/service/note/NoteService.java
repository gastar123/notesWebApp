package com.xe72.notesWebApp.service.note;

import com.xe72.notesWebApp.dto.model.NoteDto;

import java.util.List;

// TODO: Проверить, эти аннотации на реализации должны быть?
public interface NoteService {
    List<NoteDto> getAllNotes();

    List<NoteDto> getNotesByVersion(Long version);

    // TODO: Обрабатывать EntityNotFoundException
    NoteDto getNote(Long id);

    // TODO: Обрабатывать EntityNotFoundException
//    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
//    @Transactional
    NoteDto addNote(NoteDto note);

    // TODO: Проверить что Transactional работает как надо, и теги в случае ошибки не сохранятся
//    @Transactional
    List<NoteDto> addNotesBatch(List<NoteDto> notes);

//    @PreAuthorize("hasPermission(#id, 'note', 'delete')")
    void deleteNote(Long id);

    // Сделать проверку списка целиком
    void deleteNotes(List<Long> ids);
}
