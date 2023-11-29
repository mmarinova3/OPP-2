package com.winery.entities;

import jakarta.persistence.*;

@Table(name = "bottled_wine", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_wine_id", columnList = "wine_id"),
        @Index(name = "fk_bottle_id", columnList = "bottle_id"),
        @Index(name = "quantity", columnList = "quantity")
})
@Entity
public class BottledWine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    @ManyToOne
    @JoinColumn(name = "bottle_id", nullable = false)
    private Bottle bottle;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Wine getWine() {
        return wine;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }

    public Bottle getBottle() {
        return bottle;
    }

    public void setBottle(Bottle bottle) {
        this.bottle = bottle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
