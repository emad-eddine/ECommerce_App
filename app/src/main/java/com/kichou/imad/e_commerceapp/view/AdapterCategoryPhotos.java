package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.clients.ClientChoosesGategory;

import java.util.ArrayList;

public class AdapterCategoryPhotos extends RecyclerView.Adapter<AdapterCategoryPhotos.MyViewHolder> {

    private Context ct;

    private int [] imgLink ;
    private String []imgTitles ;
    private ClientChoosesGategory clientChoosesGategory;

    public AdapterCategoryPhotos(Context ctx , int [] imgLink,String[] imgTitles , ClientChoosesGategory clientChoosesGategory)
    {
        this.ct = ctx;
        this.imgLink= imgLink;
        this.imgTitles = imgTitles;
        this.clientChoosesGategory = clientChoosesGategory;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(ct);
        View view = layoutInflater.inflate(R.layout.category_row_model,parent,false);
        return new AdapterCategoryPhotos.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imageView.setImageResource(imgLink[position]);
        holder.imaTitle.setText(imgTitles[position]);

    }

    @Override
    public int getItemCount() {
        return imgLink.length;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView imaTitle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.catImgView);
            imaTitle = itemView.findViewById(R.id.CategoryTitle);
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clientChoosesGategory.OnchoosingGategroyByClient(imgTitles[getAdapterPosition()]);
                }
            });
        }
    }
}
