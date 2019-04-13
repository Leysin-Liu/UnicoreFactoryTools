package com.unicore.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @Desc LogUtils
 * @Author leysin
 * @Time 2016-10-24 11:34:45
 */
public class ULog {
	
	private static boolean isDUG = true;
	
	public static String getClassName(Object o){
		return o.getClass().getSimpleName();
	}

	public static void e(String tag, String msg) {
		if (isDUG)Log.e( tag,msg );
	}
	
	public static void w(String tag, String msg) {
		if (isDUG)Log.w(tag,msg);
	}

	public static void i(String tag, String msg) {
		if (isDUG)Log.i(tag,msg);
	}
	
	public static void d(String tag, String msg) {
		if (isDUG)Log.d(tag,msg);
	}

	public static void toast(Context context, String msg) {
		if (isDUG)toast(context, msg, 0);
	}
	public static void toast(Context context, String msg, int duration) {
		if (isDUG)Toast.makeText(context, msg, duration).show();
	}

}
