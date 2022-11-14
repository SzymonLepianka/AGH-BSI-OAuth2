package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("UsersAccessService")
public class UsersAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private User createUserFromResult(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setBirthDate(rs.getDate("birth_date"));
        user.setFirstName(rs.getString("first_name"));
        user.setDeveloper(rs.getBoolean("is_developer"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setSurname(rs.getString("surname"));
        user.setUsername(rs.getString("username"));
        return user;
    }

    public List<User> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * from oauth.users";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createUserFromResult(resultSet));
    }

    public User readById(Integer id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", id);
        final String sql = "select * from oauth.users where user_id=:user_id";
        List<User> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createUserFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public User create(User object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("birth_date", object.getBirthDate());
        parameters.addValue("email", object.getEmail());
        parameters.addValue("first_name", object.getFirstName());
        parameters.addValue("is_developer", object.getDeveloper());
        parameters.addValue("password", object.getPassword());
        parameters.addValue("phone_number", object.getPhoneNumber());
        parameters.addValue("surname", object.getSurname());
        parameters.addValue("username", object.getUsername());
        final String sql = "insert into oauth.users (birth_date, email, first_name, is_developer, password, phone_number, surname, username) values (:birth_date, :email, :first_name, :is_developer, :password, :phone_number, :surname, :username)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public User update(User object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", object.getUserId());
        parameters.addValue("birth_date", object.getBirthDate());
        parameters.addValue("email", object.getEmail());
        parameters.addValue("first_name", object.getFirstName());
        parameters.addValue("is_developer", object.getDeveloper());
        parameters.addValue("password", object.getPassword());
        parameters.addValue("phone_number", object.getPhoneNumber());
        parameters.addValue("surname", object.getSurname());
        parameters.addValue("username", object.getUsername());
        final String sql = "update oauth.users set birth_date = :birth_date, email = :email, first_name = :first_name, is_developer = :is_developer, password = :password, phone_number = :phone_number, surname = :surname, username = :username where user_id = :user_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(User object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", object.getUserId());
        final String sql = "DELETE FROM oauth.users WHERE user_id = :user_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
