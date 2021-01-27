package com.xe72.notesWebApp.dto.mapper;

import com.xe72.notesWebApp.dto.model.NoteDto;
import com.xe72.notesWebApp.dto.model.TagDto;
import com.xe72.notesWebApp.entity.Note;
import com.xe72.notesWebApp.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class NoteMapper {

    @Autowired
    private UserMapper userMapper;

    public NoteDto toNoteDto(Note note) {
        NoteDto result = new NoteDto();
        result.setId(note.getId());
        result.setTitle(note.getTitle());
        result.setText(note.getText());
        result.setTags(note.getTags().stream().map(it -> new TagDto(it.getName())).collect(Collectors.toSet()));
        result.setCreateDate(note.getCreateDate());
        result.setModifyDate(note.getModifyDate());
        result.setUser(note.getUser().getUsername());
        result.setVersion(note.getVersion());

        return result;
    }

    /**
     * ЮЗЕР НЕ ЗАПОЛНЯЕТСЯ!!! Нужно устанавливать его вручную
     * @param noteDto
     * @return
     */
    public Note toNoteEntity(NoteDto noteDto) {
        Note result = new Note();
        result.setId(noteDto.getId());
        result.setTitle(noteDto.getTitle());
        result.setText(noteDto.getText());
        result.setTags(noteDto.getTags().stream().map(it -> new Tag(it.getName())).collect(Collectors.toSet()));
        result.setCreateDate(noteDto.getCreateDate());
        result.setModifyDate(noteDto.getModifyDate());
//        result.setUser();
        result.setVersion(noteDto.getVersion());

        return result;
    }
}
