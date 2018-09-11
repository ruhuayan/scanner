package com.richyan.android.textscanner;

public class Item {
    private String  mark, product, tag, unit;
    private float measurement;

    public Item(String mark, String product, String tag, String unit, float measurement) {
        this.mark = mark;
        this.product = product;
        this.tag = tag;
        this.unit = unit;
        this.measurement = measurement;
    }

    public String getProduct(){return product;}

    public String getTag() {
        return tag;
    }

    public String getUnit() {
        return unit;
    }

    public float getMeasurement() {
        return measurement;
    }

}
