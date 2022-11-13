package Group28.OAuth.Model.State;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedirectingToAppRedirectURL extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("RedirectingToAppRedirectURL");

        // wyciągam clientID z params
        Long clientID = Long.parseLong(params.get("clientID"));

        // biorę z bazy danych redirectURL danego klienta
        IDatabaseEditor db = DatabaseEditor.getInstance();
        String redirectURL = db.getAppsAccessObject().readById(clientID).getRedirectURL();

        // przypadek scopes -> AuthCode
        if (params.containsKey("code") && !params.containsKey("createdRefreshToken")) {

            // wyciągam code z params
            String code = params.get("code");

            // biorę wszystkie AuthCodes z bazy danych i sprawdzam czy istnieje o takich parametrach jak w params
            List<AuthCode> authCodes = db.getAuthCodesAccessObject().readAll();
            AuthCode authCode = authCodes.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (thrown in RedirectingToAppRedirectURL)"));

            // zwracam obiekt Response z pobranym redirectURL i pobranym obiektem authCode
            return new Response(redirectURL, authCode);
        }
        // przypadek AuthCode -> AccessToken
        else if (params.containsKey("createdRefreshToken") && params.containsKey("createdAccessToken")) {

            // wyciągam AccessToken i RefreshToken z params
            String createdRefreshToken = params.get("createdRefreshToken");
            String createdAccessToken = params.get("createdAccessToken");

            //tworzę zwracany obiekt Response
            ArrayList<String> response = new ArrayList<>();
            response.add(createdAccessToken);
            response.add(createdRefreshToken);
            // zwracam obiekt Response z pobranym redirectURL i accesstoken+refreshtoken
            return new Response(redirectURL, response);
        }

        // gdy nic się nie dopasowało zmienam stan na failure
        context.changeState(new Failure());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "RedirectingToAppRedirectURL";
    }
}
