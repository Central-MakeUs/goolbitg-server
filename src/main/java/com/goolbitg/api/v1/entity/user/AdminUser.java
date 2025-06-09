package com.goolbitg.api.v1.entity;

import java.util.Set;

import com.goolbitg.api.v1.security.RoleType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AdminUser
 */
@Entity
@Table(name = "admin_users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUser {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "admin_user_roles", joinColumns = @JoinColumn(name = "email"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles;

}
