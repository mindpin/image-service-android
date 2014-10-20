package com.mindpin.image_service_android;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mindpin.image_service_android.utils.BaseUtils;
import com.mindpin.image_service_android.widget.UploadImageLayout;
import roboguice.activity.RoboActivity;

import java.io.File;

/**
 * Created by dd on 14-10-11.
 */
public class UploadActivity extends RoboActivity implements View.OnClickListener {
    private LinearLayout ll_photos;
    private File cache_dir;
    private String filename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        init();
    }

    private void init() {
        cache_dir = BaseUtils.create_dir(new File("/kc/cache/image"));
        findViewById(R.id.fabtn_camera).setOnClickListener(this);
        findViewById(R.id.fabtn_image).setOnClickListener(this);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        int dp80 = BaseUtils.dp_to_px(80);
        UploadImageLayout.set_image_size(dp80);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_camera:
                take_photo();
                break;
            case R.id.fabtn_image:
                choose_image();
                break;
        }
    }

    Uri imageUri = null;

    private void take_photo() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        try {
            imageUri = Uri.fromFile(new File(get_cache_dir(), get_new_cache_filename()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(intent, Constants.Request.TAKE_PHOTO);
        } catch (Exception ex) {
            Toast.makeText(this, "您的机器当前不能正常拍照", Toast.LENGTH_LONG);
            return;
        }
    }

    private String get_cache_dir() {
        if(cache_dir != null)
            return Environment.getExternalStorageDirectory() + "/kc/cache/image";
        else
            return null;
        // todo if no SDCard
    }

    private String get_new_cache_filename() {
        filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
        return filename;
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
        int dp5 = BaseUtils.dp_to_px(5);
        if (ll_photos.getChildCount() > 1) {
            ll_photos.setPadding(0, dp5, 0, 0);
        } else
            ll_photos.setPadding(0, 0, 0, 0);
    }

    private void choose_image() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Constants.Request.GET_IMAGE_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.Request.GET_IMAGE_FROM_ALBUM:
                    if (null != data) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        upload_image(picturePath);
                    }
                    break;
                case Constants.Request.TAKE_PHOTO:
                    // todo 处理下大小
                    upload_image(get_cache_dir() + "/" + filename);
            }
        }
    }
}