package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;

import java.util.ArrayList;

public class AdapterProductPhotosSummary extends RecyclerView.Adapter<AdapterProductPhotosSummary.MyViewHolder> {

    private Context ctx;
    private ArrayList<Uri> images;

    public AdapterProductPhotosSummary(Context ctx , ArrayList<Uri> images)
    {
        this.ctx = ctx;
        this.images =  images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.product_photos_summary_row,parent,false);
        return new AdapterProductPhotosSummary.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imageView.setImageURI(images.get(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.proImgView);

        }
    }
}
