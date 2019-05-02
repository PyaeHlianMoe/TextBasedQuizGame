package com.example.quizzer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    //https://www.youtube.com/watch?v=R_AIUy7tFVA

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context){
        this.context = context;
    }

    //list of images
    public int[] slide_images = {
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3
    };

    //list of titles
    public String[] slide_titles = {
        "QUIZZER",
        "Our Mission",
        "Our Content"
    };

    //list of description
    public String[] slides_description = {
        "An application for\nNanyang Technological Univeristy\nComputer Science Student ",
        "Understand SDLC in the fun way!",
        "Waterfall SDLC \nAgile SDLC\nand more..."
    };


    @Override
    public int getCount() {
        return slide_titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_pic);
        TextView slideTitles = (TextView) view.findViewById(R.id.slide_titles);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideTitles.setText(slide_titles[position]);
        slideDescription.setText(slides_description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
