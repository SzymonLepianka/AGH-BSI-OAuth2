package bgs.oauth_server.model;


import bgs.oauth_server.access_services.*;
import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

@Service("RevokeGrantType")
public class RevokeGrantType {

    @Autowired
    private AuthCodesAccessService authCodesAccessService;

    public boolean revokeGrantType(Integer clientID, String authCode) throws SQLException {

        // pobieram z bazy danych AuthCodes i szukam przekazanego 'authCode'
        List<AuthCode> codesFromDataBase = authCodesAccessService.readAll();
        AuthCode authCodeFound = codesFromDataBase.stream()
                .filter(c -> authCode.equals(c.getContent()) && clientID.equals(c.getClientApp().getClientAppId()))
                .findFirst()
                .orElse(null);

        if (authCodeFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auth Code does not exist in data base");
        }

        // robię update AuthCode w bazie danych
        authCodeFound.setRevoked(true);
        authCodesAccessService.update(authCodeFound);

        return true;
    }
}
