package bgs.oauth_server.token;

import io.jsonwebtoken.*;
import org.springframework.http.*;
import org.springframework.web.server.*;

import javax.xml.bind.*;

public class TokenDecoder {

    public Claims decodeToken(String token, String appSecret) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(appSecret)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT signature does not match locally computed signature.");
        }
    }

}
