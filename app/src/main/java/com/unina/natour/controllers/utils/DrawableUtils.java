package com.unina.natour.controllers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class DrawableUtils {

    public static BitmapDrawable getBitmapWithText(Context context, int drawableId, String text){

        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), drawableId);

        Bitmap bitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,80,80,false);

        if(text != null && text != "") {

            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(36);

            Canvas canvas = new Canvas(scaledBitmap);

            canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2, textPaint);
        }

        return new BitmapDrawable(context.getResources(), scaledBitmap);
    }

}
