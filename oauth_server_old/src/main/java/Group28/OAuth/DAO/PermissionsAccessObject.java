package Group28.OAuth.DAO;

import Group28.OAuth.Domain.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PermissionsAccessObject implements IDataBaseAccessObject<Permission>{
    private Connection connection;

    public PermissionsAccessObject(Connection connection) {
        this.connection = connection;
    }
    private Permission createPermissionFromResult(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setId(rs.getLong("permission_id"));
        ClientApp clientApp = DatabaseEditor.getInstance().getAppsAccessObject().readById(rs.getLong("client_app_id"));
        permission.setClientApp(clientApp);
        User user = DatabaseEditor.getInstance().getUsersAccessObject().readById(rs.getLong("user_id"));
        permission.setUser(user);
        Scope scope = DatabaseEditor.getInstance().getScopesAccessObject().readById(rs.getLong("scope_id"));
        permission.setScope(scope);
        return permission;
    }
    @Override
    public List<Permission> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM permissions");
        List<Permission> resultList = new LinkedList<>();
        while (result.next()){
            resultList.add(createPermissionFromResult(result));
        }
        return resultList;
    }

    @Override
    public Permission readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("SELECT * FROM permissions WHERE permission_id=%s", id));
        if (result.next()) {
            Permission permission = createPermissionFromResult(result);
            return permission;
        }
        return null;
    }

    @Override
    public Permission create(Permission object) throws SQLException {
        String query = "INSERT INTO permissions (client_app_id, scope_id, user_id)" + "values(?,?,?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setLong(1, object.getClientApp().getId());
        preparedStmt.setLong(2, object.getScope().getId());
        preparedStmt.setLong(3, object.getUser().getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public Permission update(Permission object) throws SQLException {
        String query = "UPDATE permissions SET client_app_id = ?, scope_id = ?, user_id = ? WHERE permission_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setLong(1, object.getClientApp().getId());
        preparedStmt.setLong(2, object.getScope().getId());
        preparedStmt.setLong(3, object.getUser().getId());
        //where
        preparedStmt.setLong(4, object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(Permission object) throws SQLException {
        String query = "DELETE FROM permissions WHERE permission_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        //where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }
}
