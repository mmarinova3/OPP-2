package com.winery.entities;

import jakarta.persistence.*;

@Table(name = "wine_composition", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_grape_id", columnList = "grape_id"),
        @Index(name = "fk_wine_id", columnList = "wine_id")

})
@Entity
public class WineComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "grape_id", nullable = false)
    private GrapeVariety grape;

    @ManyToOne
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    public Integer getId() {
        return id;
    }

    public GrapeVariety getGrape() {
        return grape;
    }

    public void setGrape(GrapeVariety grape) {
        this.grape = grape;
    }

    public Wine getWine() {
        return wine;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }

    public void setId(Integer id) {
        this.id = id;
    }




}
