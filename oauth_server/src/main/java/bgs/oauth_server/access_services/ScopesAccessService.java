package bgs.oauth_server.access_services;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("ScopesAccessService")
public class ScopesAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private Scope createScopeFromResult(ResultSet rs) throws SQLException {
        Scope scope = new Scope();
        scope.setScopeId(rs.getInt("scope_id"));
        scope.setName(rs.getString("name"));
        return scope;
    }

    public List<Scope> readAll() throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "SELECT * FROM oauth.scopes";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createScopeFromResult(resultSet));
    }

    public Scope readById(Integer id) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("scope_id", id);
        final String sql = "SELECT * FROM oauth.scopes WHERE scope_id = :scope_id";
        List<Scope> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createScopeFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Scope create(Scope object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", object.getName());
        final String sql = "INSERT INTO oauth.scopes (name) values (:name)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public Scope update(Scope object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", object.getName());
        parameters.addValue("scope_id", object.getScopeId());
        final String sql = "UPDATE oauth.scopes SET name = :name WHERE scope_id = :scope_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(Scope object) throws SQLException {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("scope_id", object.getScopeId());
        final String sql = "DELETE FROM oauth.scopes WHERE scope_id = :scope_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
