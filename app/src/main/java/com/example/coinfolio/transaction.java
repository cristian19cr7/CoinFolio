package com.example.coinfolio;

import java.io.Serializable;

public class transaction implements Serializable {
    public Double assetAmount;
    public Double investmentAmount;
    public String assetName;
    public String assetID;
    public Double boughtPrice;


    public transaction(Double amount, Double investment, String name, String id)
    {
        assetAmount = amount;
        investmentAmount = investment;
        assetName = name;
        assetID = id;
        boughtPrice = investment/amount;
    }
    public transaction()
    {
        assetAmount = 0.0;
        investmentAmount = 0.0;
        assetName = "";
        assetID = "";
        boughtPrice = 0.00;
    }

    public Double getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(double assetAmount) {
        this.assetAmount = assetAmount;
    }

    public Double getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(double investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }
}
