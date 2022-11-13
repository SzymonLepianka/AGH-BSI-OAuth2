package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("RefreshTokensAccessService")
public class RefreshTokensAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private AccessTokensAccessService accessTokensAccessService;

    private RefreshToken createRefreshTokenFromResult(ResultSet rs) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(rs.getLong("refresh_token_id"));
        refreshToken.setExpiresAt(rs.getTimestamp("expires_at"));
        refreshToken.setRevoked(rs.getBoolean("revoked"));
        AccessToken accessToken = accessTokensAccessService.readById(rs.getLong("access_token_id"));
        refreshToken.setAccessToken(accessToken);
        return refreshToken;
    }

    public List<RefreshToken> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "select * from refresh_tokens";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createRefreshTokenFromResult(resultSet));
    }

    public RefreshToken readById(Long id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("refresh_token_id", id);
        final String sql = "select * from refresh_tokens where refresh_token_id=:refresh_token_id";
        List<RefreshToken> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createRefreshTokenFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public RefreshToken create(RefreshToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("access_token_id", object.getAccessToken().getId());
        final String sql = "insert into refresh_tokens (expires_at, revoked, access_token_id) values (:expires_at, :revoked, :access_token_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public RefreshToken update(RefreshToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("expires_at", object.getExpiresAt());
        parameters.addValue("revoked", object.isRevoked());
        parameters.addValue("access_token_id", object.getAccessToken().getId());
        parameters.addValue("refresh_token_id", object.getId());
        final String sql = "update refresh_tokens set expires_at = :expires_at, revoked = :revoked, access_token_id = :access_token_id where refresh_token_id = :refresh_token_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(RefreshToken object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("refresh_token_id", object.getId());
        final String sql = "DELETE FROM refresh_tokens WHERE refresh_token_id = :refresh_token_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}