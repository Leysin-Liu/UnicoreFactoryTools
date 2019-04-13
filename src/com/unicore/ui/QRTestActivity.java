package com.unicore.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unicore.ctrl.SwitchUtils;
import com.unicore.tools.R;
import com.unicore.uart.Const;
import com.unicore.uart.GPIOUtils;
import com.unicore.uart.PortHelper;
import com.unicore.uart.UartListener;
import com.unicore.uart.UartParameter;

public class QRTestActivity extends BaseActivity implements UartListener{

	public UartParameter parameter = null;
	public PortHelper mPortHelper = null;
	public TextView mTextView = null;
	public ScrollView mScrollView = null;
	public StringBuffer mStringBuffer = new StringBuffer();
	private static final int MSG_START_SCAN = 0;
	public Button clean;
	public Button start;

	@Override
	protected <T extends View> int setLayout() {
		return R.layout.activity_qr;
	}

	@Override
	protected void initData() {
		mPortHelper = new PortHelper(getApplicationContext());
		parameter = new UartParameter(Const.Path.UART1, 9600);
		//GPIOUtils.writeValueToNode(Const.Path.UART_SWT_1,GPIOUtils.LOW);
		SwitchUtils.getDefault().switchToQR();
		mPortHelper.setParameter(parameter);
		mPortHelper.setDataListener(this);
		mPortHelper.open();
	}

	@Override
	protected void findViews() {
		mTextView = findID(R.id.textView);
		mScrollView = findID(R.id.qr_scrollView);
		clean = findID(R.id.clearContent);
		start = findID(R.id.startScan);
	}

	@Override
	protected void setListener() {
		clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mStringBuffer != null && mStringBuffer.length() > 0){
					mStringBuffer.delete(0, mStringBuffer.length());
					showText();
				}
			}
		});

		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(MSG_START_SCAN);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 260:
		case 261:
			SwitchUtils.getDefault().startScan();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDataSend(byte[] data) {
		mStringBuffer.append("send:" + new String(data) + "\n");
		showText();
	}

	@Override
	public void onDataRead(byte[] data) {
		mStringBuffer.append("read:" + new String(data) + "\n");
		showText();
	}

	@Override
	public void onError(int errorCode, String errorStr) {
		mStringBuffer.append("error:" + errorCode +  errorStr +"\n");
		showText();
	}

	public void showText(){
		mTextView.setText("");
		mTextView.setText(mStringBuffer);
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPortHelper.close();
	}

	public ExecutorService executorService = Executors.newSingleThreadExecutor();
	@SuppressLint("HandlerLeak")
	private  final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int message = msg.what;
			switch(message){
			case MSG_START_SCAN:
				SwitchUtils.getDefault().startScan();
				break;
			}
		};
	};


}
