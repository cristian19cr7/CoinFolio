package com.example.coinfolio;

import java.io.Serializable;
import java.util.Comparator;

public class Coin implements Serializable {
    public String name = "";
    public String imageURL = "";
    public String coin_ID = "";
    public int market_cap_rank = 0;
    public String symbol = "";
    public Double current_price = 0.000;
    public Double price_change_percentage_24h = 0.000;


    public static Comparator<Coin> percentSortAscComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c1.getPrice_change_percentage_24h().compareTo(c2.getPrice_change_percentage_24h());
        }
    };

    public static Comparator<Coin> percentSortDescComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c2.getPrice_change_percentage_24h().compareTo(c1.getPrice_change_percentage_24h());
        }
    };

    public static Comparator<Coin> RankSortAscComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c1.getMarket_cap_rank()-c2.getMarket_cap_rank();
        }
    };

    public static Comparator<Coin> RankSortDescComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c2.getMarket_cap_rank()-c1.getMarket_cap_rank();
        }
    };

    public static Comparator<Coin> CoinNameSortAscComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c1.getName().compareTo(c2.getName());
        }
    };

    public static Comparator<Coin> CoinNameSortDescComparator = new Comparator<Coin>() {
        @Override
        public int compare(Coin c1, Coin c2) {
            return c2.getName().compareTo(c1.getName());
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCoin_ID() {
        return coin_ID;
    }

    public void setCoin_ID(String coin_ID) {
        this.coin_ID = coin_ID;
    }

    public int getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Double current_price) {
        this.current_price = current_price;
    }

    public Double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(Double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }
}
