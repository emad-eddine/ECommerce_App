package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kichou.imad.e_commerceapp.utils.OrderDetails;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;

public class ClientPaiementThree extends AppCompatActivity {
    private ImageView backBtn;
    private Button nextBtn,PrecedentBtn;



    String addr1 , addr2 , wilaya;
    int paiementType;
    private OrderDetails orderDetails = new OrderDetails();

    private EditText cardName , cardNum,cardDate,cardCvv;

    private ViewDialogue alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_paiement_three);
        alert = new ViewDialogue(this);

        orderDetails = (OrderDetails) getIntent().getExtras().getSerializable("order");
        paiementType = getIntent().getIntExtra("paiementType",0);
        addr1 = getIntent().getStringExtra("addr1");
        addr2 = getIntent().getStringExtra("addr2");
        wilaya = getIntent().getStringExtra("wilaya");


       // Log.d("TAG", "onCreate: " + addr1 + " " + addr2 + " " + wilaya + " " + paiementType);

        cardName = findViewById(R.id.productNameInput);
        cardNum = findViewById(R.id.productNameInput2);
        cardDate = findViewById(R.id.dateInput);
        cardCvv = findViewById(R.id.cvvInput);

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
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(),ClientValidateOrder.class);

                intent.putExtra("paiementType",paiementType);
                intent.putExtra("order",orderDetails);

                intent.putExtra("addr1",addr1);
                intent.putExtra("addr2",addr2);
                intent.putExtra("wilaya",wilaya);


                String name,num,date,cvv;

                name = cardName.getText().toString();
                num = cardNum.getText().toString();
                date = cardDate.getText().toString();
                cvv = cardCvv.getText().toString();

                if(name.equals("")||num.equals("")||date.equals("")||cvv.equals(""))
                {
                    alert.showDialog("Remplir tu les champs");
                }
                else
                {
                    intent.putExtra("cardName",name);
                    intent.putExtra("cardNum",num);
                    intent.putExtra("cardDate",date);
                    intent.putExtra("cardCvv",cvv);


                    startActivity(intent);
                }


            }
        });


    }
}