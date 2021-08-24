package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kichou.imad.e_commerceapp.utils.OrderDetails;

public class ClientPaiementOne extends AppCompatActivity {


    private ImageView backBtn,homeRadio,cartRadio;

    private Button nextBtn;

    private OrderDetails orderDetails = new OrderDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_paiement_one);
        backBtn = findViewById(R.id.backToSellerMain);
        homeRadio = findViewById(R.id.homeRadio);
        homeRadio.setTag(R.drawable.radio_btn_icon_select);
        cartRadio = findViewById(R.id.cartRadio);
        cartRadio.setTag(R.drawable.radio_btn_not_icon);

        homeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeRadio.setImageResource(R.drawable.radio_btn_icon_select);
                homeRadio.setTag(R.drawable.radio_btn_icon_select);

                cartRadio.setImageResource(R.drawable.radio_btn_not_icon);
                cartRadio.setTag(R.drawable.radio_btn_not_icon);
            }
        });

        cartRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cartRadio.setImageResource(R.drawable.radio_btn_icon_select);
                cartRadio.setTag(R.drawable.radio_btn_icon_select);

                homeRadio.setImageResource(R.drawable.radio_btn_not_icon);
                homeRadio.setTag(R.drawable.radio_btn_not_icon);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClientMainActivity.class);
                finish();
                startActivity(intent);
            }
        });


        orderDetails = (OrderDetails) getIntent().getExtras().getSerializable("order");


        nextBtn = findViewById(R.id.addProdNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 0 for home paiment 1 for cart paiement

                Intent intent = new Intent(getApplicationContext(),ClientPaiementTwo.class);

                if((Integer)homeRadio.getTag() == R.drawable.radio_btn_icon_select)
                {
                    intent.putExtra("paiementType",0);
                    intent.putExtra("order",orderDetails);
                }
                else if((Integer)cartRadio.getTag() == R.drawable.radio_btn_icon_select)
                {
                    intent.putExtra("paiementType",1);
                    intent.putExtra("order",orderDetails);
                }

                startActivity(intent);
            }
        });





    }
}