package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ClientOrdersAdapter extends RecyclerView.Adapter<ClientOrdersAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<String> productsName = new ArrayList<>();
    ArrayList<String> productsPrice = new ArrayList<>();
    ArrayList<String> productsQte = new ArrayList<>();
    ArrayList<String> productsEtats = new ArrayList<>();

    public ClientOrdersAdapter() {

    }

    public ClientOrdersAdapter(Context ctx, ArrayList<String> productsName, ArrayList<String> productsPrice, ArrayList<String> productsQte, ArrayList<String> productsEtats) {
        this.ctx = ctx;
        this.productsName = productsName;
        this.productsPrice = productsPrice;
        this.productsQte = productsQte;
        this.productsEtats = productsEtats;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.client_order_row,parent,false);
        return new ClientOrdersAdapter.MyViewHolder(view);
    }

    public void setProductsName(ArrayList<String> productsName) {
        this.productsName = productsName;
    }

    public void setProductsPrice(ArrayList<String> productsPrice) {
        this.productsPrice = productsPrice;
    }

    public void setProductsQte(ArrayList<String> productsQte) {
        this.productsQte = productsQte;
    }

    public void setProductsEtats(ArrayList<String> productsEtats) {
        this.productsEtats = productsEtats;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.productName.setText(productsName.get(position));
        holder.productPrice.setText(productsPrice.get(position));
        holder.productQte.setText(productsQte.get(position));


        String eta = productsEtats.get(position);

        if(eta.equals("produit valider"))
        {

            holder.containerLay.setBackground(ContextCompat.getDrawable(ctx,R.drawable.border_green));
            holder.etats.setText(productsEtats.get(position));
        }
        else
        {
            holder.containerLay.setBackground(ContextCompat.getDrawable(ctx,R.drawable.border_red));
            holder.etats.setText(productsEtats.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return productsName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView etats,productName,productQte,productPrice;
        private LinearLayout containerLay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            etats = itemView.findViewById(R.id.addrClient);
            productName = itemView.findViewById(R.id.productName);
            productQte = itemView.findViewById(R.id.productqte);
            productPrice = itemView.findViewById(R.id.producurice);

            containerLay = itemView.findViewById(R.id.containerLay);


        }
    }
}
