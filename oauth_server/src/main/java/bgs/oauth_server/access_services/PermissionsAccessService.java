package bgs.oauth_server.access_services;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service("PermissionsAccessService")
public class PermissionsAccessService {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private ScopesAccessService scopesAccessService;
    @Autowired
    private AppsAccessService appsAccessService;

    private Permission createPermissionFromResult(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setPermissionId(rs.getInt("permission_id"));
        ClientApp clientApp = appsAccessService.readById(rs.getInt("client_app_id"));
        permission.setClientApp(clientApp);
        User user = usersAccessService.readById(rs.getInt("user_id"));
        permission.setUser(user);
        Scope scope = scopesAccessService.readById(rs.getInt("scope_id"));
        permission.setScope(scope);
        return permission;
    }

    public List<Permission> readAll() {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        final String sql = "SELECT * FROM oauth.permissions";
        return namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createPermissionFromResult(resultSet));
    }


    public Permission readById(Integer id) {

        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("permission_id", id);
        final String sql = "SELECT * FROM oauth.permissions WHERE permission_id=:permission_id";
        List<Permission> result = namedJdbcTemplate.query(sql, parameters, (resultSet, i) -> createPermissionFromResult(resultSet));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }


    public Permission create(Permission object) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", object.getClientApp().getClientAppId());
        parameters.addValue("scope_id", object.getScope().getScopeId());
        parameters.addValue("user_id", object.getUser().getUserId());
        final String sql = "INSERT INTO oauth.permissions (client_app_id, scope_id, user_id) values(:client_app_id, :scope_id, :user_id)";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public Permission update(Permission object) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("client_app_id", object.getClientApp().getClientAppId());
        parameters.addValue("scope_id", object.getScope().getScopeId());
        parameters.addValue("user_id", object.getUser().getUserId());
        parameters.addValue("permission_id", object.getPermissionId());
        final String sql = "UPDATE oauth.permissions SET client_app_id = :client_app_id, scope_id = :scope_id, user_id = :user_id WHERE permission_id = :permission_id";
        namedJdbcTemplate.update(sql, parameters);
        return object;
    }

    public void remove(Permission object) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("permission_id", object.getPermissionId());
        final String sql = "DELETE FROM oauth.permissions WHERE permission_id = :permission_id";
        namedJdbcTemplate.update(sql, parameters);
    }
}
