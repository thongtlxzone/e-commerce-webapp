package com.project.shopapp.services;

import com.project.shopapp.models.RoleEntity;
import com.project.shopapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }
}
