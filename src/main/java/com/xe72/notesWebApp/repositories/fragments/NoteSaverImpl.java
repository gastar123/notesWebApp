package com.xe72.notesWebApp.repositories.fragments;

import com.xe72.notesWebApp.entities.Note;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class NoteSaverImpl implements NoteSaver<Note> {

    @Autowired
    EntityManager em;

    @Override
    public <S extends Note> S save(S entity) {
        entity.getTagList().stream().forEach(it -> {
            // Обращаемся напрямую к реализации (hibernate). Класс Session
            em.unwrap(Session.class).saveOrUpdate(it);
        });
        em.persist(entity);
        return entity;
    }


}
