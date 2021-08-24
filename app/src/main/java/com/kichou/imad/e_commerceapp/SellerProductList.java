package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.EditProductInterface;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ProductRefImages;
import com.kichou.imad.e_commerceapp.view.AdapterProductListPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SellerProductList extends AppCompatActivity implements EditProductInterface {


    private ImageView backToSellerMainBtn;

    // recycle view

    private RecyclerView sellerProductListsRecycleView;
    private AdapterProductListPhoto adapterProductListPhoto;


    private ProgressBar progressBar;

    private ArrayList<String> imagesUrl = new ArrayList<>();

    private ArrayList<String> productsNames = new ArrayList<>();

    private ArrayList<String> productsPrice = new ArrayList<>();

    private ArrayList<String> productsQte  = new ArrayList<>();

    private ArrayList<String> productKey = new ArrayList<>();
    private ArrayList<ProductRefImages> imagesUrlOfProduct = new ArrayList<>();


    private DatabaseReference sellerRefrence;

    private EditProductInterface editProductInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_list);
        progressBar  = findViewById(R.id.progressBar);

        editProductInterface = this;

        backToSellerMainBtn = findViewById(R.id.backToSellerMain);
        backToSellerMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // recycle view

        sellerProductListsRecycleView = findViewById(R.id.sellerProductListsRecycleView);

        // get data

        sellerRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("products");

        sellerRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {

                    for(DataSnapshot val : task.getResult().getChildren())
                    {
                      // Log.d("TAG1", "onComplete: " + val.getKey());

                       productKey.add(val.getKey());

                        // add images
                        imagesUrl.add(val.child("Images").child("img0").getValue().toString());

                        ProductRefImages productRefImages = new ProductRefImages();
                        ArrayList<String> imagesurlFromdb = new ArrayList<>();
                        productRefImages.setProductKey(val.getKey());

                        for(DataSnapshot imgurl : val.child("Images").getChildren())
                        {
                            //Log.d("TAG1", "onComplete: " + imgurl.getValue().toString());
                            imagesurlFromdb.add(imgurl.getValue().toString());

                        }
                        productRefImages.setProductImagesUrl(imagesurlFromdb);
                        imagesUrlOfProduct.add(productRefImages);

                        // add product name
                        productsNames.add(val.child("pname").getValue().toString());
                        // add price
                        productsPrice.add(val.child("price").getValue().toString());
                        // add qte
                        productsQte.add(val.child("pQte").getValue().toString());
                    }

//                        for (ProductRefImages ref : imagesUrlOfProduct)
//                        {
//                            Log.d("TAG1", "onComplete: " + ref.getProductKey() + " ");
//                            for(String url : ref.getProductImagesUrl())
//                            {
//                                Log.d("TAG1", "onComplete: " + url);
//                            }
//
//                        }



                    sellerProductListsRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterProductListPhoto = new AdapterProductListPhoto(editProductInterface,getApplicationContext(),imagesUrl,productsNames,productsPrice,productsQte,productKey,imagesUrlOfProduct);

                    sellerProductListsRecycleView.setAdapter(adapterProductListPhoto);

                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });



    }


    @Override
    public void onProductEdit(String pKey , String productName, String productPrice, String ProductQte)
    {
        Intent intent = new Intent(SellerProductList.this,EditSellerProduct.class);

        intent.putExtra("pKey",pKey);
        intent.putExtra("pName",productName);
        intent.putExtra("pPrice",productPrice);
        intent.putExtra("pQte",ProductQte);

        startActivity(intent);
    }
}