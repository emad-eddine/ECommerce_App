package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginControll;
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginController;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginControll;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginController;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.clientView.ClientLoginView;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerLoginView;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity{

    private Button toClientLoginPageBtn;
    private TextView toClientSignupPage;
    private TextView toSellerLoginPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);


        toClientLoginPageBtn = findViewById(R.id.toClientLoginPage);
        toClientSignupPage = findViewById(R.id.toClientSingupPage);
        toSellerLoginPage = findViewById(R.id.toSellerLoginPage);




        toClientLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,ClientLoginActivity.class);
                finish();
                startActivity(intent);
            }
        });


        toClientSignupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,ClientSignupActivity.class);
                finish();
                startActivity(intent);
            }
        });


        toSellerLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SellerLoginActivity.class);
                finish();
                startActivity(intent);
            }
        });



    }


}