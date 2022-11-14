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
        clientApp.setClientAppId(rs.getInt("client_app_id"));
        User user = usersAccessService.readById(rs.getInt("user_id"));
        clientApp.setUser(user);
        clientApp.setAppSecret(rs.getInt("app_secret"));
        clientApp.setRedirectURL(rs.getString("redirect_url"));
        clientApp.setAgeRestriction(rs.getBoolean("age_restriction"));
        return clientApp;
    }

    public List<ClientApp> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * from oauth.client_apps";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createClientAppFromResult(resultSet));
    }

    public ClientApp readById(Integer id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", id);
        final String sql = "select * from oauth.client_apps where client_app_id = :client_app_id";
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
        parameters.addValue("redirect_url", object.getRedirectURL());
        parameters.addValue("age_restriction", object.isAgeRestriction());
        parameters.addValue("user_id", object.getUser().getUserId());
        final String sql = "insert into oauth.client_apps (app_secret, redirect_url, age_restriction, user_id) values (:app_secret, :redirect_url, :age_restriction, :user_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public ClientApp update(ClientApp object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("app_secret", object.getAppSecret());
        parameters.addValue("redirect_url", object.getRedirectURL());
        parameters.addValue("age_restriction", object.isAgeRestriction());
        parameters.addValue("user_id", object.getUser().getUserId());
        parameters.addValue("client_app_id", object.getClientAppId());
        final String sql = "update oauth.client_apps set app_secret=:app_secret, redirect_url=:redirect_url, age_restriction=:age_restriction, user_id=:user_id where client_app_id=:client_app_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(ClientApp object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", object.getClientAppId());
        final String sql = "DELETE FROM oauth.client_apps WHERE client_app_id = :client_app_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
