package com.xe72.notesWebApp.controller.note;

import com.xe72.notesWebApp.dto.mapper.NoteMapper;
import com.xe72.notesWebApp.dto.model.NoteDto;
import com.xe72.notesWebApp.entity.Note;
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
import java.util.stream.Collectors;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteMapper noteMapper;

    @GetMapping("")
    public String notesPage(Model model) {
        List<NoteDto> noteDtos = noteService.getAllNotes().stream().map(noteMapper::toNoteDto).collect(Collectors.toList());
//        allNotes.forEach(System.out::println);
        model.addAttribute("notes", noteDtos);
        return "notes";
    }

    @GetMapping("/add")
    public String addNotePage(NoteDto note) {
        return "add";
    }

    @PostMapping("/add")
    public String addNoteOnServer(@Valid NoteDto noteDto, BindingResult bindingResult) {
        Note note = noteMapper.toNoteEntity(noteDto);
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

    // И вообще нужна ли тут проверка? Кнопка "Редактировать" будет заблочена на клиенте, и в любом случае сохранить чужую заметку не получится
    // PostAuthorize чтобы не делать повторный запрос в бд
    @PostAuthorize("hasPermission(#model.getAttribute('note'), 'edit')")
    @GetMapping("/note/{id}/edit")
    public String editNote(Model model, @PathVariable Long id) {
        NoteDto noteDto = noteMapper.toNoteDto(noteService.getNote(id));
        model.addAttribute("note", noteDto);
        return "add";
    }

    @GetMapping("note/{id}")
    public String viewNote(Model model, @PathVariable Long id) {
        NoteDto noteDto = noteMapper.toNoteDto(noteService.getNote(id));
        model.addAttribute("note", noteDto);
        return "noteViewer";
    }

}
