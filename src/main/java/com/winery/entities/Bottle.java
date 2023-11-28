package com.winery.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "bottle", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "volume", columnList = "volume"),
        @Index(name = "quantity", columnList = "quantity")
})

@Entity
public class Bottle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(name = "volume", nullable = false, length = 5)
    private Double volume;

    @Column(name = "quantity",  length = 1000)
    private Integer quantity;

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
