package com.se114p12.backend.services.role;

import com.se114p12.backend.dtos.role.RoleRequestDTO;
import com.se114p12.backend.dtos.role.RoleResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface RoleService {

    PageVO<RoleResponseDTO> getAllRoles(Specification<Role> specification, Pageable pageable);

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO create(RoleRequestDTO roleRequestDTO);

    RoleResponseDTO update(Long id, RoleRequestDTO roleRequestDTO);

    void delete(Long id);
}