package com.want.wso2.callback;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.want.wso2.convert.BitmapConvert;

import okhttp3.Response;

/**
 * Created by wisn on 2017/8/22.
 */

public abstract class BitmapCallback extends AbsCallback<Bitmap> {

    private BitmapConvert convert;

    public BitmapCallback() {
        convert = new BitmapConvert();
    }

    public BitmapCallback(int maxWidth, int maxHeight) {
        convert = new BitmapConvert(maxWidth, maxHeight);
    }

    public BitmapCallback(int maxWidth, int maxHeight, Bitmap.Config decodeConfig, ImageView.ScaleType scaleType) {
        convert = new BitmapConvert(maxWidth, maxHeight, decodeConfig, scaleType);
    }

    @Override
    public Bitmap convertResponse(Response response) throws Throwable {
        Bitmap bitmap = convert.convertResponse(response);
        response.close();
        return bitmap;
    }
}
