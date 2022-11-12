package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;

import java.sql.*;
import java.util.*;

public class AppsAccessObject implements IDataBaseAccessObject<ClientApp>{
    private Connection connection;

    public AppsAccessObject(Connection connection) {
        this.connection = connection;
    }

    private ClientApp createClientAppFromResult(ResultSet rs) throws SQLException {
        ClientApp clientApp = new ClientApp();
        clientApp.setId(rs.getLong("client_app_id"));
        User user = DatabaseEditor.getInstance().getUsersAccessObject().readById(rs.getLong("user_id"));
        clientApp.setUser(user);
        clientApp.setAppSecret(rs.getLong("app_secret"));
        clientApp.setRedirectURL(rs.getString("redirecturl"));
        clientApp.setAgeRestriction(rs.getBoolean("age_restriction"));
        return clientApp;
    }
    @Override
    public List<ClientApp> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("select * from client_apps");
        List<ClientApp> ResultList = new LinkedList<>();
        while(result.next()) {
            ResultList.add(createClientAppFromResult(result));
        }
        return ResultList;
    }

    @Override
    public ClientApp readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("select * from client_apps where client_app_id = %s", id));
        if (result.next()) {
            ClientApp clinetApp = createClientAppFromResult(result);
            return clinetApp;
        }
        return null;
    }


    @Override
    public ClientApp create(ClientApp object) throws SQLException {
        String query = "insert into client_apps (app_secret, redirecturl, age_restriction, user_id)" + "values (?,?,?,?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setLong(1, object.getAppSecret());
        preparedStmt.setString(2,object.getRedirectURL());
        preparedStmt.setBoolean(3, object.isAgeRestriction());
        preparedStmt.setLong(4, object.getUser().getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public ClientApp update(ClientApp object) throws SQLException {
        String query = "update client_apps set app_secret=?, redirecturl=?, age_restriction=?, user_id where client_app_id=?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setLong(1,object.getAppSecret());
        preparedStmt.setString(2, object.getRedirectURL());
        preparedStmt.setLong(3, object.getUser().getId());
        preparedStmt.setBoolean(4, object.isAgeRestriction());
        //where
        preparedStmt.setLong(4, object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(ClientApp object) throws SQLException {
        String query = "DELETE FROM client_apps WHERE client_app_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        //where
        preparedStmt.setLong(1,object.getId());
        preparedStmt.execute();
    }
}
