package com.piisw.cinema_tickets_app.infrastructure.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piisw.cinema_tickets_app.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserInfo implements UserDetails {

    private Long id;

    private String name;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private GrantedAuthority userRole;

    public UserInfo(Long id, String name, String username, String email, String password, GrantedAuthority userRole) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static UserInfo fromUser(User user) {
        GrantedAuthority userRole = mapToSimpleGrantedAuthority(user.getUserRole());
        return new UserInfo(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), userRole);
    }

    private static SimpleGrantedAuthority mapToSimpleGrantedAuthority(UserRole userRole) {
        return new SimpleGrantedAuthority(userRole.name());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(userRole);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserInfo)) {
            return false;
        }

        UserInfo that = (UserInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
