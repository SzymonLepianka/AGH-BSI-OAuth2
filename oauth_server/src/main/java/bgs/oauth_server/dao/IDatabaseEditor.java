package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;

public interface IDatabaseEditor {
    boolean connect(String database);
    boolean disconnect();

    IDataBaseAccessObject<User> getUsersAccessObject();
    IDataBaseAccessObject<Permission> getPermissionsAccessObject();
    IDataBaseAccessObject<ClientApp> getAppsAccessObject();
    IDataBaseAccessObject<AuthCode> getAuthCodesAccessObject();
    IDataBaseAccessObject<AccessToken> getAccessTokensAccessObject();
    IDataBaseAccessObject<RefreshToken> getRefreshTokensAccessObject();
    IDataBaseAccessObject<Scope> getScopesAccessObject();

}
