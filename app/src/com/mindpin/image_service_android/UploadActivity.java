package com.mindpin.image_service_android;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mindpin.image_service_android.widget.UploadImageLayout;
import roboguice.activity.RoboActivity;

/**
 * Created by dd on 14-10-11.
 */
public class UploadActivity extends RoboActivity implements View.OnClickListener {
    private LinearLayout ll_photos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        init();
    }

    private void init() {
        findViewById(R.id.fabtn_camera).setOnClickListener(this);
        findViewById(R.id.fabtn_image).setOnClickListener(this);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_camera:
                // todo get from camera
                upload_image("1");
                break;
            case R.id.fabtn_image:
                choose_image();
                break;
        }
    }

    private void upload_image(String image_path) {
        UploadImageLayout uploadImageLayout = new UploadImageLayout(this, image_path);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 5);
        ll_photos.addView(uploadImageLayout, ll_photos.getChildCount() - 1, params);
        uploadImageLayout.upload();

        reset_ll_photos_padding_top();
        ll_photos.requestLayout();
    }

    private void reset_ll_photos_padding_top() {
        if(ll_photos.getChildCount() > 1){
            ll_photos.setPadding(0 ,5, 0, 0);
        }
        else
            ll_photos.setPadding(0 ,0, 0, 0);
    }

    private void choose_image() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Constants.Request.RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Request.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            upload_image(picturePath);
        }
    }
}