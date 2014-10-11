package com.mindpin.image_service_android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
import com.mindpin.image_service_android.utils.UiFont;

public class FuturaCondensedTextView extends TextView {

    public FuturaCondensedTextView(Context context) {
        super(context);
        init();
    }

    public FuturaCondensedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FuturaCondensedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(UiFont.FUTURACONDENSED_FONT);
        }
        setGravity(Gravity.CENTER);
    }
}