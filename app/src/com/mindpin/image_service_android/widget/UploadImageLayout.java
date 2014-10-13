package com.mindpin.image_service_android.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mindpin.image_service_android.R;
import com.mindpin.image_service_android.models.interfaces.IImageData;
import com.mindpin.image_service_android.network.DataProvider;
import roboguice.util.RoboAsyncTask;

import java.util.Random;

/**
 * Created by dd on 14-10-13.
 */
public class UploadImageLayout extends RelativeLayout implements View.OnClickListener {
    ImageView iv_image;
    FontAwesomeButton fabtn_copy, fabtn_close;
    TextView tv_url;
    private String image_path = null;
    private IImageData image_data = null;
    private Animation operatingAnim;
    private LinearInterpolator lin;

    public UploadImageLayout(Context context, String image_path) {
        super(context);
        this.image_path = image_path;
        init();
    }

    public UploadImageLayout(Context context) {
        super(context);
        init();
    }

    public UploadImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadImageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.upload_image, this, true);
        find_views();
        bind_views();

        init_anim();
        moving_up(R.id.fatv_loading);
    }

    private void bind_views() {
        fabtn_close.setOnClickListener(this);
        fabtn_copy.setOnClickListener(this);
        tv_url.setOnClickListener(this);
    }

    private void find_views() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        fabtn_copy = (FontAwesomeButton) findViewById(R.id.fabtn_copy);
        fabtn_close = (FontAwesomeButton) findViewById(R.id.fabtn_close);
        tv_url = (TextView) findViewById(R.id.tv_url);
    }

    private void init_anim() {
        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }


    private void moving_up(int res_id) {
        moving_up(findViewById(res_id));
    }

    private void moving_up(View view) {
        if (view != null) {
            view.startAnimation(operatingAnim);
        }
    }

    public void show_image_info() {
        findViewById(R.id.rl_image).setVisibility(View.VISIBLE);
        findViewById(R.id.fatv_loading).setVisibility(View.GONE);
        findViewById(R.id.fatv_loading).setAnimation(null);
    }

    public void upload() {
        if (image_path == null) {
            System.out.println("not image_path cancel upload");
            return;
        }
        new RoboAsyncTask<Void>(getContext()) {

            @Override
            public Void call() throws Exception {
                image_data = DataProvider.upload(image_path);
                Thread.sleep(500 + new Random().nextInt(3000));
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                show();
            }
        }.execute();

    }

    private void show() {
        bind_image_info();
        show_image_info();
    }

    private void bind_image_info() {
        try {
            tv_url.setText(image_data.get_url());
            iv_image.setImageBitmap(BitmapFactory.decodeFile(image_path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_close:
                this.setVisibility(GONE);
                break;
            case R.id.fabtn_copy:
            case R.id.tv_url:
                copy();
                break;
        }
    }

    private void copy() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("url", image_data.get_url());
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard =
                    (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(image_data.get_url());
        }
        Toast.makeText(getContext().getApplicationContext(), "已成功复制url", Toast.LENGTH_SHORT).show();
    }
}
