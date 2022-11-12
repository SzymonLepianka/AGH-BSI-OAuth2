package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;

import java.sql.*;
import java.util.*;

public class AccessTokensAccessObject implements IDataBaseAccessObject<AccessToken>{
    private Connection connection;

    public AccessTokensAccessObject(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<AccessToken> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("select * from access_tokens");
        List<AccessToken> ResultList = new LinkedList<>();
        while(result.next()) {
            ResultList.add(createAccessTokenFromResult(result));
        }
        return ResultList;
    }

    private AccessToken createAccessTokenFromResult(ResultSet rs) throws SQLException {
        AccessToken accessToken = new AccessToken();
        accessToken.setId(rs.getLong("access_token_id"));
        accessToken.setCreatedAt(rs.getTimestamp("created_at"));
        accessToken.setExpiresAt(rs.getTimestamp("expires_at"));
        accessToken.setRevoked(rs.getBoolean("revoked"));
        accessToken.setScopes(rs.getString("scopes"));
        accessToken.setUpdatedAt(rs.getTimestamp("updated_at"));
        ClientApp clientApp = DatabaseEditor.getInstance().getAppsAccessObject().readById(rs.getLong("client_app_id"));
        accessToken.setClientApp(clientApp);
        User user = DatabaseEditor.getInstance().getUsersAccessObject().readById(rs.getLong("user_id"));
        accessToken.setUser(user);
        return accessToken;
    }

    @Override
    public AccessToken readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("select * from access_tokens where access_token_id=%s", id));
        if(result.next()) {
            AccessToken accessToken = createAccessTokenFromResult(result);
            return accessToken;
        }
        return null;
    }

    @Override
    public AccessToken create(AccessToken object) throws SQLException {
        String query = " insert into access_tokens (created_at, expires_at, revoked, scopes, updated_at, client_app_id, user_id)" + " values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setTimestamp(1, object.getCreatedAt());
        preparedStmt.setTimestamp(2, object.getExpiresAt());
        preparedStmt.setBoolean(3, object.isRevoked());
        preparedStmt.setString(4, object.getScopes());
        preparedStmt.setTimestamp(5, object.getUpdatedAt());
        preparedStmt.setLong(6, object.getClientApp().getId());
        preparedStmt.setLong(7, object.getUser().getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public AccessToken update(AccessToken object) throws SQLException {
        String query = "update access_tokens set created_at = ?, expires_at = ?, revoked = ?, scopes = ?, updated_at = ?, client_app_id = ?, user_id = ? where access_token_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Set
        preparedStmt.setTimestamp(1, object.getCreatedAt());
        preparedStmt.setTimestamp(2, object.getExpiresAt());
        preparedStmt.setBoolean(3, object.isRevoked());
        preparedStmt.setString(4, object.getScopes());
        preparedStmt.setTimestamp(5, object.getUpdatedAt());
        preparedStmt.setLong(6, object.getClientApp().getId());
        preparedStmt.setLong(7, object.getUser().getId());
        // Where
        preparedStmt.setLong(8,object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(AccessToken object) throws SQLException {
        String query = "DELETE FROM access_tokens WHERE access_token_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }
}
