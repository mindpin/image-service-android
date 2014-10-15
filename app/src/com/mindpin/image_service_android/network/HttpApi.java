package com.mindpin.image_service_android.network;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.mindpin.image_service_android.KCApplication;
import com.mindpin.image_service_android.R;
import com.mindpin.image_service_android.models.http.ImageData;
import com.mindpin.image_service_android.models.interfaces.IImageData;

import java.io.File;

/**
 * Created by dd on 14-10-15.
 */
public class HttpApi {
    public static final String SITE = KCApplication.get_context().getResources().getString(R.string.http_site);
    public static final String URL_UPLOAD = SITE + "/api/upload";

    public static IImageData upload(String image_path) {
        System.out.println("upload image_path:" + image_path);
        HttpRequest request = HttpRequest.post(URL_UPLOAD);
        request.part("file", image_path, new File(image_path));
        if (request.ok()) {
            String body = request.body();
            System.out.println("upload body:" + body);
            return new Gson().fromJson(body, ImageData.class);
        }
        else
            return null;
    }
}
