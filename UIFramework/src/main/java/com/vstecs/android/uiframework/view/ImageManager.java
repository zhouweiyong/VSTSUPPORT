package com.vstecs.android.uiframework.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.vstecs.android.funframework.ui.activity.BaseActivity;
import com.vstecs.android.uiframework.R;

import java.io.File;
import java.io.IOException;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/4/12
 * class description:请输入类描述
 */
public class ImageManager {

    public static void loadImage(ImageView iv, String url) {
        Glide.with(iv.getContext())
                .load(url)
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(iv);
    }

    public static File getCachePath(Context context) {
        return Glide.getPhotoCacheDir(context);
    }
}
