package com.example.coinfolio;

import java.io.Serializable;

public class PortfolioAsset implements Serializable {
    private String NameofAseet;
    private Double AmountofAsset;
    private String AssetID;
    private Double InvestmentOnThisAsset;

    public PortfolioAsset(String nameofAseet, String id, Double amountofAsset, Double invest) {
        NameofAseet = nameofAseet;
        AmountofAsset = amountofAsset;
        AssetID = id;
        InvestmentOnThisAsset = invest;
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
    public void updateInvestmentAmount(Double amount)
    {
        this.InvestmentOnThisAsset += amount;
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
    public Double getInvestmentonThisAsset() {
        return InvestmentOnThisAsset;
    }

    public void setInvestmentonThisAsset(Double investmentonThisAsset) {
        InvestmentOnThisAsset = investmentonThisAsset;
    }
}
