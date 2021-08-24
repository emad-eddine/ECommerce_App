package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;

public class AdapterBestProducts extends RecyclerView.Adapter<AdapterBestProducts.MyViewHolder> {

    private Context ctx;
    private int[] imageUrl;
    private String[] productName;
    private String[] productPrice;

    public AdapterBestProducts(Context ctx, int[] imageUrl, String[] productName, String[] productPrice) {
        this.ctx = ctx;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @NonNull
    @Override
    public AdapterBestProducts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.new_product_row, parent, false);
        return new AdapterBestProducts.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBestProducts.MyViewHolder holder, int position) {
        holder.productImage.setImageResource(imageUrl[position]);
        holder.productTitle.setText(productName[position]);
        holder.productPrice.setText(productPrice[position]);

    }

    @Override
    public int getItemCount() {
        return imageUrl.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImg);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);

        }
    }

}

