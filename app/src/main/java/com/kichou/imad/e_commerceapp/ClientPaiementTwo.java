package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.utils.OrderDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;

public class ClientPaiementTwo extends AppCompatActivity {

    private ImageView backBtn;
    private Button nextBtn,PrecedentBtn;

    private EditText addr1 , addr2 , wilayaInput;

    private ViewDialogue alert;

    private OrderDetails orderDetails = new OrderDetails();
    int paiementType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_paiement_two);
        alert = new ViewDialogue(this);

         paiementType = getIntent().getIntExtra("paiementType",0);
        orderDetails = (OrderDetails) getIntent().getExtras().getSerializable("order");

        addr1 = findViewById(R.id.productNameInput);
        addr2 = findViewById(R.id.productNameInput2);
        wilayaInput = findViewById(R.id.wilayaInput);


        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClientMainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        PrecedentBtn = findViewById(R.id.addProdBackBtn);
        PrecedentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextBtn = findViewById(R.id.addProdNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(paiementType == 0)
                {
                    // no card
                    changeAct(ClientValidateOrder.class);
                }
                else if(paiementType == 1)
                {
                    changeAct(ClientPaiementThree.class);
                }

            }
        });
    }



    private void changeAct(Class act)
    {
        Intent intent = new Intent(getApplicationContext(),act);

        intent.putExtra("paiementType",paiementType);
        intent.putExtra("order",orderDetails);
        String addrOne , addrTwo , addrThree;

        addrOne = addr1.getText().toString().trim();
        addrTwo = addr2.getText().toString().trim();
        addrThree = wilayaInput.getText().toString().trim();

        if(addrOne.equals("") || addrThree.equals(""))
        {
            alert.showDialog("Tu Dois remplir address 1 et wilaya");
        }
        else
            {
            intent.putExtra("addr1",addrOne);
            intent.putExtra("addr2",addrTwo);
            intent.putExtra("wilaya",addrThree);
            finish();
            startActivity(intent);

        }
    }
}