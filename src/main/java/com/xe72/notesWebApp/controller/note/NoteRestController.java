package com.xe72.notesWebApp.controller.note;

import com.xe72.notesWebApp.dto.model.NoteDto;
import com.xe72.notesWebApp.dto.model.TagDto;
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

//    @GetMapping("notes")
//    public PagingNoteList getNoteList(@RequestParam int page, @RequestParam int limit) {
//        return noteService.getPageableNotes(page, limit);
//    }

    @GetMapping("notes")
    public List<NoteDto> getNotesByVersion(@RequestParam("version") Long version) {
        return noteService.getNotesByVersion(version);
    }

    @GetMapping("tags")
    public List<TagDto> getTagList() {
        return tagService.getTags();
    }

    @PostMapping("notes")
    public Long addNote(@RequestBody NoteDto note) {
        return noteService.addNote(note).getId();
    }

    @PostMapping("notesBatch")
    public List<Long> addNotesBatch(@RequestBody List<NoteDto> notes) {
        return noteService.addNotesBatch(notes).stream().map(NoteDto::getId).collect(Collectors.toList());
    }

    @DeleteMapping("notes")
    public void deleteNotes(@RequestBody List<Long> noteIds) {
        noteService.deleteNotes(noteIds);
    }

    // Перенести логику в сервис?
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam("login") String login, @RequestParam("password") String password, HttpServletRequest req, HttpServletResponse resp) {
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
