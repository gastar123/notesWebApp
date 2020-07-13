package com.xe72.notesWebApp.controllers;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {

    private NoteService noteService;

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("")
    public String notesPage(Model model) {
        List<Note> allNotes = noteService.getAllNotes();
        allNotes.forEach(System.out::println);
        model.addAttribute("notes", allNotes);
        return "notes";
    }

    @GetMapping("/add")
    public String addNotePage(Note note) {
        return "add";
    }

    @PostMapping("/add")
    public String addNoteOnServer(@Valid Note note, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add";
        } else {
            noteService.addNote(note);
            return "redirect:/";
        }
    }

    @GetMapping("/deleteNote/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editNote(Model model, @PathVariable Long id) {
        Note note = noteService.getNote(id);
        model.addAttribute("note", note);
        return "add";
    }

}
