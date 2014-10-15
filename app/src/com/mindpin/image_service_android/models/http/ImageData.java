package com.mindpin.image_service_android.models.http;

import com.mindpin.image_service_android.models.interfaces.IImageData;

/**
 * Created by dd on 14-10-13.
 */
public class ImageData implements IImageData {
    String filename, url;
    int filesize, width, height;
    Object major_color;

    public ImageData() {
    }

    @Override
    public String get_filename() {
        return filename;
    }

    @Override
    public String get_url() {
        return url;
    }

    @Override
    public int get_filesize() {
        return filesize;
    }

    @Override
    public int get_width() {
        return width;
    }

    @Override
    public int get_height() {
        return height;
    }

    @Override
    public Object get_major_color() {
        return major_color;
    }
}
