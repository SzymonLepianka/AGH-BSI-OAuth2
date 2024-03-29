package bgs.oauth_server.token;

import io.jsonwebtoken.*;

import javax.crypto.spec.*;
import javax.xml.bind.*;
import java.security.*;
import java.sql.Timestamp;
import java.util.*;

public class AccessTokenBuilder {

    String secretKey;
    Timestamp createdAt;
    Timestamp expiresAt;
    String scopes;
    Integer clientID;
    String userID;
    String username;

    public AccessTokenBuilder(Timestamp createdAt, Timestamp expiresAt, String scopes, Integer clientID, Integer userID, String username, Integer appSecret) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.scopes = scopes;
        this.clientID = clientID;
        this.userID = String.valueOf(userID);
        this.secretKey = String.valueOf(appSecret);
        this.username = username;
    }

    private static Map<String, Object> createHead() {
        Map<String, Object> map = new HashMap<>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
        return map;
    }

    @Override
    public String toString() {
        return "TokenBuilder{" + "secretKey='" + secretKey + '\'' + ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", scopes='" + scopes + '\'' + ", clientID=" + clientID + ", userID='" + userID + '\'' + '}';
    }

    public String generateToken() {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setHeader(createHead()).claim("clientID", clientID).setIssuedAt(createdAt).setExpiration(expiresAt).claim("scopes", scopes).setSubject(userID).claim("username", username).signWith(SignatureAlgorithm.HS256, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
