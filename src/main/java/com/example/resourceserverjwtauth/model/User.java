package com.example.resourceserverjwtauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{
    private String id;
    private String username;
    private String password;
    private int role_id;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
