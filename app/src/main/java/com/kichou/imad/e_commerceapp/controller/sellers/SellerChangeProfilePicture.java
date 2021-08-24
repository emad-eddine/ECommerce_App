package com.kichou.imad.e_commerceapp.controller.sellers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ProductDetails;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerPictureInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class SellerChangeProfilePicture implements SellerPictureChanger{

    private SellerPictureInterface sellerPictureInterface;
    private Context context;


    private  String saveCurrentDate, saveCurrentTime;
    private String productRandomKey;


    String userPhoneKey = Prevlent.currentSeller;
    private DatabaseReference SellerRef = FirebaseDatabase.getInstance().getReference().child("sellers").child(userPhoneKey);

    public SellerChangeProfilePicture(SellerPictureInterface sellerPictureInterface, Context context)
    {
        this.sellerPictureInterface = sellerPictureInterface;
        this.context = context;
        Paper.init(context);
    }


    @Override
    public void onSellerProfilePictureChnage(Uri imageUri)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference imageUrlRef = FirebaseStorage.getInstance().getReference().child("seller_Profile_images").child(userPhoneKey);
        StorageReference imagePath;

        // delete current user image

        imageUrlRef.delete();

        imagePath = imageUrlRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = imagePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                sellerPictureInterface.onPictureChangeFailed("failed to upload profile picture");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                if (taskSnapshot.getMetadata() != null)
                {
                    if (taskSnapshot.getMetadata().getReference() != null)
                    {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                String imageUrl = uri.toString();

                                // save data to seller

                                HashMap<String, Object> profileImg = new HashMap<>();
                                profileImg.put("profile_img",imageUrl);
                                SellerRef.child("profile_img").updateChildren(profileImg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            sellerPictureInterface.onPictureChangeSucces("picture added",imageUri);
                                        }
                                        else
                                            sellerPictureInterface.onPictureChangeFailed("failed to upload profile picture");
                                    }
                                });


                            }
                        });
                    }
                }
            }
        });


    }




}





