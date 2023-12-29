package com.winery.entities;

import jakarta.persistence.*;

import java.util.Objects;


@Table(name = "wine_yield", indexes = {
        @Index(name = "fk_grape_id", columnList = "grape_id"),
        @Index(name = "yield_per_kg", columnList = "yield_per_kg")
})
@Entity
public class WineYield {
    @Id
    @OneToOne
    @JoinColumn(name = "grape_id", nullable = false)
    private GrapeVariety grape;

    @Column(name = "yield_per_kilogram")
    private Double yieldPerKg;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WineYield wineYield = (WineYield) o;
        return Objects.equals(getGrape(), wineYield.getGrape());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGrape());
    }

}
