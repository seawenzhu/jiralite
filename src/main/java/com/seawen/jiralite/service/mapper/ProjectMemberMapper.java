package com.seawen.jiralite.service.mapper;

import com.seawen.jiralite.domain.*;
import com.seawen.jiralite.service.dto.ProjectMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectMember and its DTO ProjectMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface ProjectMemberMapper extends EntityMapper<ProjectMemberDTO, ProjectMember> {

    @Mapping(source = "project.id", target = "projectId")
    ProjectMemberDTO toDto(ProjectMember projectMember); 

    @Mapping(source = "projectId", target = "project")
    ProjectMember toEntity(ProjectMemberDTO projectMemberDTO);

    default ProjectMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectMember projectMember = new ProjectMember();
        projectMember.setId(id);
        return projectMember;
    }
}
