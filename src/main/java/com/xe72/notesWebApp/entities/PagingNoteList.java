package com.xe72.notesWebApp.entities;

import java.util.List;

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
