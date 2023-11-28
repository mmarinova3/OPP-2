package com.winery.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "role", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "role_name", columnList = "role_name")
})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20, unique = true)
    private UserRole roleName;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserRole getRoleName() {
        return roleName;
    }

    public void setRoleName(UserRole roleName) {
        this.roleName = roleName;
    }
}
