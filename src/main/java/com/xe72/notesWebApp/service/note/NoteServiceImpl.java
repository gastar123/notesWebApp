package com.xe72.notesWebApp.service.note;

import com.xe72.notesWebApp.dto.mapper.NoteMapper;
import com.xe72.notesWebApp.dto.mapper.UserMapper;
import com.xe72.notesWebApp.dto.model.NoteDto;
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
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    UserService userService;

    @Override
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll().stream().map(noteMapper::toNoteDto).collect(Collectors.toList());
    }

//    public PagingNoteList getPageableNotes(int page, int size) {
//        Sort sort = Sort.by("createDate").descending().and(Sort.by("id"));
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<Note> pg = noteRepository.findAll(pageable);
//        List<Note> noteList = noteRepository.findAll(pageable).getContent();
//        return new PagingNoteList(noteList, page + 1);
//    }

    @Override
    public List<NoteDto> getNotesByVersion(Long version) {
        return noteRepository.findByVersionGreaterThan(version).stream().map(noteMapper::toNoteDto)
                             .collect(Collectors.toList());
    }

    // TODO: Обрабатывать EntityNotFoundException
    @Override
    public NoteDto getNote(Long id) {
        return noteMapper.toNoteDto(noteRepository.findById(id).get());
//        return noteRepository.getOne(id);
    }

    // TODO: Обрабатывать EntityNotFoundException
//    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
    @Override
    @Transactional
    public NoteDto addNote(NoteDto note) {
//        Note newNote;
//        if (note.getId() == null) {
//            newNote = note;
//            note.setCreateDate(new Date());
//            note.setUser(userProvider.getCurrentUser().orElseThrow(() -> new RuntimeException("Not authorized")));
//        } else {
//            newNote = noteRepository.findById(note.getId()).get();
//            newNote.setTitle(note.getTitle());
//            newNote.setText(note.getText());
//            newNote.setTagList(note.getTagList());
//            newNote.setModifyDate(new Date());
//        }
//        Note newNote = prepareNoteForSave(note);
        return noteMapper.toNoteDto(noteRepository.save(prepareNoteForSave(note)));
    }

    // TODO: Проверить что Transactional работает как надо, и теги в случае ошибки не сохранятся
    @Override
    @Transactional
    public List<NoteDto> addNotesBatch(List<NoteDto> notes) {
        return notes.stream().map(n -> noteMapper.toNoteDto(noteRepository.save(prepareNoteForSave(n))))
                    .collect(Collectors.toList());
    }

    // Дичь конечно. Разобраться как сделать для списка по нормальному, а не по одному элементу
    // Кастомные методы для проверки прав https://www.baeldung.com/spring-security-create-new-custom-security-expression
    // TODO: Проверка прав вообще работает? На приватном методе
    @PreAuthorize("hasPermission(#note.id, 'note', 'edit')")
    private Note prepareNoteForSave(NoteDto note) {
        Note newNote;
        if (note.getId() == null) {
            newNote = noteMapper.toNoteEntity(note);
            newNote.setCreateDate(new Date());
            newNote.setUser(userMapper.toUserEntity(userService.getCurrentUser().orElseThrow(() -> new RuntimeException("Not authorized"))));
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
