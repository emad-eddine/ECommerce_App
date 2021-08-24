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

import com.kichou.imad.e_commerceapp.controller.clients.ClientSignUpControll;
import com.kichou.imad.e_commerceapp.controller.clients.ClientSignupController;
import com.kichou.imad.e_commerceapp.view.clientView.ClientSignupView;

public class ClientSignupActivity extends AppCompatActivity implements ClientSignupView {

    private TextView toClientLoginPageBtn, toSellerLoginPage;

    private ProgressDialog loadingBar ;

    private ImageView clientSignupShowPassword;

    // get client signup inputs

    private EditText clientFirstName , clientLastName , clientPhone,clientPassword;

    // signup btn
    private Button clientSignupBtn;

    // controller

    ClientSignupController clientSignupController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup);

        loadingBar = new ProgressDialog(this);

        clientSignupShowPassword = findViewById(R.id.clientSignupImage);

        toClientLoginPageBtn = findViewById(R.id.toClientLoginPage);

        toSellerLoginPage = findViewById(R.id.toSellerLoginPage);

        // find views by id

        clientFirstName = findViewById(R.id.clientFirstNameInput);
        clientLastName = findViewById(R.id.clientLastNameInput);
        clientPhone = findViewById(R.id.clientPhoneInput);
        clientPassword = findViewById(R.id.clientPasswordInput);

        clientSignupBtn = findViewById(R.id.clientSignupBtn);


        //
        clientSignupController = new ClientSignUpControll(this);

        toClientLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientSignupActivity.this,ClientLoginActivity.class);
                startActivity(intent);
            }
        });

        toSellerLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientSignupActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        clientSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSignupController.onClientSignup(clientFirstName.getText().toString().trim()
                        ,clientLastName.getText().toString().trim()
                        ,clientPhone.getText().toString().trim()
                        ,clientPassword.getText().toString()
                        );

                loadingBar.setTitle("Create Client Account");
                loadingBar.setMessage("Please Wait !");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        });

        // show password

        clientSignupShowPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    clientPassword.setTransformationMethod(null);
                    return true;
                }
                else
                {
                    clientPassword.setTransformationMethod(new PasswordTransformationMethod());
                    return true;
                }

            }
        });

    }

    @Override
    public void OnSignupSuccess(String message) {
        loadingBar.dismiss();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ClientSignupActivity.this,ClientLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnSignupError(String message) {

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();

    }
}