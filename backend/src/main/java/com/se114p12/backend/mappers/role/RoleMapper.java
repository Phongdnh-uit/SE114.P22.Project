package com.se114p12.backend.mappers.role;

import com.se114p12.backend.dtos.role.RoleRequestDTO;
import com.se114p12.backend.dtos.role.RoleResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.mappers.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleRequestDTO, RoleResponseDTO> {}
