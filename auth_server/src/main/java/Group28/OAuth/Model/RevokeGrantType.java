package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

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
