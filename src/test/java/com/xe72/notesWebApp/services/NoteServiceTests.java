package com.xe72.notesWebApp.services;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class NoteServiceTests {

    Logger logger = LoggerFactory.getLogger(NoteServiceTests.class);

    @Autowired
    private NoteService noteService;

    @Test
    @WithUserDetails()
    public void testNewAndExistingTags() {
        Assertions.assertTrue(noteService.getAllNotes().isEmpty());
        Note firstNote = new Note();
        firstNote.setTitle("First title");
        firstNote.setText("First text");
        Set<Tag> firstTagList = new HashSet<>();
        firstTagList.add(new Tag("tag1"));
        firstTagList.add(new Tag("tag2"));
        firstNote.setTagList(firstTagList);
        noteService.addNote(firstNote);

        logger.info("FirstNoteSaved");

        Note secondNote = new Note();
        secondNote.setTitle("First title");
        secondNote.setText("First text");
        Set<Tag> secondTagList = new HashSet<>();
        secondTagList.add(new Tag("tag1"));
        secondTagList.add(new Tag("tag3"));
        secondNote.setTagList(secondTagList);
        noteService.addNote(secondNote);

        // Проверка что всё сохранилось
        List<Tag> savedTags = noteService.getAllNotes()
                .stream()
                .flatMap(note -> note.getTagList().stream())
                .collect(Collectors.toList());
        Assertions.assertTrue(savedTags.size() == 4);
        Set<String> allTagNames = Stream.concat(firstTagList.stream(), secondTagList.stream())
                .map(Tag::getName)
                .collect(Collectors.toSet());
        Assertions.assertTrue(savedTags.stream().allMatch(tag -> allTagNames.contains(tag.getName())));
    }
}
