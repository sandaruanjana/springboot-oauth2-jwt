package com.example.resourceserverjwtauth.dao.impl;

import com.example.resourceserverjwtauth.dao.UserDao;
import com.example.resourceserverjwtauth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@RequiredArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean save(User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "INSERT INTO users (id,username, password,role_id) VALUES (:id, :username, :password, :role_id)";

        parameterSource.addValue("id", user.getId());
        parameterSource.addValue("username", user.getUsername());
        parameterSource.addValue("password", user.getPassword());
        parameterSource.addValue("role_id", user.getRole_id());

        int update = new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, parameterSource);

        return update > 0;
    }

    @Override
    public User findByUsername(String username) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "SELECT * FROM users WHERE username = :username";

        parameterSource.addValue("username", username);

        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForObject(sql, parameterSource, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setAccountNonLocked(rs.getBoolean("isAccountNonLocked"));
            user.setAccountNonExpired(rs.getBoolean("isAccountNonExpired"));
            user.setCredentialsNonExpired(rs.getBoolean("isCredentialsNonExpired"));
            user.setEnabled(rs.getBoolean("isEnabled"));
            user.setRole_id((rs.getInt("role_id")));
            return user;
        });

    }
}
