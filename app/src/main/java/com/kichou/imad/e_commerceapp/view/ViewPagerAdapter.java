package com.kichou.imad.e_commerceapp.view;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter
{

    private Context ctx;
    private ArrayList<String> imagesUrl = new ArrayList<>();

    public ViewPagerAdapter(Context ctx , ArrayList<String> images)
    {
        this.ctx = ctx;
        this.imagesUrl = images;
    }


    @Override
    public int getCount()
    {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(this.ctx);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(imagesUrl.get(position)).into(imageView);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
