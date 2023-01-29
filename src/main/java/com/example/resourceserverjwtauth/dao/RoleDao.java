package com.example.resourceserverjwtauth.dao;

import com.example.resourceserverjwtauth.model.Role;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public interface RoleDao {
    boolean save(Role role);

    Role findByName(String name);

    Role findById(int id);

}
