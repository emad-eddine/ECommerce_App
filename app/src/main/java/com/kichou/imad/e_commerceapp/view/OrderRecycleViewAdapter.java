package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.R;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderRecycleViewAdapter extends RecyclerView.Adapter<OrderRecycleViewAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<String> client = new ArrayList<>();
    ArrayList<String> addr = new ArrayList<>();
    ArrayList<String> productNames = new ArrayList<>();
    ArrayList<String> productQtes = new ArrayList<>();
    ArrayList<String> productPrices = new ArrayList<>();
    ArrayList<String> orderKey = new ArrayList<>();
    ArrayList<String> productKeys = new ArrayList<>();
    ArrayList<String> clientsKey = new ArrayList<>();
    ArrayList<String> etats = new ArrayList<>();


    public OrderRecycleViewAdapter()
    {

    }



    public OrderRecycleViewAdapter(Context ctx, ArrayList<String> client, ArrayList<String> addr, ArrayList<String> productNames, ArrayList<String> productQtes, ArrayList<String> productPrices, ArrayList<String> orderKey , ArrayList<String> productKeys, ArrayList<String> clientsKey,ArrayList<String> etats) {
        this.ctx = ctx;
        this.client = client;
        this.addr = addr;
        this.productNames = productNames;
        this.productQtes = productQtes;
        this.productPrices = productPrices;
        this.orderKey = orderKey;
        this.productKeys = productKeys;
        this.clientsKey = clientsKey;
        this.etats = etats;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.seller_order_row,parent,false);
        return new OrderRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.client.setText(client.get(position));
        holder.address.setText(addr.get(position));
        holder.productName.setText(productNames.get(position));
        holder.productQte.setText(productQtes.get(position));
        holder.productPrice.setText(productPrices.get(position));



        if(etats.get(position).equals("produit valider"))
        {
            holder.validBtn.setLocked(true);
            holder.validBtn.setOuterColor(R.color.green);
            holder.validBtn.setText("Commande valider");
        }

    }

    public void setClient(ArrayList<String> client) {
        this.client = client;
    }

    public void setAddr(ArrayList<String> addr) {
        this.addr = addr;
    }

    public void setProductNames(ArrayList<String> productNames) {
        this.productNames = productNames;
    }

    public void setProductQtes(ArrayList<String> productQtes) {
        this.productQtes = productQtes;
    }

    public void setProductPrices(ArrayList<String> productPrices) {
        this.productPrices = productPrices;
    }

    public void setOrderKey(ArrayList<String> orderKey) {
        this.orderKey = orderKey;
    }

    public void setProductKeys(ArrayList<String> productKeys) {
        this.productKeys = productKeys;
    }

    public void setClientsKey(ArrayList<String> clientsKey) {
        this.clientsKey = clientsKey;
    }

    @Override
    public int getItemCount() {
        return client.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView client,address,productName,productQte,productPrice;
        private SlideToActView validBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            client = itemView.findViewById(R.id.client);
            address = itemView.findViewById(R.id.addrClient);
            productName = itemView.findViewById(R.id.productName);
            productQte = itemView.findViewById(R.id.productqte);
            productPrice = itemView.findViewById(R.id.producurice);
            validBtn = itemView.findViewById(R.id.validBtsn);

            validBtn.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideToActView slideToActView)
                {
                    int pos = getAdapterPosition();



                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(productKeys.get(pos)).child(orderKey.get(pos)).child("products").child(clientsKey.get(pos));

                   HashMap<String,String> data = new HashMap<>();
                   data.put("etat","produit valider");

                   orderRef.child("etats").setValue("produit valider");



                }
            });


        }
    }
}
