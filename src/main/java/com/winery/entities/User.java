package com.winery.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "user", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "username", columnList = "username", unique = true),
        @Index(name = "password", columnList = "password"),
        @Index(name = "fk_role_id", columnList = "role_id")
})

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    public User(Integer id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        if (role != null) {
            return role.getRoleName().toString();
        } else {
            return "No Role";
        }
    }
    public Integer getRoleId() {
        if (role != null) {
            return role.getId();
        } else {
            return 1;
        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role);
    }
}