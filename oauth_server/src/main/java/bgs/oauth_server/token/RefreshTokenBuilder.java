package bgs.oauth_server.token;

import io.jsonwebtoken.*;

import javax.crypto.spec.*;
import javax.xml.bind.*;
import java.security.*;
import java.sql.Timestamp;
import java.util.*;

public class RefreshTokenBuilder {

    String secretKey;
    Timestamp expiresAt;
    Integer createdAccessTokenID;


    public RefreshTokenBuilder(Timestamp expiresAt, Integer createdAccessTokenID, Integer appSecret) {
        this.expiresAt = expiresAt;
        this.secretKey = String.valueOf(appSecret);
        this.createdAccessTokenID = createdAccessTokenID;
    }

    private static Map<String, Object> createHead() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
        return map;
    }

    @Override
    public String toString() {
        return "RefreshTokenBuilder{" + "secretKey='" + secretKey + '\'' + ", expiresAt=" + expiresAt + ", createdAccessTokenID=" + createdAccessTokenID + '}';
    }

    public String generateToken() {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setHeader(createHead()).setExpiration(expiresAt).claim("access_token_id", createdAccessTokenID).signWith(SignatureAlgorithm.HS256, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
