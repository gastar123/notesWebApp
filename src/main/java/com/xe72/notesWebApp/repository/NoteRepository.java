package com.xe72.notesWebApp.repository;

import com.xe72.notesWebApp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>/*, NoteSaver<Note>*/ {

    List<Note> findByVersionGreaterThan(Long version);
}
