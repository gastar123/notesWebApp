package com.xe72.notesWebApp.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class NoteListener {

    @Autowired
    AbstractSequenceMaxValueIncrementer noteVersionSequence;

    @PrePersist
    @PreUpdate
    private void updateVersion(Note note) {
        note.setVersion(noteVersionSequence.nextLongValue());
    }
}
