package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.utils.Prevlent;

import okhttp3.internal.Util;

public class EditSellerProfile extends AppCompatActivity {


    private ImageView  backBtn;

    private EditText sellerFirstName , sellerLastName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sellerReffrence;

    private Button editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_profile);

        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        sellerFirstName = findViewById(R.id.clientFirstNameInput);
        sellerLastName  = findViewById(R.id.clientLastNameInput);

        editBtn = findViewById(R.id.clientSignupBtn);

        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // check if inputs are valid

                String firstName = sellerFirstName.getText().toString().trim();
                String lastName = sellerLastName.getText().toString().trim();

                // check if input are empty
                if(firstName.isEmpty() || lastName.isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Champs Vide !!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // check if input are valid

                    if(TextUtils.isDigitsOnly(firstName) || TextUtils.isDigitsOnly(lastName))
                    {
                        Toast.makeText(getBaseContext(),"Champs non Valide !!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // update information
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        sellerReffrence = firebaseDatabase.getReference();
                        sellerReffrence.child("sellers").child(Prevlent.currentSeller).child("seller_first_name").setValue(firstName);
                        sellerReffrence.child("sellers").child(Prevlent.currentSeller).child("seller_last_name").setValue(lastName);
                        Toast.makeText(getBaseContext(),"Profile updated!!",Toast.LENGTH_SHORT).show();

                        Prevlent.sellerFirstName = firstName;
                        Prevlent.sellerLastName = lastName;
                        Intent intent = new Intent(EditSellerProfile.this,SellerMainActivity.class);
                        finish();
                        startActivity(intent);

                    }
                }

            }
        });

    }



}