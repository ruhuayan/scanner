package com.richyan.android.textscanner;

public class Article {
    private String barcode, mark, product, tag, unit, purchaseDate;
    private float measure, price;
    private int purchaseNumber;

    public Article(){
    }

    public Article(String mark, String product, String tag, String unit, float measure ){
        this.mark = mark;
        this.product = product;
        this.tag = tag;
        this.unit = unit;
        this.measure = measure;
    }
    public Article(String mark, String product, String tag, String unit, float measure, float price ){
        this.mark = mark;
        this.product = product;
        this.tag = tag;
        this.unit = unit;
        this.measure = measure;
        this.price = price;
    }

    public Article(String mark, String product, String tag, String unit, float measure, String purchaseDate, int purchaseNumber, float price ){
        this.mark = mark;
        this.product = product;
        this.tag = tag;
        this.unit = unit;
        this.measure = measure;
        this.price = price;
    }


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public float getMeasure() {
        return measure;
    }

    public void setMeasure(float measure) {
        this.measure = measure;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(int purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
}
