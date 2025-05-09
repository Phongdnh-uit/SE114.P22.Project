package com.se114p12.backend.services.role;

import com.se114p12.backend.dto.role.RoleRequestDTO;
import com.se114p12.backend.dto.role.RoleResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mapper.role.RoleMapper;
import com.se114p12.backend.repository.authentication.RoleRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public PageVO<RoleResponseDTO> getAllRoles(Specification<Role> specification, Pageable pageable) {
        Page<Role> rolePage = roleRepository.findAll(specification, pageable);
        List<RoleResponseDTO> roleResponseDTOs = rolePage.getContent().stream()
                .map(roleMapper::entityToResponse)
                .toList();
        return PageVO.<RoleResponseDTO>builder()
                .content(roleResponseDTOs)
                .totalElements(rolePage.getTotalElements())
                .totalPages(rolePage.getTotalPages())
                .numberOfElements(rolePage.getNumberOfElements())
                .page(rolePage.getNumber())
                .size(rolePage.getSize())
                .build();
    }

    public RoleResponseDTO getRoleById(Long id) {
        return roleMapper.entityToResponse(findRoleById(id));
    }

    public RoleResponseDTO create(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.requestToEntity(roleRequestDTO);
        checkRoleNameUniqueness(role.getName());
        role = roleRepository.save(role);
        return roleMapper.entityToResponse(role);
    }

    public RoleResponseDTO update(Long id, RoleRequestDTO roleRequestDTO) {
        Role existingRole = findRoleById(id);
        if (!existingRole.getName().equals(roleRequestDTO.getName())) {
            checkRoleNameUniqueness(roleRequestDTO.getName());
        }
        roleMapper.partialUpdate(roleRequestDTO, existingRole);
        existingRole = roleRepository.save(existingRole);
        return roleMapper.entityToResponse(existingRole);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    // Private helper methods

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private void checkRoleNameUniqueness(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new DataConflictException("Role name already exists");
        }
    }
}