package com.xe72.notesWebApp.repository.fragment;

public interface NoteSaver<T> {

    public <S extends T> S save(S entity);
}
