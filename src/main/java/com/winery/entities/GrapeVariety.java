package com.winery.entities;

import jakarta.persistence.*;

@Table(name = "grape_variety", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "grape_name", columnList = "grape_name"),
        @Index(name = "category", columnList = "category"),
        @Index(name = "quantity", columnList = "quantity")
})
@Entity
public class GrapeVariety {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "grape_name", nullable = false)
    private String grapeName;
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private GrapeCategory category;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    public String getGrapeName() {
        return grapeName;
    }

    public void setGrapeName(String grapeName) {
        this.grapeName = grapeName;
    }

    public GrapeCategory getCategory() {
        return category;
    }

    public void setCategory(GrapeCategory category) {
        this.category = category;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
