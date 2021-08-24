package com.kichou.imad.e_commerceapp.model.sellerModel;

import android.text.TextUtils;

public class Seller implements SellerData {

    private String sellerFirstName;
    private String sellerLastName;
    private String sellerPhone;
    private String sellerPassword;


    public Seller(String sellerFirstName, String sellerLastName, String sellerPhone, String sellerPassword) {
        this.sellerFirstName = sellerFirstName;
        this.sellerLastName = sellerLastName;
        this.sellerPhone = sellerPhone;
        this.sellerPassword = sellerPassword;
    }

    public Seller(String sellerPhone, String sellerPassword) {
        this.sellerPhone = sellerPhone;
        this.sellerPassword = sellerPassword;
    }


    @Override
    public String getSellerFirstName() {
        return this.sellerFirstName;
    }

    @Override
    public String getSellerLastName() {
        return this.sellerLastName;
    }

    @Override
    public String getSellerPhone() {
        return this.sellerPhone;
    }

    @Override
    public String getSellerPassword() {
        return this.sellerPassword;
    }

    @Override
    public int isSellerValid() {
        // 0. Check for fields empty
        // 1. Check for Password > 6
        // 2 if phone more than 10 degit
        if(TextUtils.isEmpty(getSellerFirstName()) || TextUtils.isEmpty(getSellerLastName())
                || TextUtils.isEmpty(getSellerPhone()) || TextUtils.isEmpty(getSellerPassword()))
            return  0;
        else if(getSellerPassword().length()<=6)
            return 1;
        else if(getSellerPhone().length() > 10)
            return 2;
        else
            return -1;
    }

    @Override
    public int isLoginValid() {
        // 0. Check for fields empty
        // 1. Check for Password > 6
        // 2 if phone more than 10 degit
        // -1 else
        if(TextUtils.isEmpty(getSellerPhone()) || TextUtils.isEmpty(getSellerPassword()))
            return  0;
        else if(getSellerPassword().length()<=6)
            return 1;
        else if(getSellerPhone().length() > 10)
            return 2;
        return -1;
    }
}
