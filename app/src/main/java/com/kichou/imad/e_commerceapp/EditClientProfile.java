package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.utils.Prevlent;

public class EditClientProfile extends AppCompatActivity {

    private ImageView backBtn;

    private EditText clientFirstName , clientLastName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference clientReffrence;

    private Button editBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_profile);
        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        clientFirstName = findViewById(R.id.clientFirstNameInput);
        clientLastName  = findViewById(R.id.clientLastNameInput);

        editBtn = findViewById(R.id.clientSignupBtn);

        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // check if inputs are valid

                String firstName = clientFirstName.getText().toString().trim();
                String lastName = clientLastName.getText().toString().trim();

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
                        clientReffrence = firebaseDatabase.getReference();
                        clientReffrence.child("clients").child(Prevlent.currentClient).child("client_first_name").setValue(firstName);
                        clientReffrence.child("clients").child(Prevlent.currentClient).child("client_last_name").setValue(lastName);

                        Prevlent.clientFirstName = firstName;
                        Prevlent.clientLastName = lastName;

                        Toast.makeText(getBaseContext(),"Profile updated!!",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditClientProfile.this,ClientMainActivity.class);
                        finish();
                        startActivity(intent);

                    }
                }

            }
        });

    }
}