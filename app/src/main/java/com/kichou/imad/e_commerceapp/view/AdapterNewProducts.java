package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNewProducts  extends RecyclerView.Adapter<AdapterNewProducts.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> productKey;
    private ArrayList<String> imageUrl;
    private ArrayList<String> productName;
    private ArrayList<String> productPrice;
    private SeeProductDetails seeProductDetailsInter;

    public AdapterNewProducts(Context ctx, ArrayList<String> imageUrl, ArrayList<String> productName, ArrayList<String> productPrice,ArrayList<String> productKey,SeeProductDetails seeProductDetailsInter) {
        this.ctx = ctx;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productKey = productKey;
        this.seeProductDetailsInter = seeProductDetailsInter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.new_product_row,parent,false);
        return new AdapterNewProducts.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Picasso.get().load(imageUrl.get(position)).into(holder.productImage);
        holder.productTitle.setText(productName.get(position));
        holder.productPrice.setText(productPrice.get(position));

    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;

       // private LinearLayout pLayout;
        private TextView pLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImg);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            pLayout = itemView.findViewById(R.id.textBtnToDetails);

            pLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seeProductDetailsInter.seeDetails(productKey.get(getAdapterPosition()));
                }
            });


        }
    }
}
