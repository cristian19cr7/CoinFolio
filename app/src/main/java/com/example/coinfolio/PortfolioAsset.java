package com.example.coinfolio;

import java.io.Serializable;

public class PortfolioAsset implements Serializable {
    private String NameofAseet;
    private Double AmountofAsset;
    private String AssetID;

    public PortfolioAsset(String nameofAseet, String id, Double amountofAsset) {
        NameofAseet = nameofAseet;
        AmountofAsset = amountofAsset;
        AssetID = id;
    }
    public PortfolioAsset(PortfolioAsset newPortfolioasset)
    {
        this.NameofAseet = newPortfolioasset.getNameofAseet();
        this.AmountofAsset = newPortfolioasset.getAmountofAsset();
    }

    public PortfolioAsset() {
    }

    public void updateAmount(Double amountToIncreaseBy)
    {
        this.AmountofAsset = this.AmountofAsset + amountToIncreaseBy;
    }

    public void removeAmount(Double amount)
    {
        this.AmountofAsset = this.AmountofAsset - amount;
    }
    public String getNameofAseet() {
        return NameofAseet;
    }

    public void setNameofAseet(String nameofAseet) {
        NameofAseet = nameofAseet;
    }

    public Double getAmountofAsset() {
        return AmountofAsset;
    }

    public void setAmountofAsset(Double amountofAsset) {
        AmountofAsset = amountofAsset;
    }

    public String getAssetID() {
        return AssetID;
    }

    public void setAssetID(String assetID) {
        AssetID = assetID;
    }
}
