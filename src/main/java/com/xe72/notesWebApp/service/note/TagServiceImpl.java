package com.xe72.notesWebApp.service.note;

import com.xe72.notesWebApp.dto.model.TagDto;
import com.xe72.notesWebApp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<TagDto> getTags() {
        return tagRepository.findAll().stream().map(it -> new TagDto(it.getName())).collect(Collectors.toList());
    }
}
