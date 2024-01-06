package com.winery.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "wine_components", indexes = {
        @Index(name = "fk_grape_id", columnList = "grape_id"),
        @Index(name = "fk_wine_id", columnList = "wine_id"),
        @Index(name = "quantityNeeded", columnList = "quantityNeeded")
})
public class WineComponents {

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "grape_id", nullable = false)
    private GrapeVariety grape;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "wine_id", nullable = false)
    private WineComposition wineComposition;

    @Column(name = "quantityNeeded")
    private double quantityNeeded;


    public GrapeVariety getGrape() {
        return grape;
    }

    public void setGrape(GrapeVariety grape) {
        this.grape = grape;
    }


    public WineComposition getWineComposition() {
        return wineComposition;
    }

    public void setWineComposition(WineComposition wineComposition) {
        this.wineComposition = wineComposition;
    }

    public double getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }


}