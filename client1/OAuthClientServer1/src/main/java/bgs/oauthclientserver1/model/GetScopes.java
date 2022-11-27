package bgs.oauthclientserver1.model;

import io.jsonwebtoken.*;
import org.springframework.stereotype.*;

import javax.xml.bind.*;
import java.util.*;

@Service("GetScopes")
public class GetScopes {
    public List<String> getScopes(String accessToken) {
        String appSecret = "222222";
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(appSecret)).parseClaimsJws(accessToken).getBody();

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