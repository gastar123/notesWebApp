package com.xe72.notesWebApp.repositories.fragments;

public interface NoteSaver<T> {

    public <S extends T> S save(S entity);
}
