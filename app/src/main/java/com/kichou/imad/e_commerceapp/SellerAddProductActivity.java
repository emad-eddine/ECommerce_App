package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.utils.ProductDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;

public class SellerAddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView gategoryType;

    private ImageView backBtn;

    private Button addProdNextBtn;
    private Button backToMainSellerBtn;

    private EditText productName ,productSpec;
    private Spinner productType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);
        ViewDialogue alert = new ViewDialogue(this);


        gategoryType = findViewById(R.id.product_gategory);
        backBtn = findViewById(R.id.backToSellerMain);
        addProdNextBtn = findViewById(R.id.addProdNextBtn);

        productName = findViewById(R.id.productNameInput);
        productType = findViewById(R.id.productTypeInput);
        productSpec = findViewById(R.id.productSpecefication);


        productType.setOnItemSelectedListener(this);
        // complete init
        String gategory = getIntent().getExtras().get("gategory").toString();
        // add to class
        ProductDetails.productGategory = gategory;
        gategoryType.setText(gategory);

        // spinner & adapter

        int resourceId ;
        if(gategory.equals("Homme")) {
            resourceId = R.array.homme_type;
        }
        else if(gategory.equals("Femme"))
        {
            resourceId = R.array.femme_type;
        }
        else
            resourceId = R.array.other_type;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,resourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productType.setAdapter(adapter);

        // btns clicked

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddProductActivity.this,SellerMainActivity.class);
                startActivity(intent);
            }
        });

        addProdNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerAddProductActivity.this,SellerAddProductPhoto.class);

                // save user input

                String proName = productName.getText().toString().trim();
                String proDesc = productSpec.getText().toString();

                if(proName.equals("") || proDesc.equals(""))
                {
                    alert.showDialog("Toutes les champ doivent etre remplir !!");
                }
                else
                {
                    ProductDetails.productName = productName.getText().toString().trim();
                    ProductDetails.productDescription = productSpec.getText().toString();
                    startActivity(intent);
                }




            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
        ProductDetails.productType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}