package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.controller.sellers.SellerSignUpControll;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerSignupController;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerSignupView;

public class SellerSignupActivity extends AppCompatActivity implements SellerSignupView {


    private TextView toSellerLoginPage , toClientLoginPage;

    private ProgressDialog loadingBar ;

    private EditText sellerFirstName , sellerLastName , sellerPhone,sellerPassword;

    // show password if click on eye icon

    private ImageView showPassword;

    // signuo btn
    private Button sellerSignupBtn;

    // controller
    SellerSignupController sellerSignupController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_signup);

        loadingBar = new ProgressDialog(this);

        showPassword = findViewById(R.id.sellerPasswordView);
        //
        toSellerLoginPage = findViewById(R.id.toSellerLoginPage);
        toClientLoginPage = findViewById(R.id.toClientLoginPage);

        // findedittext

        sellerFirstName = findViewById(R.id.sellerFirstName);
        sellerLastName = findViewById(R.id.sellerLastName);
        sellerPhone = findViewById(R.id.sellerPhone);
        sellerPassword = findViewById(R.id.sellerPassword);

        sellerSignupBtn = findViewById(R.id.sellerSignupBtn);

        sellerSignupController = new SellerSignUpControll(this);

        toSellerLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerSignupActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        toClientLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerSignupActivity.this,ClientLoginActivity.class);
                startActivity(intent);
            }
        });

        // signup btn

        sellerSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellerSignupController.onSellerSignup(sellerFirstName.getText().toString().trim(),
                        sellerLastName.getText().toString().trim()
                        ,sellerPhone.getText().toString().trim()
                        ,sellerPassword.getText().toString().trim());

                loadingBar.setTitle("Create Seller Account");
                loadingBar.setMessage("Please Wait !");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        });

        // show password when eye image is pressed
        showPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    sellerPassword.setTransformationMethod(null);
                    return true;
                }
                else
                {
                    sellerPassword.setTransformationMethod(new PasswordTransformationMethod());
                    return true;
                }

            }
        });

    }

    @Override
    public void OnSignupSuccess(String message) {
        loadingBar.dismiss();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SellerSignupActivity.this,SellerLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnSignupError(String message) {

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
    }
}