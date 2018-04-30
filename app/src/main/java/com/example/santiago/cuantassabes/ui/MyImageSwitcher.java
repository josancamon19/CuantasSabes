package com.example.santiago.cuantassabes.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MyImageSwitcher extends ImageSwitcher {

    public MyImageSwitcher(Context context) {
        super(context);
    }

    public MyImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrl(String url) {
        ImageView image = (ImageView) this.getNextView();
        Picasso.get().load(Uri.parse(url)).into(image);
        showNext();
    }
}
