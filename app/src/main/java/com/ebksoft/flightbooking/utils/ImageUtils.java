package com.ebksoft.flightbooking.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by chauminhnhut on 1/4/16.
 */
public class ImageUtils {

    public static void load(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Picasso.with(context).load(ConfigAPI.IMAGE_PATH + url).into(imageView);
    }
}
