package com.kichou.imad.e_commerceapp.controller.sellers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
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
import com.kichou.imad.e_commerceapp.view.sellerView.SellerAddProductView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class SellerAddProductController implements SellerAddProductControll {

    private SellerAddProductView sellerAddProductView;
    private Context context;

    private ArrayList<String> imagesUrl = new ArrayList<>();
    private  String saveCurrentDate, saveCurrentTime;
    private String productRandomKey;

    String userPhoneKey = Prevlent.currentSeller;

    private  DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("sellers").child(userPhoneKey);

    public SellerAddProductController(SellerAddProductView sellerAddProductView, Context context) {
        this.sellerAddProductView = sellerAddProductView;
        this.context = context;
        Paper.init(context);
    }


    @Override
    public void onSellerAddProduct()
    {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference imageUrlRef = FirebaseStorage.getInstance().getReference().child("Products");

        StorageReference imagePath;


        for (Uri img : ProductDetails.productPhotos)
        {
            imagePath = imageUrlRef.child(img.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask = imagePath.putFile(img);

            uploadTask.addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    sellerAddProductView.onAddProductFailed("Some Error Occurrs");
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
                                    imagesUrl.add(imageUrl);
                                    Log.d("TAG", "onSuccess: " + imageUrl);
                                    if(imagesUrl.size() == ProductDetails.productPhotos.size())
                                    {
                                        saveProductDataToDb();
                                    }

                                }
                            });
                        }
                    }
                }
            });
        }
    }


        private void saveProductDataToDb()
        {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> imagesMap = new HashMap<>();

            productMap.put("pid", productRandomKey);
            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("description", ProductDetails.productDescription);
            productMap.put("gategory", ProductDetails.productGategory);
            productMap.put("price", ProductDetails.productPrice);
            productMap.put("pname", ProductDetails.productName);
            productMap.put("pQte", ProductDetails.productQte);
            productMap.put("pAddress", ProductDetails.productAddress);
            productMap.put("pType", ProductDetails.productType);
            int index = 0;
            for(String url : imagesUrl)
            {
                imagesMap.put("img" + index,url);
                index = index + 1;
            }

            ProductsRef.child("products").child(productRandomKey).updateChildren(productMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ProductsRef.child("products").child(productRandomKey).child("Images").updateChildren(imagesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                sellerAddProductView.onAddProductSuccess("product Added");
                            }
                            else
                            {
                                sellerAddProductView.onAddProductFailed("failed");
                            }
                        }
                    });
                }
            });

        }

}
