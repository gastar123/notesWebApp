package com.xe72.notesWebApp.services;

import com.xe72.notesWebApp.entities.Note;
import com.xe72.notesWebApp.entities.Tag;
import com.xe72.notesWebApp.utils.DatabaseCleanup;
import org.junit.jupiter.api.AfterEach;
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

/**
 * Вообще это уже нтеграционные тесты.
 * Для репозиториев можно написать отдельные, через аннотацию @DataJpaTest вместо @SpringBootTest.
 * А для теста сервисов сделать моки репозиториев.
 */
@SpringBootTest
public class NoteServiceTests {

    Logger logger = LoggerFactory.getLogger(NoteServiceTests.class);

    @Autowired
    private NoteService noteService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    // Не @BeforeEach потому что @WithUserDetails выполняется раньше
    // и он сначала добавляет юзера в бд, а потом его стирает
    @AfterEach
    public void truncateTables() {
        databaseCleanup.execute();
    }

    @Test
    @WithUserDetails()
    public void testNewAndExistingTags() {
        Assertions.assertTrue(noteService.getAllNotes().isEmpty(), "Notes table not empty");
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
        Assertions.assertEquals(4, savedTags.size());
        Set<String> allTagNames = Stream.concat(firstTagList.stream(), secondTagList.stream())
                .map(Tag::getName)
                .collect(Collectors.toSet());
        Assertions.assertTrue(savedTags.stream().allMatch(tag -> allTagNames.contains(tag.getName())));
    }

    @Test
    @WithUserDetails()
    public void testEditNote() {
        Assertions.assertTrue(noteService.getAllNotes().isEmpty(), "Notes table not empty");
        String oldText = "Old text.";
        String newText = "New text.";

        Note firstNote = new Note();
        firstNote.setTitle("First title");
        firstNote.setText(oldText);
        Set<Tag> firstTagList = new HashSet<>();
        firstTagList.add(new Tag("tag1"));
        firstTagList.add(new Tag("tag2"));
        firstNote.setTagList(firstTagList);
        noteService.addNote(firstNote);

        Long noteId = noteService.getAllNotes().get(0).getId();
        Note newNote = new Note();
        newNote.setId(noteId);
        newNote.setTitle("First title");
        newNote.setText(newText);
        Set<Tag> newTagList = new HashSet<>();
        newTagList.add(new Tag("tag1"));
        newTagList.add(new Tag("tag2"));
        newNote.setTagList(newTagList);
        noteService.addNote(newNote);

        List<Note> allNotes = noteService.getAllNotes();
        Assertions.assertEquals(1, allNotes.size());
        Assertions.assertEquals(newText, allNotes.get(0).getText());
    }
}
