package com.winery.controllers;

import com.winery.entities.Bottle;
import com.winery.entities.GrapeVariety;
import com.winery.entities.WineComponents;
import com.winery.entities.WineComposition;
import com.winery.service.*;
import com.winery.utils.Connection;
import com.winery.utils.Session;

import java.util.Objects;

public class OptimalBottling {

    private final GrapeVarietyService grapeVarietyService;
    private final WineComponentsService wineComponentsService;
    private final WineYieldService wineYieldService;


    public OptimalBottling() {
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(),Session.getInstance());
        this.wineComponentsService= WineComponentsService.getInstance(Connection.getEntityManager(),Session.getInstance());
        this.wineYieldService = WineYieldService.getInstance(Connection.getEntityManager(),Session.getInstance());
    }

    public double getLitersWineInStock(GrapeVariety grapeVariety){

        return grapeVarietyService.findQuantityById(grapeVariety.getId()) * wineYieldService.findYieldPerKgById(grapeVariety);
    }

    public double getMaxOfWineComposition(WineComposition wineComposition) {
        WineComponents wineComponents = new WineComponents();
        wineComponents.setWineComposition(wineComposition);

        double maxLiters = Double.MAX_VALUE;

        for (WineComponents wineComponent : wineComponentsService.getAll()) {
            if (Objects.equals(wineComposition.getId(), wineComponents.getWineComposition().getId())) {
                int grapeId = wineComponent.getGrape().getId();
                GrapeVariety grapeVariety = new GrapeVariety();
                grapeVariety.setId(grapeId);
                grapeVariety.setGrapeName(wineComponent.getGrape().getGrapeName());

                double litersInStock = getLitersWineInStock(grapeVariety) / wineComponent.getQuantityNeeded();

                if (litersInStock < maxLiters) {
                    maxLiters = litersInStock;
                }
            }
        }

        System.out.println("The maximum amount of "+ wineComposition.getWineName() +" is: " + maxLiters + " liters");
        return maxLiters;
    }

    public int getMaxBottling(WineComposition composition, Bottle bottle) {
        double maxLiters = getMaxOfWineComposition(composition);
        return (int) (maxLiters / bottle.getVolume());
    }

    public int getOptimalBottling(WineComposition composition, Bottle bottle) {
        int maxBottling = getMaxBottling(composition, bottle);

        int availableBottles = bottle.getQuantity();
        int bottlesToFill = Math.min(maxBottling, availableBottles);

        double litersUsed = bottlesToFill * bottle.getVolume();

        System.out.println("The bottling in " + bottle.getVolume() + " bottles is " + bottlesToFill +
                " bottles, which is " + litersUsed + " liters of wine used.");

        return bottlesToFill;
    }

    public void updateGrapeQuantity(WineComposition wineComposition, double wineLiters) {
        WineComponents wineComponents = new WineComponents();
        wineComponents.setWineComposition(wineComposition);

        for (WineComponents wineComponent : wineComponentsService.getAll()) {
            if (Objects.equals(wineComposition.getId(), wineComponents.getWineComposition().getId())) {
                int grapeId = wineComponent.getGrape().getId();
                GrapeVariety grapeVariety = new GrapeVariety();
                grapeVariety.setId(grapeId);
                grapeVariety.setGrapeName(wineComponent.getGrape().getGrapeName());


                double yieldPerKg = wineYieldService.findYieldPerKgById(grapeVariety);
                double grapeUsedKg = wineLiters * yieldPerKg;

                int grapeUsed = (int) grapeUsedKg;


                grapeVarietyService.updateQuantityInStockById(grapeId, grapeUsed);
            }
        }
    }




}



