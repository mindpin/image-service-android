package com.mindpin.image_service_android.network;

import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.image_service_android.models.http.ImageData;
import com.mindpin.image_service_android.models.interfaces.IImageData;

/**
 * Created by dd on 14-10-13.
 */
public class DataProvider {
    public static IImageData upload(String image_path) {
        return HttpApi.upload(image_path);
//        return new ImageData(image_path);
    }
}
