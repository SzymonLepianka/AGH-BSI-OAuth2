package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("AuthCodesAccessService")
public class AuthCodesAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private AppsAccessService appsAccessService;

    private AuthCode createAuthCodeFromResult(ResultSet rs) throws SQLException {
        AuthCode authCode = new AuthCode();
        authCode.setAuthCodeId(rs.getInt("auth_code_id"));
        authCode.setExpiresAt(rs.getTimestamp("expires_at"));
        authCode.setContent(rs.getString("content"));
        authCode.setRevoked(rs.getBoolean("revoked"));
        ClientApp clientApp = appsAccessService.readById(rs.getInt("client_app_id"));
        authCode.setClientApp(clientApp);
        User user = usersAccessService.readById(rs.getInt("user_id"));
        authCode.setUser(user);
        return authCode;
    }

    public List<AuthCode> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * FROM auth_codes";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createAuthCodeFromResult(resultSet));
    }

    public AuthCode readById(Integer id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("auth_code_id", id);
        final String sql = "SELECT * from auth_codes where auth_code_id=:auth_code_id";
        List<AuthCode> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createAuthCodeFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public AuthCode create(AuthCode object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("content", object.getContent());
        parameters.addValue("client_app_id", object.getClientApp().getClientAppId());
        parameters.addValue("user_id", object.getUser().getUserId());
        final String sql = "INSERT INTO auth_codes (expires_at, revoked, content, client_app_id, user_id) values (:expires_at, :revoked, :content, :client_app_id, :user_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public AuthCode update(AuthCode object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("content", object.getContent());
        parameters.addValue("client_app_id", object.getClientApp().getClientAppId());
        parameters.addValue("user_id", object.getUser().getUserId());
        parameters.addValue("auth_code_id", object.getAuthCodeId());
        final String sql = "UPDATE auth_codes SET expires_at = :expires_at, revoked = :revoked, content = :content, client_app_id = :client_app_id, user_id = :user_id WHERE auth_code_id = :auth_code_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(AuthCode object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("auth_code_id", object.getAuthCodeId());
        final String sql = "DELETE FROM auth_codes WHERE auth_code_id = :auth_code_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
