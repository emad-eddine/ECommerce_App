package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.utils.Prevlent;

public class EditSellerProduct extends AppCompatActivity {


    private EditText pName , pPrice , pQte;
    private Button editBtn;

    private ImageView backBtn;

    private AlertDialog.Builder builder ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_product);

        pName = findViewById(R.id.productNameInput);
        pPrice = findViewById(R.id.productPriceInput);
        pQte = findViewById(R.id.productQteInput);
        editBtn = findViewById(R.id.editProductBtn);
        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pName.setText(getIntent().getExtras().getString("pName"));
        pPrice.setText(getIntent().getExtras().getString("pPrice"));
        pQte.setText(getIntent().getExtras().getString("pQte"));


        builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Voulez-vous vraiment modifier le produti");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        String productName = pName.getText().toString();
                        String productPrice = pPrice.getText().toString();
                        String productQte = pQte.getText().toString();
                        String pKey = getIntent().getExtras().getString("pKey");

                        DatabaseReference pProdRef = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("products").child(pKey);
                        pProdRef.child("pname").setValue(productName);
                        pProdRef.child("price").setValue(productPrice);
                        pProdRef.child("pQte").setValue(productQte);
                        Toast.makeText(getBaseContext(),"Produit modifier avec succee",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditSellerProduct.this,SellerProductList.class);
                        finish();
                        startActivity(intent);

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog dialog = builder.create();



        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.show();


            }
        });

    }
}