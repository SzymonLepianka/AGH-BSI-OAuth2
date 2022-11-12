package Group28.OAuth.Controllers;

import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Model.State.AuthenticatingClient;
import Group28.OAuth.Model.State.Context;
import Group28.OAuth.Model.State.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestWebController {

    @GetMapping("/auth/code")
    @ResponseBody
    public String authCode(@RequestParam String clientID, @RequestParam String scopes) throws SQLException {

        // wrzucam parametry requestu do Map
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("scopes", scopes);

        // tworzę Context i ustawiam stan na AuthenticatingClient
        Context context = new Context();
        context.changeState(new AuthenticatingClient());

        // wywołuję metodę handle
        // w optymistycznym scenariuszu wywoła się:
        // AuthenticatingClient handle -> VerifyingDataFromClient handle -> CreatingAuthorizationCode handle -> RedirectingToAppRedirectURL handle
        // i zwróci obiekt Response z redirectURL i obiektem authCode
        Response response = context.handle(params);

//        AuthCode authCode = (AuthCode)response.content;
//        System.out.println(authCode.getContent());
//        System.out.println(response.redirect);


        // tu się wywoła view:
//        AuthCode authCode;
//        if (response.content instanceof AuthCode){
//            authCode = (AuthCode) response.content;
//        }
//        else {
//            throw new ;
//        }

        // zwóci coś
        // i to coś do return

        return "tu będzie coś od view co zawróci klienta na jego URI z paramentrem code / albo kod o błędzie ; code=" + ((AuthCode) response.content).getContent();
    }

    @GetMapping("/auth/tokenForCode")
    @ResponseBody
    public String tokenForCode(@RequestParam String clientID, @RequestParam String code) throws SQLException {

        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("code", code);

        Context context = new Context();
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        String[] accessAndRefresh = (String[]) response.content;
        System.out.println(Arrays.toString(accessAndRefresh));

        //TODO: tu się wywoła view

        return "zwraca token (access i refresh), id token, cookies / lub błąd że code nie istnieje";
    }

    @GetMapping("/auth/refreshToken")
    @ResponseBody
    public String refreshToken(@RequestParam String clientID, @RequestParam String refreshToken) throws SQLException {

        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("refreshToken", refreshToken);

        Context context = new Context();
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        String[] content = (String[]) response.content;
        System.out.println(Arrays.toString(content));

        //TODO: tu się wywoła view

        return "zwraca token (access i refresh), id token, cookies /";
    }

}
