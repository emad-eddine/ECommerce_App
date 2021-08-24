package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.model.sellerModel.Seller;
import com.kichou.imad.e_commerceapp.utils.Prevlent;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AddProductReview extends AppCompatActivity  {

    private TextView productName;
    private ImageView backBtn;
    private Button validBtn;
    private EditText reviewText;

    private String productKey , pName;

    private RatingBar ratingBar;


    private DatabaseReference reviewRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_review);

        pName = getIntent().getStringExtra("pName");
        productKey = getIntent().getStringExtra("pKey");

        productName = findViewById(R.id.product_Name);
        backBtn = findViewById(R.id.backToSellerMain);
        validBtn = findViewById(R.id.reviewValidBtn);
        reviewText = findViewById(R.id.productReviewInput);

        productName.setText(pName);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // rating bar

        ratingBar = findViewById(R.id.ratingBar);



        // db
        reviewRef = FirebaseDatabase.getInstance().getReference().child("poducts_reviews").child(productKey).child(Prevlent.currentClient);

        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                // add review and finish activity

                HashMap<String,String> dataToInsert = new HashMap<>();

                dataToInsert.put("review",reviewText.getText().toString());
                dataToInsert.put("rate","" + ratingBar.getRating());

                reviewRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        snapshot.getRef().setValue(dataToInsert).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Review Added",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddProductReview.this,BuyAProductActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });




    }


}