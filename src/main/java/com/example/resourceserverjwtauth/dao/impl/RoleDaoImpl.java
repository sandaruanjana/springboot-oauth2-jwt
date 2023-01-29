package com.example.resourceserverjwtauth.dao.impl;

import com.example.resourceserverjwtauth.dao.RoleDao;
import com.example.resourceserverjwtauth.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Repository
@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean save(Role role) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "INSERT INTO roles (name) VALUES (:name)";

        parameterSource.addValue("name", role.getName());

        int update = new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, parameterSource);

        return update > 0;

    }

    @Override
    public Role findByName(String name) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "SELECT * FROM roles WHERE name = :name";

        parameterSource.addValue("name", name);

       return new NamedParameterJdbcTemplate(jdbcTemplate).queryForObject(sql, parameterSource, (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }

    @Override
    public Role findById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "SELECT * FROM roles WHERE id = :id";

        parameterSource.addValue("id", id);

        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForObject(sql, parameterSource, (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            return role;
        });
    }
}
