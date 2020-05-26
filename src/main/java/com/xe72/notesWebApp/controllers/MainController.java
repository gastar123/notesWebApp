package com.xe72.notesWebApp.controllers;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addNotePage(Model model) {
        Note note = new Note();
        model.addAttribute("note", note);
        return "add";
    }

    @PostMapping("/addNote")
    public String addNoteOnServer(@RequestParam String tag, @RequestParam String title, @RequestParam String text, @Nullable @RequestParam Long id) {
        noteService.addNote(tag, title, text, id);
        return "redirect:/";
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
