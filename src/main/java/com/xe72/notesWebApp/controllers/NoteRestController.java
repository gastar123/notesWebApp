package com.xe72.notesWebApp.controllers;

import com.xe72.notesWebApp.entities.PagingNoteList;
import com.xe72.notesWebApp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteRestController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/api/noteList")
    public PagingNoteList getNoteList(@RequestParam int page, @RequestParam int limit) {
        return noteService.getPageableNotes(page, limit);
    }

    @GetMapping("/.well-known/assetlinks.json")
    public int test() {
        int a = 5;
        return a;
    }
}
