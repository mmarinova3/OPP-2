package com.winery.entities;

import jakarta.persistence.*;

@Table(name = "wine_production_per_kg_grape", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_grape_id", columnList = "grape_id"),
        @Index(name = "yield_per_kg", columnList = "yield_per_kg")
})
@Entity
public class WineProductionPerKgGrape {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "grape_id", nullable = false)
    private GrapeVariety grape;

    @Column(name = "yield_per_kilogram")
    private Double yieldPerKg;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GrapeVariety getGrape() {
        return grape;
    }

    public void setGrape(GrapeVariety grape) {
        this.grape = grape;
    }

    public Double getYieldPerKg() {
        return yieldPerKg;
    }

    public void setYieldPerKg(Double yieldPerKg) {
        this.yieldPerKg = yieldPerKg;
    }
}
