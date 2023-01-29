package com.example.resourceserverjwtauth.service.impl;

import com.example.resourceserverjwtauth.dao.RoleDao;
import com.example.resourceserverjwtauth.model.Role;
import com.example.resourceserverjwtauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Override
    public boolean save(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }
}
