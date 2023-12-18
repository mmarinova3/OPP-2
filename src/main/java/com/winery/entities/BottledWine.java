package com.winery.entities;

import jakarta.persistence.*;
import java.util.Date;

@Table(name = "bottled_wine", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "fk_wine_id", columnList = "wine_id"),
        @Index(name = "fk_bottle_id", columnList = "bottle_id"),
        @Index(name = "quantity", columnList = "quantity"),
        @Index(name = "bottling_date", columnList = "bottling_date"),
})
@Entity
public class BottledWine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Id
    @ManyToOne
    @JoinColumn(name = "wine_id", nullable = false)
    private WineComposition wineComposition;
    @Id
    @ManyToOne
    @JoinColumn(name = "bottle_id", nullable = false)
    private Bottle bottle;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Temporal(TemporalType.DATE)
    @Column(name = "bottling_date", nullable = false)
    private Date bottlingDate;

    public WineComposition getWineComposition() {
        return wineComposition;
    }

    public void setWineComposition(WineComposition wineComposition) {
        this.wineComposition = wineComposition;
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

    public Date getBottlingDate() {
        return bottlingDate;
    }

    public void setBottlingDate(Date bottlingDate) {
        this.bottlingDate = bottlingDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}