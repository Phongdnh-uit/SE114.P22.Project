package com.se114p12.backend.mapper.user;

import com.se114p12.backend.dto.user.UserRequestDTO;
import com.se114p12.backend.dto.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.mapper.GenericMapper;
import com.se114p12.backend.mapper.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RoleMapper.class})
public interface UserMapper extends GenericMapper<User, UserRequestDTO, UserResponseDTO> {
}
