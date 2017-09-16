package com.seawen.jiralite.service.mapper;

import com.seawen.jiralite.domain.*;
import com.seawen.jiralite.service.dto.CommentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comments and its DTO CommentsDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, })
public interface CommentsMapper extends EntityMapper <CommentsDTO, Comments> {

    @Mapping(source = "issue.id", target = "issueId")
    CommentsDTO toDto(Comments comments); 

    @Mapping(source = "issueId", target = "issue")
    Comments toEntity(CommentsDTO commentsDTO); 
    default Comments fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comments comments = new Comments();
        comments.setId(id);
        return comments;
    }
}
