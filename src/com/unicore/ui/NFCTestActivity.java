package com.unicore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unicore.tools.R;
import com.unicore.utils.NFCUtils;
import com.unicore.utils.ULog;

public class NFCTestActivity extends BaseActivity {
	private TextView mTextView;
	private StringBuffer sb = new StringBuffer();
	private ScrollView mScrollView;
	private static final String TAG = "NFCTestActivity";
	public String mTagText = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		NFCUtils.init(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//关闭前台调度系统  
		NFCUtils.mNfcAdapter.disableForegroundDispatch(this); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		//开启前台调度系统  
		NFCUtils.mNfcAdapter.enableForegroundDispatch
		(this, NFCUtils.mPendingIntent, NFCUtils.mIntentFilter, NFCUtils.mTechList);
	}

	@Override  
	protected void onNewIntent(Intent intent) {  
		super.onNewIntent(intent);  
		//当该Activity接收到NFC标签时，运行该方法  
		//调用工具方法，读取NFC数据  
		ULog.i(TAG, "onNewIntent");
			String tag = NFCUtils.processIntent(intent);
			if(!tag.isEmpty()){
				sb.append(tag + "\n");
				mTextView.setText(sb);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}
	}

	@Override
	protected <T extends View> int setLayout() {
		return R.layout.activity_nfc;
	}

	@Override
	protected void initData() {
		 
	}

	@Override
	protected void findViews() {
		mTextView = (TextView) findViewById(R.id.nfcText);
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
	}

	@Override
	protected void setListener() {

	}


}
