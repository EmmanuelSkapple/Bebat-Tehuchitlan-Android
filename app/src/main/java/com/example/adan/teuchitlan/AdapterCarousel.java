package com.example.adan.teuchitlan;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by adan on 18/05/18.
 */

public class AdapterCarousel extends PagerAdapter {

    ArrayList<String> lstImages;
    Context context;
    LayoutInflater layoutInflater;

    public AdapterCarousel(ArrayList<String> lstImages, Context context) {
        this.lstImages = lstImages;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(lstImages!=null) {
            return lstImages.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.card_item_carousel, container, false);
        try {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewCarousel);
            new DownLoadImageTask(imageView).execute(lstImages.get(position));
            container.addView(view);

        }catch (Exception e){

        }
        return view;
    }
}