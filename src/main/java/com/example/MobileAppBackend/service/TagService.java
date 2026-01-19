package com.example.MobileAppBackend.service;

import com.example.MobileAppBackend.dto.model.TagDto;
import com.example.MobileAppBackend.model.Tag;
import com.example.MobileAppBackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
            log.warn("Tag with name {} already exists", tagDto.getName());
            throw new RuntimeException("Tag with this name already exists");
        }
        Tag tag = modelMapper.map(tagDto,Tag.class);
        log.debug("Created tag {}", tag);
        log.info("Tag created successfully");
        return this.tagRepository.save(tag);
    }
}
