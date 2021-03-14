package com.example.coinfolio;

import java.io.Serializable;

public class Coin implements Serializable {
    public String name = "";
    public String imageURL = "";
    public String coin_ID = "";
    public int market_cap_rank = 0;
    public String symbol = "";
    public Double current_price = 0.000;
    public Double price_change_percentage_24h = 0.000;
}
