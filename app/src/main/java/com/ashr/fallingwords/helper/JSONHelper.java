package com.ashr.fallingwords.helper;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class JSONHelper {
    Context context;
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("words_v2.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public JSONHelper(Context context) {
        this.context = context;
    }
}
