package com.unicore.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.unicore.tools.R;

public class UToast{
	
	
	public static void create(Context context,int resId){
		create(context, context.getResources().getText(resId),Toast.LENGTH_SHORT);
	}
	
	public static void create(Context context,CharSequence msg){
		create(context,msg,Toast.LENGTH_SHORT);
	}
	
	public static void create(Context context,int resId,int duration){
		create(context,context.getResources().getText(resId),duration);
	}
	
	public static void create(Context context,CharSequence msg,int duration){
			View rootView = View.inflate(context, R.layout.custom_toast, null);
			Toast toast = new Toast(context);
	   		TextView toastTextView = 
	   				(TextView) rootView.findViewById(R.id.toast_textview);
	   		toastTextView.setText(msg);
	   		WindowManager wm = 
	   				(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	   			//int height = wm.getDefaultDisplay().getHeight(); //Method Disuse
	   			DisplayMetrics dm = new DisplayMetrics();
	   			wm.getDefaultDisplay().getMetrics(dm);
	   			int height = dm.heightPixels;
	   			toastTextView.setText(msg);
	   			toast.setDuration(duration);
	   			toast.setGravity(Gravity.TOP, 0, height/2);
	   			toast.setView(rootView);
	   			toast.show();
	}
}
