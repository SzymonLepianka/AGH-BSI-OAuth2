package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;

import java.sql.*;
import java.util.*;

public class ScopesAccessObject implements IDataBaseAccessObject<Scope>{
    private Connection connection;

    public ScopesAccessObject(Connection connection) {
        this.connection = connection;
    }
    private Scope createScopeFromResult(ResultSet rs) throws SQLException {
        Scope scope = new Scope();
        scope.setId(rs.getLong("scope_id"));
        scope.setName(rs.getString("name"));
        return scope;
    }
    @Override
    public List<Scope> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM scopes");
        List<Scope> resultList = new LinkedList<>();
        while(result.next()){
            resultList.add(createScopeFromResult(result));
        }
        return resultList;
    }

    @Override
    public Scope readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("SELECT * FROM scopes WHERE scope_id = %s", id));
        if(result.next()) {
            Scope scope = createScopeFromResult(result);
            return scope;
        }
        return null;
    }

    @Override
    public Scope create(Scope object) throws SQLException {
        String query = "INSERT INTO scopes (name)" + "values (?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setString(1, object.getName());
        preparedStmt.execute();
        return object;
    }

    @Override
    public Scope update(Scope object) throws SQLException {
        String query = "UPDATE scopes SET name = ? WHERE scope_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setString(1, object.getName());
        //where
        preparedStmt.setLong(2, object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(Scope object) throws SQLException {
        String query = "DELETE FROM scopes WHERE scope_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        //where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }
}
