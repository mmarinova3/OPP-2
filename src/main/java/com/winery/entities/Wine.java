package com.winery.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Table(name = "wine", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "wine_name", columnList = "wine_name"),
        @Index(name = "Volume", columnList = "Volume")
})

@Entity
public class Wine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer id;

    @Column(name = "wine_name", nullable = false, length = 50)
    private String wine_name;

    @Column(name = "volume",  length = 1000)
    private Double volume;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWine_name() {
        return wine_name;
    }

    public void setWine_name(String wine_name) {
        this.wine_name = wine_name;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}
