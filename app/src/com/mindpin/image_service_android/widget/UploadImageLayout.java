package com.mindpin.image_service_android.widget;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.*;
import android.widget.*;
import com.mindpin.image_service_android.R;
import com.mindpin.image_service_android.models.interfaces.IImageData;
import com.mindpin.image_service_android.network.DataProvider;
import com.mindpin.image_service_android.utils.PointEvaluator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-10-13.
 */
public class UploadImageLayout extends RelativeLayout implements View.OnClickListener {
    ImageView iv_image;
    FontAwesomeButton fabtn_copy, fabtn_close;
    FontAwesomeTextView fatv_loading;
    TextView tv_url;
    private String image_path = null;
    private IImageData image_data = null;
    boolean is_first_to_bottom_right = true;//默认调用两次，这里只让它执行一次回调
    private ValueAnimator animator_to_bottom_right;
    private ValueAnimator animator_to_top_left;

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
        fatv_loading = (FontAwesomeTextView) findViewById(R.id.fatv_loading);
        tv_url = (TextView) findViewById(R.id.tv_url);
    }

    private void init_anim() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (is_first_to_bottom_right) {
                    is_first_to_bottom_right = false;

                    to_bottom_right(getWidth(), getHeight(), (LinearLayout.LayoutParams) getLayoutParams());
                }
            }
        });

    }

    private void to_bottom_right(int width, int height, final LinearLayout.LayoutParams layoutParams) {
        Point p0 = new Point(0, 0);
        Point p1 = new Point(width, height);
        animator_to_bottom_right =
                ValueAnimator.ofObject(new PointEvaluator(), p0, p1)
                        .setDuration(1000);
        animator_to_bottom_right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(point.x, point.y);
                params.setMargins(
                        layoutParams.leftMargin, layoutParams.topMargin,
                        layoutParams.rightMargin, layoutParams.bottomMargin
                );
                setLayoutParams(params);
            }
        });
        animator_to_bottom_right.start();
    }


    private void moving_up(int res_id) {
        moving_up(findViewById(res_id));
    }

    private void moving_up(View view) {
        if (view != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).setDuration(500);
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
        }
    }

    public void show_image_info() {
        findViewById(R.id.rl_image).setVisibility(View.VISIBLE);
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
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                if (image_data == null) {
                    hide_loading();
                    top_left_out();
                    Toast.makeText(getContext(), "上传图片失败，请检查网络", Toast.LENGTH_LONG).show();
                } else {
                    show();
                    hide_loading();
                }
            }
        }.execute();

    }

    private void hide_loading() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fatv_loading, "alpha", 1f, 0f).setDuration(200);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeView(fatv_loading);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.start();
    }

    private void top_left_out() {
        animator_to_bottom_right.cancel();
        Point p0 = new Point(getWidth(), getHeight());
        Point p1 = new Point(0, 0);
        animator_to_top_left =
                ValueAnimator.ofObject(new PointEvaluator(), p0, p1)
                        .setDuration(300);
        animator_to_top_left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(point.x, point.y);
                setLayoutParams(params);
            }
        });
        animator_to_top_left.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ((ViewGroup)getParent()).removeView(UploadImageLayout.this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator_to_top_left.start();
    }

    private void show() {
        bind_image_info();
        show_image_info();
    }

    private void bind_image_info() {
        try {
            tv_url.setText(image_data.get_url());
            ImageLoader.getInstance().displayImage(image_data.get_url(), iv_image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_close:
                close();
                break;
            case R.id.fabtn_copy:
            case R.id.tv_url:
                copy();
                break;
        }
    }

    private void close() {
        top_left_out();
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
