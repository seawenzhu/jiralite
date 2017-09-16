package com.seawen.jiralite.service.mapper;

import com.seawen.jiralite.domain.*;
import com.seawen.jiralite.service.dto.CodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Code and its DTO CodeDTO.
 */
@Mapper(componentModel = "spring", uses = {CodeTypeMapper.class, })
public interface CodeMapper extends EntityMapper <CodeDTO, Code> {

    @Mapping(source = "codeType.id", target = "codeTypeId")
    @Mapping(source = "codeType.typeCode", target = "codeTypeTypeCode")
    CodeDTO toDto(Code code); 

    @Mapping(source = "codeTypeId", target = "codeType")
    Code toEntity(CodeDTO codeDTO); 
    default Code fromId(Long id) {
        if (id == null) {
            return null;
        }
        Code code = new Code();
        code.setId(id);
        return code;
    }
}
