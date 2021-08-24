package com.kichou.imad.e_commerceapp.view.sellerView;

import android.net.Uri;

public interface SellerPictureInterface {


    void onPictureChangeSucces(String message , Uri imgUri);
    void onPictureChangeFailed(String message);


}
