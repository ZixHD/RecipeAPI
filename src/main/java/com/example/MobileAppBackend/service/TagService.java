package com.example.MobileAppBackend.service;

import com.example.MobileAppBackend.dto.model.TagDto;
import com.example.MobileAppBackend.model.Tag;
import com.example.MobileAppBackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public List<Tag> findAllTags(){
        return tagRepository.findAll();
    }

    public Tag createTag(TagDto tagDto){
        if(tagRepository.existsTagByName(tagDto.getName())){
            throw new RuntimeException("Tag with this name already exists");
        }
        Tag tag = modelMapper.map(tagDto,Tag.class);
        return this.tagRepository.save(tag);
    }
}
