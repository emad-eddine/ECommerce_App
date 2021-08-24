package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.utils.ProductDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;

public class SellerAddProductDetails extends AppCompatActivity {

    private Button nextBtn;
    private Button backBtn;
    private ImageView backToSellerMain;

    // edit text and text view

    private TextView productGategory;
    private EditText productPrice,productQte , productAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product_details);

        ViewDialogue alert = new ViewDialogue(this);

        nextBtn = findViewById(R.id.addProdNextBtn);
        backBtn = findViewById(R.id.addProdBackBtn);
        backToSellerMain = findViewById(R.id.backToSellerMain);

        backToSellerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(SellerMainActivity.class);
            }
        });
        // next step
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save data before move to final step

                String price = productPrice.getText().toString().trim();
                String qte = productQte.getText().toString().trim();
                String addr = productAddr.getText().toString();

                if(price.equals("") || qte.equals("") || addr.equals(""))
                {
                    alert.showDialog("tu dois remplir toutes les champs!!");
                }
                else
                {
                    try {
                        ProductDetails.productPrice = Double.parseDouble(productPrice.getText().toString().trim());
                        ProductDetails.productQte = Integer.parseInt(productQte.getText().toString().trim());
                        ProductDetails.productAddress = productAddr.getText().toString();
                        changeActivity(SellerAddProductSummary.class);
                    }
                    catch (NumberFormatException exception)
                    {
                        //Toast.makeText(getBaseContext(),"le prix est qte non valide",Toast.LENGTH_SHORT).show();
                        alert.showDialog("le prix ou qte non valide");
                    }

                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // text view and edit text

        productGategory = findViewById(R.id.product_gategory);
        productGategory.setText(ProductDetails.productGategory);

        productPrice = findViewById(R.id.productPriceInput);

        productQte = findViewById(R.id.productQteInput);

        productAddr = findViewById(R.id.sellerAddressInput);


    }


    private void changeActivity(Class act)
    {
        Intent intent = new Intent(SellerAddProductDetails.this,act) ;
        startActivity(intent);
    }

}