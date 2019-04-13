package com.unicore.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unicore.tools.R;
import com.unicore.uart.Const;
import com.unicore.uart.GPIOUtils;
import com.unicore.uart.PortHelper;
import com.unicore.uart.UartListener;
import com.unicore.uart.UartParameter;
import com.unicore.utils.DataConvert;
import com.unicore.utils.SPUtils;
import com.unicore.view.UToast;

/**
 * @author leysin
 * @date Apr 9, 2019
 * @Time 10:44:31 AM
 */
public class PsamTestActivity extends BaseActivity{

	private TextView mTextView;
	private ScrollView mScrollView;
	private Button powerOn;
	private Button powerOff;
	private Button apdu;
	private PortHelper mHelper;
	private StringBuffer mStringBuffer = new StringBuffer();
	private String[] items = new String[]{"Psam 1","Psam 2"};
	private int psamItem = 0;
	@Override
	protected <T extends View> int setLayout() {
		return R.layout.activity_psam;
	}
	
	@Override
	protected void initData() {
		psamItem = (int) SPUtils.get(PsamTestActivity.this,"PsamItem",0);
		setActionBartTitle();
		initPsam(psamItem);
	}

	private void initPsam(int item) {
		if(mHelper != null){
			mHelper.close();
			mHelper = null;
		}	
			if(item == 0){
				createPort(Const.Path.UART1,Const.Path.UART_SWT_1);
			}else {
				createPort(Const.Path.UART0,Const.Path.UART_SWT_2);
			}
	}

	private void createPort(String portNode,String swichNode) {
		mHelper = new PortHelper(getApplicationContext());
		mHelper.setParameter(new UartParameter(portNode, 38400));
		GPIOUtils.writeValueToNode(swichNode, GPIOUtils.HIGH);
		GPIOUtils.writeValueToNode(Const.Path.PSAM_PWR_EN,GPIOUtils.HIGH);
		mHelper.setDataListener(new UartListener() {
			@Override
			public void onError(int errorCode, String errorStr) {
				showContent("Error:" + errorStr + "\n");
			}
			@Override
			public void onDataSend(byte[] data) {
				showContent(items[psamItem] + "-send:" + DataConvert.bytesToHexString(data) + "\n");
			}
			@Override
			public void onDataRead(byte[] data) {
				showContent(items[psamItem] + "-read:" + DataConvert.bytesToHexString(data) + "\n");
			}
		});
		mHelper.open();
	}

	private void showContent(String content){
		mStringBuffer.append(content);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			psamItem = (int) SPUtils.get(PsamTestActivity.this,"PsamItem",0);
			shwoItemDialog(psamItem);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setActionBartTitle() {
		getActionBar().setTitle(items[psamItem]);		
	}

	private void shwoItemDialog(int position) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(PsamTestActivity.this);
		dialog.setTitle("选择Psam设备")
		.setSingleChoiceItems(items,position, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UToast.create(getApplicationContext(),"position = " + which);
				psamItem = which;
				SPUtils.put(PsamTestActivity.this, "PsamItem", psamItem);
				initPsam(which);
				dialog.dismiss();
			}
		}).setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				setActionBartTitle();
			}
		}).create().show();
	}

	@Override
	protected void findViews() {
		powerOn = findID(R.id.powerOn);
		powerOff = findID(R.id.powerOff);
		apdu = findID(R.id.apdu);
		mTextView = findID(R.id.psam_textView);
		mScrollView = findID(R.id.psam_scrollView);
		mTextView.setTextSize(10);
	}

	@Override
	protected void setListener() {
		powerOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendCMD(Const.Cmd.POWER_ON);
			}
		});

		powerOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendCMD(Const.Cmd.POWER_OFF);
			}
		});

		apdu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendCMD(Const.Cmd.EXE_APDU);
			}
		});
	}
	
	public void sendCMD(byte[] cmd){	
		if(mHelper != null && mHelper.isWritable()){
			mHelper.sendDataToUart(cmd);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHelper.close();
		GPIOUtils.writeValueToNode(Const.Path.PSAM_PWR_EN,GPIOUtils.LOW);
		SPUtils.put(PsamTestActivity.this, "PsamItem", psamItem);
	}

}
