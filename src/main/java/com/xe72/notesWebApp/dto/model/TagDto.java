package com.xe72.notesWebApp.dto.model;

import javax.validation.constraints.NotBlank;

public class TagDto {

    @NotBlank
    private String name;

    public TagDto() {
    }

    public TagDto(@NotBlank String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
