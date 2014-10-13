package com.mindpin.image_service_android.models.interfaces;

import java.io.Serializable;

/**
 * Created by dd on 14-10-13.
 */
public interface IImageData extends Serializable {
    public String get_filename();
    public String get_url();
    public int get_filesize();
    public int get_width();
    public int get_height();
    public int get_major_color();
}
