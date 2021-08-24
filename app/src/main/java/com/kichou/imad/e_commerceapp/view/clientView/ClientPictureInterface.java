package com.kichou.imad.e_commerceapp.view.clientView;

import android.net.Uri;

public interface ClientPictureInterface {
    void onPictureChangeSucces(String message , Uri imgUri);
    void onPictureChangeFailed(String message);
}
