package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("AppsAccessService")
public class AppsAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired
    private UsersAccessService usersAccessService;

    private ClientApp createClientAppFromResult(ResultSet rs) throws SQLException {
        ClientApp clientApp = new ClientApp();
        clientApp.setId(rs.getLong("client_app_id"));
        User user = usersAccessService.readById(rs.getLong("user_id"));
        clientApp.setUser(user);
        clientApp.setAppSecret(rs.getLong("app_secret"));
        clientApp.setRedirectURL(rs.getString("redirecturl"));
        clientApp.setAgeRestriction(rs.getBoolean("age_restriction"));
        return clientApp;
    }

    public List<ClientApp> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * from client_apps";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createClientAppFromResult(resultSet));
    }

    public ClientApp readById(Long id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", id);
        final String sql = "select * from client_apps where client_app_id = :client_app_id";
        List<ClientApp> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createClientAppFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public ClientApp create(ClientApp object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("app_secret", object.getAppSecret());
        parameters.addValue("redirecturl", object.getRedirectURL());
        parameters.addValue("age_restriction", object.isAgeRestriction());
        parameters.addValue("user_id", object.getUser().getId());
        final String sql = "insert into client_apps (app_secret, redirecturl, age_restriction, user_id) values (:app_secret, :redirecturl, :age_restriction, :user_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public ClientApp update(ClientApp object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("app_secret", object.getAppSecret());
        parameters.addValue("redirecturl", object.getRedirectURL());
        parameters.addValue("age_restriction", object.isAgeRestriction());
        parameters.addValue("user_id", object.getUser().getId());
        parameters.addValue("client_app_id", object.getId());
        final String sql = "update client_apps set app_secret=:app_secret, redirecturl=:redirecturl, age_restriction=:age_restriction, user_id=:user_id where client_app_id=:client_app_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(ClientApp object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", object.getId());
        final String sql = "DELETE FROM client_apps WHERE client_app_id = :client_app_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
