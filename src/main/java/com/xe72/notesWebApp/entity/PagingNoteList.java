package com.xe72.notesWebApp.entity;

import java.util.List;

// TODO: Пока нигде не используется
public class PagingNoteList {

    public PagingNoteList(List<Note> noteList, int nextPage) {
        this.noteList = noteList;
        this.nextPage = nextPage;
    }

    private List<Note> noteList;
    private int nextPage;

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

}
