package bgs.oauth_server.model.State;

import bgs.oauth_server.access_services.*;
import bgs.oauth_server.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@Service("RedirectingToAppRedirectURL")
public class RedirectingToAppRedirectURL implements State {

    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private AuthCodesAccessService authCodesAccessService;
    @Autowired
    private Failure failure;


    @Override
    public Response handle(Map<String, String> params) throws SQLException {

        System.out.println("RedirectingToAppRedirectURL");

        // wyciągam clientID z params
        Integer clientID = Integer.parseInt(params.get("clientID"));

        // biorę z bazy danych redirectURL danego klienta
        String redirectURL = appsAccessService.readById(clientID).getRedirectURL();

        // przypadek scopes -> AuthCode
        if (params.containsKey("code") && !params.containsKey("createdRefreshToken")) {

            // wyciągam code z params
            String code = params.get("code");

            // biorę wszystkie AuthCodes z bazy danych i sprawdzam czy istnieje o takich parametrach jak w params
            List<AuthCode> authCodes = authCodesAccessService.readAll();
            AuthCode authCode = authCodes.stream().filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getClientAppId())).findFirst().orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (thrown in RedirectingToAppRedirectURL)"));

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
//        context.changeState(new Failure());
        return failure.handle(params);
    }

    @Override
    public String toString() {
        return "RedirectingToAppRedirectURL";
    }
}
