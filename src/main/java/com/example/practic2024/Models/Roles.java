package com.example.practic2024.Models;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    shop,del;

    @Override
    public String getAuthority() {
        return name();
    }
}
