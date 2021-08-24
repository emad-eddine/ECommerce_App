package com.kichou.imad.e_commerceapp.model.sellerModel;

public interface SellerData {

    String getSellerFirstName();
    String getSellerLastName();
    String getSellerPhone();
    String getSellerPassword();

    int isSellerValid();

    int isLoginValid();


}
