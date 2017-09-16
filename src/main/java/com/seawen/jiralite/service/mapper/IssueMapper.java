package com.seawen.jiralite.service.mapper;

import com.seawen.jiralite.domain.*;
import com.seawen.jiralite.service.dto.IssueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Issue and its DTO IssueDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, })
public interface IssueMapper extends EntityMapper <IssueDTO, Issue> {

    @Mapping(source = "project.id", target = "projectId")
    IssueDTO toDto(Issue issue); 
    @Mapping(target = "comments", ignore = true)

    @Mapping(source = "projectId", target = "project")
    Issue toEntity(IssueDTO issueDTO); 
    default Issue fromId(Long id) {
        if (id == null) {
            return null;
        }
        Issue issue = new Issue();
        issue.setId(id);
        return issue;
    }
}
