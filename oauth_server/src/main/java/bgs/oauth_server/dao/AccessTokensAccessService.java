package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("AccessTokensAccessService")
public class AccessTokensAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private UsersAccessService usersAccessService;

    private AccessToken createAccessTokenFromResult(ResultSet rs) throws SQLException {
        AccessToken accessToken = new AccessToken();
        accessToken.setId(rs.getLong("access_token_id"));
        accessToken.setCreatedAt(rs.getTimestamp("created_at"));
        accessToken.setExpiresAt(rs.getTimestamp("expires_at"));
        accessToken.setRevoked(rs.getBoolean("revoked"));
        accessToken.setScopes(rs.getString("scopes"));
        accessToken.setUpdatedAt(rs.getTimestamp("updated_at"));
        ClientApp clientApp = appsAccessService.readById(rs.getLong("client_app_id"));
        accessToken.setClientApp(clientApp);
        User user = usersAccessService.readById(rs.getLong("user_id"));
        accessToken.setUser(user);
        return accessToken;
    }

    public List<AccessToken> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * from access_tokens";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createAccessTokenFromResult(resultSet));
    }

    public AccessToken readById(Long id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("access_token_id", id);
        final String sql = "select * from access_tokens where access_token_id=:access_token_id";
        List<AccessToken> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createAccessTokenFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public AccessToken create(AccessToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("created_at", object.getCreatedAt());
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("scopes", object.getScopes());
        parameters.addValue("updated_at", object.getUpdatedAt());
        parameters.addValue("client_app_id", object.getClientApp().getId());
        parameters.addValue("user_id", object.getUser().getId());
        final String sql = "insert into access_tokens (created_at, expires_at, revoked, scopes, updated_at, client_app_id, user_id) values (:created_at, :expires_at, :revoked, :scopes, :updated_at, :client_app_id, :user_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public AccessToken update(AccessToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("created_at", object.getCreatedAt());
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("scopes", object.getScopes());
        parameters.addValue("updated_at", object.getUpdatedAt());
        parameters.addValue("client_app_id", object.getClientApp().getId());
        parameters.addValue("user_id", object.getUser().getId());
        parameters.addValue("access_token_id", object.getId());
        final String sql = "update access_tokens set created_at = :created_at, expires_at = :expires_at, revoked = :revoked, scopes = :scopes, updated_at = :updated_at, client_app_id = :client_app_id, user_id = :user_id where access_token_id = :access_token_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(AccessToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("access_token_id", object.getId());
        final String sql = "DELETE FROM access_tokens WHERE access_token_id = :access_token_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
