package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.controller.sellers.SellerAddProductControll;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerAddProductController;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerAddProductSuccesDIalogue;
import com.kichou.imad.e_commerceapp.utils.ProductDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;
import com.kichou.imad.e_commerceapp.view.AdapterProductPhotosSummary;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerAddProductView;

import java.util.ArrayList;

public class SellerAddProductSummary extends AppCompatActivity implements SellerAddProductView, SellerAddProductSuccesDIalogue {

    private ImageView backToSellerMain;
    private Button submitBtn , backBtn ;

    private TextView productGategory,productName,productPrice,productQte,productAddr;

    private RecyclerView recyclerView;
    private AdapterProductPhotosSummary adapterProductPhotosSummary;

    private ArrayList<Uri> images = new ArrayList<>();


    // interface for add product

    private SellerAddProductControll sellerAddProductControll;
    private ProgressDialog progressDialog;


    private ViewDialogue viewDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product_summary);
        viewDialogue = new ViewDialogue(this);
        images = ProductDetails.productPhotos;
        productGategory = findViewById(R.id.product_gategory);
        productGategory.setText(ProductDetails.productGategory + "/Sommaire");

        productName = findViewById(R.id.product_name);
        productName.setText(ProductDetails.productName + " / Type : " + ProductDetails.productType);

        productPrice = findViewById(R.id.product_price);
        productPrice.setText(""+ProductDetails.productPrice + " DA");

        productQte = findViewById(R.id.product_qte);
        productQte.setText(""+ProductDetails.productQte);

        productAddr = findViewById(R.id.product_addr);
        productAddr.setText(ProductDetails.productAddress);

        backToSellerMain = findViewById(R.id.backToSellerMain);
        submitBtn = findViewById(R.id.addProdNextBtn);
        backBtn = findViewById(R.id.addProdBackBtn);


        backToSellerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(SellerMainActivity.class);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // recycle views

        recyclerView = findViewById(R.id.product_summary_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        // adapter
        adapterProductPhotosSummary = new AdapterProductPhotosSummary(this,images);
        recyclerView.setAdapter(adapterProductPhotosSummary);

        //submit


        sellerAddProductControll = new SellerAddProductController(this,this);
        progressDialog = new ProgressDialog(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add product

                sellerAddProductControll.onSellerAddProduct();

                progressDialog.setTitle("Adding Your Product");
                progressDialog.setMessage("Please Wait....!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        });






    }



    private void changeActivity(Class act)
    {
        Intent intent = new Intent(SellerAddProductSummary.this,act) ;
        finish();
        startActivity(intent);
    }

    @Override
    public void onAddProductSuccess(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
       viewDialogue.showSucessDialog(this,"Votre produit a été ajouter avec success");

    }

    @Override
    public void onAddProductFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        viewDialogue.showDialog("Erreur lors l'ajoute du votre produit");
    }

    @Override
    public void succes()
    {
        changeActivity(SellerMainActivity.class);
    }
}