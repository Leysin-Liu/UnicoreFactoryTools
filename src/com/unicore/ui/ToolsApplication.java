package com.unicore.ui;

import com.unicore.utils.CrashHandler;

import android.app.Application;

public class ToolsApplication extends Application{
	
	@Override  
    public void onCreate() {  
        super.onCreate();  
       /* CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());  */
    } 

}
