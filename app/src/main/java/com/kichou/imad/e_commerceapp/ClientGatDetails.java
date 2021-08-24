package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ProductRefImages;
import com.kichou.imad.e_commerceapp.view.AdapterProductListPhoto;
import com.kichou.imad.e_commerceapp.view.ProductDetailsAdapter;

import java.util.ArrayList;

public class ClientGatDetails extends AppCompatActivity implements SeeProductDetails {

    private TextView product_gategory;
    private ImageView backBtn;

    private ProgressBar progressBar;

    private RecyclerView productsRecycleView;
    private ProductDetailsAdapter productDetailsAdapter;

    private ArrayList<String> imagesUrl = new ArrayList<>();

    private ArrayList<String> productsNames = new ArrayList<>();

    private ArrayList<String> productsPrice = new ArrayList<>();

    private ArrayList<String> productsQte  = new ArrayList<>();

    private ArrayList<String> productKey = new ArrayList<>();

    private ArrayList<ProductRefImages> imagesUrlOfProduct = new ArrayList<>();

    private DatabaseReference sellerRefrence;
    private DatabaseReference sellerRefrence2;

    private SeeProductDetails seeProductDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_gat_details);
        seeProductDetails = this;
        progressBar = findViewById(R.id.progressBar3);

        product_gategory = findViewById(R.id.product_gategory);

        String gatName = getIntent().getExtras().getString("gat");
        product_gategory.setText(gatName);

        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        productsRecycleView  = findViewById(R.id.detailsRecy);



        sellerRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers");

        sellerRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {

                    for(DataSnapshot val : task.getResult().getChildren())
                    {

                        sellerRefrence2  = FirebaseDatabase.getInstance().getReference().child("sellers").child(val.getKey()).child("products");

                        sellerRefrence2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task)
                            {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else
                                {
                                    for(DataSnapshot val : task.getResult().getChildren())
                                    {
                                        if(val.child("gategory").getValue().toString().equals(gatName))
                                        {
                                            productKey.add(val.getKey());
                                            //    Log.d("TAG1", "onComplete: " + val.getKey());
                                            // add images
                                            imagesUrl.add(val.child("Images").child("img0").getValue().toString());
                                            //  Log.d("TAG1", "onComplete: " + val.child("Images").child("img0").getValue().toString());
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
                                            // Log.d("TAG1", "onComplete: " + val.child("pname").getValue().toString());
                                            // add price
                                            productsPrice.add(val.child("price").getValue().toString());
                                            // add qte
                                            productsQte.add(val.child("pQte").getValue().toString());
                                        }

                                    }

                                    productsRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                                    productDetailsAdapter = new ProductDetailsAdapter(getApplicationContext(),imagesUrl,productsNames,productsPrice,productsQte,productKey,imagesUrlOfProduct,seeProductDetails);
                                    productsRecycleView.setAdapter(productDetailsAdapter);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }

                        });





                }

            }
        }


        });
}

    @Override
    public void seeDetails(String pKey)
    {
        Intent intent = new Intent(getApplicationContext(),BuyAProductActivity.class);
        intent.putExtra("pKey",pKey);
       // Toast.makeText(this,pKey,Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}