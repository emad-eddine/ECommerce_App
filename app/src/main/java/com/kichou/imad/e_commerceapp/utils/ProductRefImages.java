package com.kichou.imad.e_commerceapp.utils;

import java.util.ArrayList;

public class ProductRefImages {

    private String productKey;
    private ArrayList<String> productImagesUrl = new ArrayList<>();

    public ProductRefImages() {

    }



    public ProductRefImages(String productKey, ArrayList<String> productImagesUrl) {
        this.productKey = productKey;
        this.productImagesUrl = productImagesUrl;
    }


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public ArrayList<String> getProductImagesUrl() {
        return productImagesUrl;
    }

    public void setProductImagesUrl(ArrayList<String> productImagesUrl) {
        this.productImagesUrl = productImagesUrl;
    }
}
