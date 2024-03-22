package ro.cristiansterie.vote.util;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import ro.cristiansterie.vote.properties.JWTConfiguration;

@Service
public class JWTUtils {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private JWTConfiguration config;

    public JWTUtils(JWTConfiguration config) {
        this.config = config;
    }

    public String generateJWTToken(Authentication auth) {
        // WIP: the object saved in the JWT should be larger
        String username = (String) auth.getPrincipal();

        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + config.getExpiration() * 100))
                .signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
    }

    public boolean validateJWTToken(String token) {
        try {
            Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(token);

            return true;
        } catch (SignatureException se) {
            log.error("Invalid JWT signature: {}", se.getMessage());
        } catch (MalformedJwtException mje) {
            log.error("Invalid JWT token: {}", mje.getMessage());
        } catch (ExpiredJwtException eje) {
            log.error("JWT token is expired: {}", eje.getMessage());
        } catch (UnsupportedJwtException uje) {
            log.error("JWT Token is unsupported: {}", uje.getMessage());
        } catch (IllegalArgumentException iae) {
            log.error("JWT claims string is empty", iae.getMessage());
        }

        return false;
    }

    public String parseUsername(String token) {
        return Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(token).getBody().getSubject();
    }
}
