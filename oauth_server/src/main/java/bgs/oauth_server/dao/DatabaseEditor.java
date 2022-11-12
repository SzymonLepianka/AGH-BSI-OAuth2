package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;

import java.sql.*;

public class DatabaseEditor implements IDatabaseEditor {
    @Value("${spring.datasource.url}")
    private String databaseUrl = "jdbc:mysql://localhost:3306/oauth_db?serverTimezone=Europe/Warsaw";

    @Value("${spring.datasource.username}")
    private String databaseUser = "root";

    @Value("${spring.datasource.password}")
    private String databasePassword = "";

    private Connection databaseConnection;

    private IDataBaseAccessObject<User> usersAccessObject;

    private IDataBaseAccessObject<Permission> permissionsAccessObject;

    private IDataBaseAccessObject<ClientApp> clientAppsAccessObject;

    private IDataBaseAccessObject<AuthCode> authCodesAccessObject;

    private IDataBaseAccessObject<AccessToken> accessTokensAccessObject;

    private IDataBaseAccessObject<RefreshToken> refreshTokensAccessObject;

    private IDataBaseAccessObject<Scope> scopesAccessObject;

    private static DatabaseEditor instance = null;

    private DatabaseEditor() throws SQLException {
        this.databaseConnection = DriverManager.getConnection(this.databaseUrl, this.databaseUser, this.databasePassword);
        this.usersAccessObject = new UsersAccessObject(this.databaseConnection);
        this.refreshTokensAccessObject = new RefreshTokensAccessObject(this.databaseConnection);
        this.accessTokensAccessObject = new AccessTokensAccessObject(this.databaseConnection);
        this.authCodesAccessObject = new AuthCodesAccessObject(this.databaseConnection);
        this.permissionsAccessObject = new PermissionsAccessObject(this.databaseConnection);
        this.clientAppsAccessObject = new AppsAccessObject(this.databaseConnection);
        this.scopesAccessObject = new ScopesAccessObject(this.databaseConnection);
    }

    public static IDatabaseEditor getInstance() throws SQLException {
        if(DatabaseEditor.instance == null)
            DatabaseEditor.instance = new DatabaseEditor();
        return DatabaseEditor.instance;
    }

    public void finalize() {
        try {
            this.databaseConnection.close();
            super.finalize();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public boolean connect(String database) {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public IDataBaseAccessObject<User> getUsersAccessObject() {
        return this.usersAccessObject;
    }

    @Override
    public IDataBaseAccessObject<Permission> getPermissionsAccessObject() {
        return this.permissionsAccessObject;
    }

    @Override
    public IDataBaseAccessObject<ClientApp> getAppsAccessObject() {
        return this.clientAppsAccessObject;
    }

    @Override
    public IDataBaseAccessObject<AuthCode> getAuthCodesAccessObject() {
        return this.authCodesAccessObject;
    }

    @Override
    public IDataBaseAccessObject<AccessToken> getAccessTokensAccessObject() {
        return this.accessTokensAccessObject;
    }

    @Override
    public IDataBaseAccessObject<RefreshToken> getRefreshTokensAccessObject() {
        return this.refreshTokensAccessObject;
    }

    @Override
    public IDataBaseAccessObject<Scope> getScopesAccessObject() {
        return this.scopesAccessObject;
    }


}
