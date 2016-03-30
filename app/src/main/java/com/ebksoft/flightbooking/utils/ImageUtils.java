package com.ebksoft.flightbooking.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ebksoft.flightbooking.R;
import com.squareup.picasso.Picasso;

/**
 * Created by chauminhnhut on 1/4/16.
 */
public class ImageUtils {

    public static void load(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Picasso.with(context).load(ConfigAPI.IMAGE_PATH + url).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(imageView);
        else
            imageView.setImageResource(R.drawable.ic_no_image);
    }

    public static void load2(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Picasso.with(context).load(ConfigAPI.IMAGE_PATH_2 + url).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(imageView);
        else
            imageView.setImageResource(R.drawable.ic_no_image);
    }
}
