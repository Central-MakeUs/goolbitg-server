package com.goolbitg.api.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import com.goolbitg.api.security.RoleType;

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

    @ElementCollection
    @CollectionTable(name = "admin_user_roles", joinColumns = @JoinColumn(name = "email"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles;

}
