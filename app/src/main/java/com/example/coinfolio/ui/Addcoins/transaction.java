package com.example.coinfolio.ui.Addcoins;

import java.io.Serializable;

public class transaction implements Serializable {
    double assetAmount;
    double investmentAmount;
    String assetName;
    String assetID;
    double boughtPrice;
    public transaction(double amount, double investment, String name, String id)
    {
        assetAmount = amount;
        investmentAmount = investment;
        assetName = name;
        assetID = id;
    }
    transaction()
    {
        assetAmount = 0.0;
        investmentAmount = 0.0;
        assetName = "";
        assetID = "";
    }

    public double getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(double assetAmount) {
        this.assetAmount = assetAmount;
    }

    public double getInvestmentAmount() {
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

    public double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }
}
