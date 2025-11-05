package ro.cristiansterie.vote.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class FaceIDAuthentication extends AbstractAuthenticationToken {

    private final Object principal; // userId
    private final String faceImageBase64;

    public FaceIDAuthentication(String faceImageBase64) {
        super(null);
        this.faceImageBase64 = faceImageBase64;
        this.principal = null;
        setAuthenticated(false);
    }

    public FaceIDAuthentication(Object principal, String faceImageBase64,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.faceImageBase64 = faceImageBase64;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // No password
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getFaceImageBase64() {
        return faceImageBase64;
    }
}
