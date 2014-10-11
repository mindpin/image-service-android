package com.mindpin.image_service_android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.mindpin.image_service_android.KCApplication;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseUtils {

    public static int dp_to_px(float dp) {
        try {
            final float scale = KCApplication.get_context().getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        } catch (Exception e) {
            return (int) dp;
        }
    }

    public static int px_to_dp(float px) {
        final float scale = KCApplication.get_context().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @SuppressLint("SimpleDateFormat")
	public static String date_string(long time_seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MM月d日");
        return sdf.format(new Date(time_seconds * 1000));
    }
    @SuppressLint("SimpleDateFormat")
	public static String date_all_string(Date date) {
		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = sdf.format(date);
		return dateStr;
	}
    @SuppressLint("SimpleDateFormat")
	public static String date_all_string(long date) {
		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = sdf.format(date);
		return dateStr;
	}
    
    @SuppressLint("SimpleDateFormat")
	public static String time_string(long time_seconds){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("HH:mm");
        return sdf.format(new Date(time_seconds * 1000));
    }
    
	@SuppressLint("SimpleDateFormat")
	public static String friendly_time_string(long time_seconds) {
		Date date = new Date(time_seconds * 1000);
		Date now = new Date();
		
		// 如果不是当年的，显示年份
		if ( now.getYear() != date.getYear() ){
			return (date.getYear() + 1900) + "年";
		}
		
		// 是当年的，显示可读的日期，或时间，或x分钟前
		long seconds_delta = (now.getTime() - date.getTime()) / 1000; // 相差秒数
		int MM = (int) seconds_delta / 60; // 相差分钟数
		int hh = (int) MM / 60; // 相差小时数
		int dd = (int) hh / 24; // 相差天数
		
		String re;
		if (dd >= 1) {
			re = date_string(time_seconds);
		} else if (hh >= 1) {
			re = time_string(time_seconds);
		} else if (MM >= 1) {
			re = MM + "分钟前";
		} else {
			re = "1分钟前";
		}
		return re;
	}
    
    // yyyy-MM-ddTHH:mm:ssZ
    @SuppressLint("SimpleDateFormat")
	public static long parse_iso_time_string_to_long(String iso_time_string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return sdf.parse(iso_time_string).getTime();
    }

    public static boolean net_is_active(Context context){
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mgrConn.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }
    
	public static boolean is_wifi_active(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}
//   　如果拟开发一个网络应用的程序，首先考虑是否接入网络，在Android手机中判断是否联网可以通过 ConnectivityManager 类
//    的isAvailable()方法判断，首先获取网络通讯类的实例
//    ConnectivityManager cwjManager=
//    (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//   ，使用cwjManager.getActiveNetworkInfo().isAvailable(); 
//   来返回是否有效，如果为True则表示当前Android手机已经联网，可能是WiFi或GPRS、HSDPA等等，
//   具体的可以通过 ConnectivityManager 类的getActiveNetworkInfo() 方法判断详细的接入方式，
//   需要注意的是有关调用需要加入 这个权限，android开发网提醒大家在真机上Market和Browser程序都使用了这个方法
//   ，来判断是否继续，同时在一些网络超时的时候也可以检查下网络连接是否存在，以免浪费手机上的电力资源。
    
    
    // [1,2,3,4] -> "1,2,3,4"
    public static String integer_list_to_string(List<Integer> ids) {
        String res = "";
        if (ids != null) {
            for (Integer s : ids) {
                if ("".equals(res)) {
                    res = res + s;
                } else {
                    res = res + "," + s;
                }
            }
        }
        return res;
    }

    // ["1","2","3","4"] -> "1,2,3,4"
    public static String string_list_to_string(List<String> strs) {
        String res = "";
        if (strs != null) {
            for (String s : strs) {
                if ("".equals(res)) {
                    res = res + s;
                } else {
                    res = res + "," + s;
                }
            }
        }
        return res;
    }

    public static List<String> string_to_string_list(String string) {
        List<String> list = new ArrayList<String>();
        String[] arr = string.split(",");
        for (String str : arr) {
            if (!"".equals(str)) {
                list.add(str);
            }
        }
        return list;
    }

    public static List<Integer> string_to_integer_list(String string) {
        List<Integer> list = new ArrayList<Integer>();
        String[] arr = string.split(",");
        for (String str : arr) {
            if (!"".equals(str)) {
                list.add(Integer.parseInt(str));
            }
        }
        return list;
    }

    // 把字节流转换成字符串
    public static String convert_stream_to_string(InputStream is) {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
                return "";
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024 * 4];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    //判断字符串非空
    public static boolean is_str_blank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static String sub_string_by(String str,int len){
    	String resulet = is_str_blank(str) ? "" : str;
    	if (str.length() > len) {
    		resulet = resulet.substring(0, len) + "...";
		}
    	return resulet;
    }

    // 快速显示一个toast
    public static void toast(int string_resource_id) {
        Toast toast = Toast.makeText(
        		KCApplication.get_context(),
                string_resource_id,
                Toast.LENGTH_SHORT
        );
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toast(String string) {
        Toast toast = Toast.makeText(
        		KCApplication.get_context(),
                string,
                Toast.LENGTH_SHORT
        );
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    //"abcdef" -  "c"  -> 2
    public static int char_at_array_index(String[] array,String s){
    	for (int i = 0; i < array.length; i++) {
			if (array[i].equals(s)) {
				return i;
			}
		}
		return -1;
    }
    
    //{'a','b','c','d','e','f'} - 'bdf' -> 1,3,5
    public static List<Integer> str_at_array_array(String[] array,String s){
		List<Integer> answer_list = new ArrayList<Integer>();
		for (int j = 0; j < s.length(); j++) {
			for (int i = 0; i < array.length; i++) {
				if (array[i].equals(s.charAt(j)+"")) {
					answer_list.add(i);
				}
			}
		}
		return answer_list;
	}

    public static class ScreenSize {
        public float width_dp;
        public float height_dp;
        public int width_px;
        public int height_px;
    }

    public static ScreenSize get_screen_size() {
        try {
            DisplayMetrics dm = KCApplication.get_context().getResources().getDisplayMetrics();

            ScreenSize ss = new ScreenSize();
            ss.width_dp = dm.widthPixels / dm.density;
            ss.height_dp = dm.heightPixels / dm.density;
            ss.width_px = dm.widthPixels;
            ss.height_px = dm.heightPixels;

            return ss;
        } catch (Exception e) {

            ScreenSize ss = new ScreenSize();
            ss.width_dp = 320;
            ss.height_dp = 533;
            ss.width_px = 480;
            ss.height_px = 800;

            return ss;
        }
    }

    public static File create_dir(File file_dir) {
        file_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + file_dir.getPath());
        if (file_dir.exists()) {
            Log.i("目录已经存在 ", file_dir.getAbsolutePath());
            return file_dir;
        }

        Log.i("目录不存在 开始创建目录 ", "true");

        try{
            boolean result = file_dir.mkdirs();
            if (result) {
                Log.i("目录创建成功 ", "true");
                return file_dir;
            }
        } catch(SecurityException se){
            Log.i("目录创建失败 ", se.toString());
        }

        return null;
    }
}