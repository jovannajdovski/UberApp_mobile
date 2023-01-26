package com.example.uberapp_tim12.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    public static String encodeToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return "data:image/png;base64,"+ encoded;
    }

    public static Bitmap decodeToImage(String image) {
        image = image.split(",")[1];
        byte[] decodedString = Base64.decode(image, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
