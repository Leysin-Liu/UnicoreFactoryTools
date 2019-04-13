package com.unicore.ui;

import com.unicore.utils.ULog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public abstract class BaseActivity extends 
			Activity{
	public final String TAG = ULog.getClassName(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout()); 
		initActivity();
	}
	
	private void initActivity() {
			initData();
			findViews();
			setListener();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T findID(int id){
		return (T) findViewById(id);
	}
	
	protected abstract <T extends View> int setLayout();
	
	protected abstract void initData();
	
	protected abstract void findViews();
	
	protected abstract void setListener();
	
}
