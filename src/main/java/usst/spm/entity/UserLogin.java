package usst.spm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Data
public class UserLogin implements UserDetails {
    List<String> roles = Arrays.asList("teacher", "admin");
    private List<String> permissions;
    private String username;
    private Serializable userId;
    @JsonIgnore
    private String password;
    private String sessionId;
    private boolean isAdmin;

    public UserLogin(String username, List<String> permissions, Serializable userId, String sessionId) {
        this.username = username;
        this.permissions = permissions;
        this.userId = userId;
        this.sessionId = sessionId;
        this.isAdmin = permissions.stream().anyMatch(roles::contains);;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
