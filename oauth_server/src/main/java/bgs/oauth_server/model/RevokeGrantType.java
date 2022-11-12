package bgs.oauth_server.model;


import bgs.oauth_server.domain.*;

import bgs.oauth_server.dao.*;

import org.springframework.http.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

public class RevokeGrantType {
    public static boolean revokeGrantType(Long clientID, String authCode) throws SQLException {

        // pobieram z bazy danych AuthCodes i szukam przekazanego 'authCode'
        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
        AuthCode authCodeFound = codesFromDataBase.stream()
                .filter(c -> authCode.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                .findFirst()
                .orElse(null);

        if (authCodeFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auth Code does not exist in data base");
        }

        // robię update AuthCode w bazie danych
        authCodeFound.setRevoked(true);
        db.getAuthCodesAccessObject().update(authCodeFound);

        return true;
    }
}
