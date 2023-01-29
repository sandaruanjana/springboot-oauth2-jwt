package com.example.resourceserverjwtauth.dao;

import com.example.resourceserverjwtauth.model.User;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public interface UserDao {
    boolean save(User user);
    User findByUsername(String username);
}
