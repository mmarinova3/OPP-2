package com.winery.entities;

import jakarta.persistence.*;


import java.io.Serializable;

@Table(name = "wine_composition", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "wine_name", columnList = "wine_name"),
})

@Entity
public class WineComposition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "wine_name", nullable = false, length = 50)
    private String wineName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWineName() {
        return wineName;
    }

    public void setWineName(String wineName) {
        this.wineName = wineName;
    }

}
