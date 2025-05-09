package com.se114p12.backend.mapper.role;

import com.se114p12.backend.dto.role.RoleRequestDTO;
import com.se114p12.backend.dto.role.RoleResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleRequestDTO, RoleResponseDTO> {
}
