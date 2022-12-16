package bgs.oauth_client_server.model;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.xml.bind.*;
import java.util.*;

@Service("GetScopes")
public class GetScopes {

    @Value("${app.secret}")
    private String APP_SECRET;

    public List<String> getScopes(String accessToken) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(APP_SECRET)).parseClaimsJws(accessToken).getBody();

        String scopesRaw = (String) claims.get("scopes");

        List<String> scopesSeparated = new ArrayList<>();
        String[] scopesArray;
        if (scopesRaw.contains(",")) {
            scopesArray = scopesRaw.split(",");
            scopesSeparated.addAll(Arrays.asList(scopesArray));
        } else {
            scopesSeparated.add(scopesRaw);
        }
        System.out.println(scopesSeparated);
        return scopesSeparated;
    }
}