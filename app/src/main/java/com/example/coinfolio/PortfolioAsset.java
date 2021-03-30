package com.example.coinfolio;

public class PortfolioAsset {
    private String NameofAseet;
    private Double AmountofAsset;

    public PortfolioAsset(String nameofAseet, Double amountofAsset) {
        NameofAseet = nameofAseet;
        AmountofAsset = amountofAsset;
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
}
