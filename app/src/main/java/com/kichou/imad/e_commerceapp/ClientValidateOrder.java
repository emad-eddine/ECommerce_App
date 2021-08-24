package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerAddProductSuccesDIalogue;
import com.kichou.imad.e_commerceapp.utils.OrderDetails;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ViewDialogue;

import java.util.HashMap;

public class ClientValidateOrder extends AppCompatActivity implements SellerAddProductSuccesDIalogue {

    private ImageView backBtn;
    private Button validBtn,PrecedentBtn;
    private TextView orderTotal , productsPrice;

    private float orderSum;
    private OrderDetails orderDetails = new OrderDetails();

    private String addr1,addr2,wilaya,cardName,cardNum,cardDate,cardCvv;
    private int paiementType;


    private ViewDialogue alert;
    private SellerAddProductSuccesDIalogue sellerAddProductSuccesDIalogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_validate_order);

        alert = new ViewDialogue(this);
        sellerAddProductSuccesDIalogue = this;
        // calculate total

        orderDetails = (OrderDetails) getIntent().getExtras().getSerializable("order");
        orderSum =  Float.parseFloat(orderDetails.orderAmount) + 400;


        orderTotal = findViewById(R.id.orderTotal);
        orderTotal.setText(""+orderSum + " DA");

        productsPrice = findViewById(R.id.productsPrice);
        productsPrice.setText(orderDetails.orderAmount + " DA");



        // get extrat

        paiementType = getIntent().getIntExtra("paiementType",0);
        addr1 = getIntent().getStringExtra("addr1");
        addr2 = getIntent().getStringExtra("addr2");
        wilaya = getIntent().getStringExtra("wilaya");

        if(paiementType == 1)
        {
            cardName = getIntent().getStringExtra("cardName");
            cardNum = getIntent().getStringExtra("cardNum");
            cardDate = getIntent().getStringExtra("cardDate");
            cardCvv = getIntent().getStringExtra("cardCvv");
        }






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


        // confurlation dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmer Commande");
        builder.setMessage("Voulez-vous vraiment confirmer la commande");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // confirm the order and add it
                        // 1 add the order to orders
                        // 2 deacrease the qte
                        // 3 clear the cart

                        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevlent.currentClient);

                        HashMap<String,Object> dataToInsert = new HashMap<>();
                        dataToInsert.put("addr1",addr1);
                        dataToInsert.put("addr2",addr2);
                        dataToInsert.put("wilaya",wilaya);
                        dataToInsert.put("total",orderSum);
                        dataToInsert.put("items_amount",orderDetails.orderAmount);
                        dataToInsert.put("liv_amount","400");
                        dataToInsert.put("paiment_type",paiementType);
                        orderRef = orderRef.push();
                        orderRef.setValue(dataToInsert);

                        HashMap<String,Object> products = new HashMap<>();

                        for(int i = 0 ; i < orderDetails.itemKey.size(); i++)
                        {

                            products.put("product_price",orderDetails.itemPrice.get(i));
                            products.put("product_qte",orderDetails.itemQte.get(i));
                            products.put("etats","Encore traitement");
                            orderRef.child("products").child(orderDetails.itemKey.get(i)).setValue(products);

                            int index = i;
                            DatabaseReference orderRef2 = orderRef;
                            //DatabaseReference orderRef3 = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevlent.currentClient);

                            DatabaseReference sellerRef= FirebaseDatabase.getInstance().getReference().child("sellers");
                            sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    for(DataSnapshot val : snapshot.getChildren())
                                    {

                                        for(DataSnapshot val2 : val.child("products").getChildren())
                                        {
                                            if(val2.getKey().equals(orderDetails.itemKey.get(index)))
                                            {
                                                HashMap<String,String> addtoFireBAse = new HashMap<>();


                                                addtoFireBAse.put("seller",val.getKey());


                                                orderRef2.child("products").child(orderDetails.itemKey.get(index)).child("sellersP").setValue(addtoFireBAse);

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            products.clear();

                        }


                        // change qte

                        DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("sellers");

                        pRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(int i= 0 ;i<orderDetails.itemKey.size();i++)
                                {
                                    for(DataSnapshot val : snapshot.getChildren())
                                    {
                                        for(DataSnapshot val2 : val.child("products").getChildren())
                                        {
                                            String key = orderDetails.itemKey.get(i);
                                            if(key.equals(val2.getKey()))
                                            {
                                                // edit the qte
                                                Log.d("TAG", "onDataChange: " );
                                                int newQte =  Integer.parseInt(val2.child("pQte").getValue().toString()) - Integer.parseInt(orderDetails.itemQte.get(i));
                                                val2.child("pQte").getRef().setValue(newQte);
                                            }
                                        }
                                    }
                                }

                                // clear the cart

                                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("client_cart").child(Prevlent.currentClient);

                                cartRef.removeValue();
                                alert.showSucessDialog(sellerAddProductSuccesDIalogue,"Commande enregister");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog dialog = builder.create();

        validBtn = findViewById(R.id.placeOrderBtn);
        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.show();
            }
        });




    }

    @Override
    public void succes()
    {
        Intent intent = new Intent(getBaseContext(),ClientMainActivity.class);
        // Toast.makeText(getBaseContext(),"Commande Confirmer",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(intent);
    }
}