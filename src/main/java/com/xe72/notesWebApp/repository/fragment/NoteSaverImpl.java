package com.xe72.notesWebApp.repository.fragment;

import com.xe72.notesWebApp.entity.Note;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class NoteSaverImpl implements NoteSaver<Note> {

    @Autowired
    EntityManager em;

    @Override
    public <S extends Note> S save(S entity) {
        entity.getTags().forEach(it -> {
            // Обращаемся напрямую к реализации (hibernate). Класс Session
            em.unwrap(Session.class).saveOrUpdate(it);
        });
        em.unwrap(Session.class).saveOrUpdate(entity);
        return entity;
    }


}
