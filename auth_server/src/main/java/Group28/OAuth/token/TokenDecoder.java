package Group28.OAuth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.DatatypeConverter;

public class TokenDecoder {

    public Claims decodeToken(String token, String appSecret){
        //This line will throw an exception if it is not a signed JWS (as expected)
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                    .parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT signature does not match locally computed signature.");
        }
    }

}
