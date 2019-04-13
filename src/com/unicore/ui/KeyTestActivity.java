package com.unicore.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unicore.tools.R;
import com.unicore.view.UToast;

public class KeyTestActivity extends Activity {
	
	private Button exit;
	private TextView mTextView;
	private StringBuffer sb = new StringBuffer();
	private ScrollView mScrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_test);
		initView();
		//onBack();
	}
	
	private void initView() {
		mTextView = (TextView) findViewById(R.id.keytext);
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		exit = (Button) findViewById(R.id.exit_bt);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyTestActivity.this.finish();
			}
		});
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(sb.length() > 1000){
			sb.delete(0, sb.length());
			mTextView.setText(sb);
		}
		String ev = "";
		int action = event.getAction();
		switch (action) {
		case KeyEvent.ACTION_DOWN:
			ev = "ACTION_DOWN";
			break;
			
		case KeyEvent.ACTION_UP:
			ev = "ACTION_UP";
			break;
			
		case KeyEvent.ACTION_MULTIPLE:
			ev = "ACTION_MULTIPLE";
			break;

		}
		sb.append("KeyCode:" + keyCode + "--" + "KeyEvent:" + ev + "\n");
		mTextView.setText(sb);
		//滚动到底部
		scrollToDown();
		return super.onKeyUp(keyCode, event);
	}
	
	private void scrollToDown() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(sb.length() > 1000){
			sb.delete(0, sb.length());
			mTextView.setText(sb);
		}
		String ev = "";
		int action = event.getAction();
		switch (action) {
		case KeyEvent.ACTION_DOWN:
			ev = "ACTION_DOWN";
			break;
			
		case KeyEvent.ACTION_UP:
			ev = "ACTION_UP";
			break;
			
		case KeyEvent.ACTION_MULTIPLE:
			ev = "ACTION_MULTIPLE";
			break;
		}
		
		sb.append("KeyCode:" + keyCode + "--" + "KeyEvent:" + ev + "\n");
		
		mTextView.setText(sb);
		//滚动到底部
		scrollToDown();
		return super.onKeyDown(keyCode, event);
	}
	
	
	long timer = 0L;
	public void onBackPressed() {
		if(System.currentTimeMillis()-timer < 500){
			KeyTestActivity.this.finish();
		}else{
			UToast.create(KeyTestActivity.this, "快速按下两次退出");
		}
		timer = System.currentTimeMillis();
	}

}
