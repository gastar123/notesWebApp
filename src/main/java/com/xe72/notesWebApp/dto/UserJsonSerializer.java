package com.xe72.notesWebApp.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xe72.notesWebApp.entity.User;

import java.io.IOException;

// TODO: Убедиться что не нужен, удалить. Залить в заметки как пример!!
//@JsonComponent
public class UserJsonSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getUsername());
    }
}
