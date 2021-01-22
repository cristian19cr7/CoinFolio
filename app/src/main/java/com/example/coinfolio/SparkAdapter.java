package com.example.coinfolio;

import android.graphics.RectF;

public class SparkAdapter extends com.robinhood.spark.SparkAdapter {
    private float[] yData;

    public SparkAdapter(float[] yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    @Override
    public boolean hasBaseLine() {
        return false;
    }

    @Override
    public float getX(int index) {
        return super.getX(index);
    }

    @Override
    public RectF getDataBounds() {
        return super.getDataBounds();
    }
}
