package com.xe72.notesWebApp.controller.note;

import com.xe72.notesWebApp.dto.model.NoteDto;
import com.xe72.notesWebApp.service.note.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("")
    public String notesPage(Model model) {
        List<NoteDto> allNotes = noteService.getAllNotes();
        allNotes.forEach(System.out::println);
        model.addAttribute("notes", allNotes);
        return "notes";
    }

    @GetMapping("/add")
    public String addNotePage(NoteDto note) {
        return "add";
    }

    @PostMapping("/add")
    public String addNoteOnServer(@Valid NoteDto note, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add";
        } else {
            noteService.addNote(note);
            return "redirect:/";
        }
    }

    @GetMapping("/note/{id}/delete")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/";
    }

    // TODO: Переделать на NotePermissionEvaluator
    // И вообще нужна ли тут проверка? Кнопка "Редактировать" будет заблочена на клиенте, и в любом случае сохранить чужую заметку не получится
//    @PostAuthorize("authentication.principal instanceof T(com.xe72.notesWebApp.entities.User) and #model.getAttribute('note').user?.username == authentication.principal.username")
    @PostAuthorize("hasPermission(#model.getAttribute('note'), 'edit')")
    @GetMapping("/note/{id}/edit")
    public String editNote(Model model, @PathVariable Long id) {
        NoteDto note = noteService.getNote(id);
        model.addAttribute("note", note);
        return "add";
    }

    @GetMapping("note/{id}")
    public String viewNote(Model model, @PathVariable Long id) {
        model.addAttribute("note", noteService.getNote(id));
        return "noteViewer";
    }

}
