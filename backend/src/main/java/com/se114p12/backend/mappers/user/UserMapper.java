package com.se114p12.backend.mappers.user;

import com.se114p12.backend.dtos.user.UserRequestDTO;
import com.se114p12.backend.dtos.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.mappers.GenericMapper;
import com.se114p12.backend.mappers.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RoleMapper.class})
public interface UserMapper extends GenericMapper<User, UserRequestDTO, UserResponseDTO> {
}
