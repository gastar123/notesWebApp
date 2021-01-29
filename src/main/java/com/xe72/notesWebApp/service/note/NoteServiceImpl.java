package com.xe72.notesWebApp.service.note;

import com.xe72.notesWebApp.entity.Note;
import com.xe72.notesWebApp.entity.Tag;
import com.xe72.notesWebApp.repository.NoteRepository;
import com.xe72.notesWebApp.repository.TagRepository;
import com.xe72.notesWebApp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

//    public PagingNoteList getPageableNotes(int page, int size) {
//        Sort sort = Sort.by("createDate").descending().and(Sort.by("id"));
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<Note> pg = noteRepository.findAll(pageable);
//        List<Note> noteList = noteRepository.findAll(pageable).getContent();
//        return new PagingNoteList(noteList, page + 1);
//    }

    @Override
    public List<Note> getNotesByVersion(Long version) {
        return noteRepository.findByVersionGreaterThan(version);
    }

    // TODO: Обрабатывать EntityNotFoundException
    @Override
    public Note getNote(Long id) {
        return noteRepository.findById(id).get();
//        return noteRepository.getOne(id);
    }

    // TODO: Обрабатывать EntityNotFoundException
//    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
    @Override
    @Transactional
    public Note addNote(Note note) {
        return noteRepository.save(prepareNoteForSave(note));
    }

    // TODO: Проверить что Transactional работает как надо, и теги в случае ошибки не сохранятся
    @Override
    @Transactional
    public List<Note> addNotesBatch(List<Note> notes) {
        return notes.stream().map(n -> noteRepository.save(prepareNoteForSave(n))).collect(Collectors.toList());
    }

    // Дичь конечно. Разобраться как сделать для списка по нормальному, а не по одному элементу
    // Кастомные методы для проверки прав https://www.baeldung.com/spring-security-create-new-custom-security-expression
    // TODO: Проверка прав вообще работает? На приватном методе
    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
    private Note prepareNoteForSave(Note note) {
        Note newNote;
        if (note.getId() == null) {
            newNote = note;
            newNote.setCreateDate(new Date());
            newNote.setUser(userService.getCurrentUser().orElseThrow(() -> new RuntimeException("Not authorized")));
        } else {
            // TODO: Может наоборот? В новую заметку дату создания и юзера прописать?
            newNote = noteRepository.findById(note.getId()).get();
            newNote.setTitle(note.getTitle());
            newNote.setText(note.getText());
            newNote.setTags(note.getTags().stream().map(it -> new Tag(it.getName())).collect(Collectors.toSet()));
            newNote.setModifyDate(new Date());
        }

        // TODO: Проверить, вроде лишнего понаписано
        // Сохраняем теги
        HashSet<Tag> newTags = new HashSet<>();
        if (newNote.getTags() != null) {
            List<Tag> tags = newNote.getTags().stream().filter(t -> !StringUtils.isEmpty(t.getName())).collect(
                    Collectors.toList());
            tagRepository.saveAll(tags).forEach(newTags::add);
            newNote.setTags(newTags);
        }
        return newNote;
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'note', 'delete')")
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    // Сделать проверку списка целиком
    @Override
    public void deleteNotes(List<Long> ids) {
        ids.stream().forEach(id -> deleteNote(id));
    }
}
