package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.controller.sellers.RemoveSellerAddProductPhoto;
import com.kichou.imad.e_commerceapp.utils.ProductDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;
import com.kichou.imad.e_commerceapp.view.AdapterProductPhoto;


import java.util.ArrayList;

public class SellerAddProductPhoto extends AppCompatActivity implements RemoveSellerAddProductPhoto {

    private RecyclerView recyclerViewProdPhoto;
    private Button addPhotoBtn;
    private Button nextBtn;
    private Button backBtn;
    private ImageView backToMain;

    private static final int imagePickUpCode= 1000;
    private static final int PermissionCode = 1001;

    private ArrayList<Uri> images = new ArrayList<>();
    AdapterProductPhoto adapterProductPhoto;

    // text views and edit text
    private TextView producygategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product_photo);
        ViewDialogue alert = new ViewDialogue(this);

        producygategory = findViewById(R.id.product_gategory);
        producygategory.setText(ProductDetails.productGategory);
        backToMain = findViewById(R.id.backToSellerMain);
        // back btn to main

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(SellerMainActivity.class);
            }
        });


        // recycle view
        recyclerViewProdPhoto = findViewById(R.id.prodImageRecy);

        recyclerViewProdPhoto.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        nextBtn = findViewById(R.id.addProdNextBtn);
        backBtn = findViewById(R.id.addProdBackBtn);
        addPhotoBtn = findViewById(R.id.AddProdPhotoBtn);
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        //denied so request the permission

                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PermissionCode);
                    }
                    else
                    {   // accepeted
                        pickImageFromGallery();
                    }
                }
                else
                {
                    // os less than marshmellow
                    pickImageFromGallery();
                }
            }

        });

      nextBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // move to next step
              // save photo uri

              if(images.isEmpty())
              {
                  alert.showDialog("Tu Dois entrer au moin une photo du l'article");
              }
              else
              {
                  ProductDetails.productPhotos = images;
                  changeActivity(SellerAddProductDetails.class);
              }

          }
      });

      backBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // changeActivity(SellerAddProductActivity.class);
              finish();
          }
      });

    }

    private void pickImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i,imagePickUpCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == imagePickUpCode)
        {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++)
                        images.add(data.getClipData().getItemAt(i).getUri()) ;
                }
            }
        }
        adapterProductPhoto = new AdapterProductPhoto(this,images,this);
        recyclerViewProdPhoto.setAdapter(adapterProductPhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PermissionCode :
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                    {
                        pickImageFromGallery();
                    }
                    else
                    {
                        Toast.makeText(this,"Pas Droit accés au galerry",Toast.LENGTH_SHORT).show();
                    }
                    break;
        }
    }

    private void changeActivity(Class act)
    {
        Intent intent = new Intent(SellerAddProductPhoto.this,act);
        startActivity(intent);
    }

    @Override
    public void onSellerPhotoProductRemove(int pos)
    {
        //images.remove(pos);
    }
}