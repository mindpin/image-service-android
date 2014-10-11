package com.mindpin.image_service_android.utils;

import android.graphics.Typeface;
import com.mindpin.image_service_android.KCApplication;

/**
 * Created by fushang318 on 2014/8/20.
 */
public class UiFont {
    final static public Typeface FONTAWESOME_FONT = Typeface.createFromAsset(
            KCApplication.get_context().getAssets(),
            "fonts/fontawesome-webfont.ttf");

    final static public Typeface FUTURACONDENSED_FONT = Typeface.createFromAsset(
            KCApplication.get_context().getAssets(),
            "fonts/futura-condensed.ttf");
}
