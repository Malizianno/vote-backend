package ro.cristiansterie.vote.util;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTTokenAuthentication extends AbstractAuthenticationToken {

    private String token;
    private String principal;
    private UserRoleEnum role;

    public JWTTokenAuthentication(String token, String principal, UserRoleEnum role) {
        super(null);
        this.token = token;
        this.principal = principal;
        this.role = role;
    }

    public JWTTokenAuthentication(String token, String principal, UserRoleEnum role,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        this.role = role;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((principal == null) ? 0 : principal.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        JWTTokenAuthentication other = (JWTTokenAuthentication) obj;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        if (principal == null) {
            if (other.principal != null)
                return false;
        } else if (!principal.equals(other.principal))
            return false;
        if (role != other.role)
            return false;
        return true;
    }

}