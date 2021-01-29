package com.xe72.notesWebApp.controller.note;

import com.xe72.notesWebApp.dto.mapper.NoteMapper;
import com.xe72.notesWebApp.dto.model.NoteDto;
import com.xe72.notesWebApp.dto.model.TagDto;
import com.xe72.notesWebApp.entity.Note;
import com.xe72.notesWebApp.service.note.NoteService;
import com.xe72.notesWebApp.service.note.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NoteRestController {

    Logger logger = LoggerFactory.getLogger(NoteRestController.class);

    @Autowired
    private NoteService noteService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RememberMeServices rememberMeServices;

    @Autowired
    private NoteMapper noteMapper;

//    @GetMapping("notes")
//    public PagingNoteList getNoteList(@RequestParam int page, @RequestParam int limit) {
//        return noteService.getPageableNotes(page, limit);
//    }

    @GetMapping("notes")
    public List<NoteDto> getNotesByVersion(@RequestParam("version") Long version) {
        List<NoteDto> notes = noteService.getNotesByVersion(version).stream().map(noteMapper::toNoteDto)
                                         .collect(Collectors.toList());
        return notes;
    }

    @GetMapping("tags")
    public List<TagDto> getTagList() {
        List<TagDto> tags = tagService.getTags().stream().map(it -> new TagDto(it.getName()))
                                      .collect(Collectors.toList());
        return tags;
    }

    @PostMapping("notes")
    public Long addNote(@RequestBody NoteDto noteDto) {
        Note note = noteMapper.toNoteEntity(noteDto);
        return noteService.addNote(note).getId();
    }

    @PostMapping("notesBatch")
    public List<Long> addNotesBatch(@RequestBody List<NoteDto> notes) {
        List<Note> noteEntities = notes.stream().map(noteMapper::toNoteEntity).collect(Collectors.toList());
        return noteService.addNotesBatch(noteEntities).stream().map(Note::getId).collect(Collectors.toList());
    }

    @DeleteMapping("notes")
    public void deleteNotes(@RequestBody List<Long> noteIds) {
        noteService.deleteNotes(noteIds);
    }

    // Перенести логику в сервис? Перенести в отдельный контроллер?
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam("login") String login,
                                        @RequestParam("password") String password,
                                        HttpServletRequest req,
                                        HttpServletResponse resp) {
        try {
            req.login(login, password);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            rememberMeServices.loginSuccess(req, resp, auth);
            return ResponseEntity.ok().build();
        } catch (ServletException e) {
            logger.error("Login failed! login:" + login + "; pass:" + password, e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest req) {
        try {
            req.logout();
            return ResponseEntity.ok().build();
        } catch (ServletException e) {
            logger.error("Logout failed", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
