package com.unicore.ui;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unicore.tools.R;
import com.unicore.uart.PortHelper;
import com.unicore.uart.UartListener;
import com.unicore.uart.UartParameter;
import com.unicore.utils.Constant;
import com.unicore.utils.DataConvert;
import com.unicore.utils.SPUtils;
import com.unicore.utils.ULog;
import com.unicore.view.UToast;

public class UartTestActivity extends BaseActivity implements UartListener{

	private static final String TAG = UartTestActivity.class.getSimpleName();

	private String[] uartList;
	private String[] baudrateList;
	private StringBuffer mStringBuffer;

	private Spinner uartSpinner;
	private Spinner baudrateSpinner;
	private Spinner formatSpinner;

	private Button senMsgButton;
	private TextView receivedData;
	private EditText sendMsg; 
	private ScrollView mScrollView;
	private UartEntity mUartEntity;
	private byte[] sendMessage;
	private Context mContext;
	private CheckBox mCheckBox;
	private String sendText;
	public static final int SEND_MSG = 1;
	private Timer mTimer;
	private Format format = Format.Txt;

	private PortHelper portHelper;

	@Override
	public int setLayout() {
		return R.layout.activity_uart;
	}

	@Override
	protected void findViews() {
		mCheckBox = findID(R.id.checkBox);
		mScrollView = findID(R.id.scrollView);
		uartSpinner = findID(R.id.uart_spinner);
		baudrateSpinner = findID(R.id.baudrate_spinner);
		formatSpinner = findID(R.id.format_spinner);
		receivedData = findID(R.id.received_data);
		sendMsg = findID(R.id.send_msg);
		senMsgButton = findID(R.id.send_msg_button);

		uartSpinner.setSelection(mUartEntity.getUartPathIndex(),true);
		baudrateSpinner.setSelection(mUartEntity.getBaudrateIndex(), true);
		formatSpinner.setSelection(mUartEntity.getFormatIndex(),true);
		sendText = (String) SPUtils.get(mContext, Constant.SEND_CONTENT, Constant.TEST_TEXT);
		sendMsg.setText(sendText);
		initSerialPort(uartList[mUartEntity.getUartPathIndex()], 
				Integer.valueOf(baudrateList[mUartEntity.getBaudrateIndex()]));
	}

	@Override
	public void initData() {
		mContext = this;
		mStringBuffer = new StringBuffer();
		mUartEntity = new UartEntity(0,0,0);

		mUartEntity.setUartPathIndex((Integer) SPUtils.get(mContext, Constant.UART_NODES_PACH,0));
		mUartEntity.setBaudrateIndex((Integer) SPUtils.get(mContext,Constant.UART_BAUDRATE,0));
		mUartEntity.setFormatIndex((Integer) SPUtils.get(mContext,Constant.FORMAT_TYPE,0));

		baudrateList = getResources().getStringArray(R.array.baudrate_list);
		uartList =  getResources().getStringArray(R.array.uart_list);
		setFormat(mUartEntity.getFormatIndex());
	}

	private void initSerialPort() {
		initSerialPort(uartList[mUartEntity.getUartPathIndex()],
				Integer.valueOf(baudrateList[mUartEntity.getBaudrateIndex()]));
	}

	private void initSerialPort(String uart,int baudrate) {
		if(portHelper == null){
			portHelper = new PortHelper(this);
			portHelper.setDataListener(this);
			portHelper.setParameter(new UartParameter(uart,baudrate));
			portHelper.open();
			ULog.i(TAG, "Create Serial Port -> \n" 
					+ "UART :" + uart + "\n" 
					+ "Baudrate: " + baudrate);
		}
		ULog.i(TAG, "portHelper == null" + portHelper == null ? "true" : "false");
	}

	private Handler mHandler =  new Handler(Looper.getMainLooper()){

		public void handleMessage(Message msg) {

			switch (msg.what) {

			case SEND_MSG:

				synchronized (portHelper) {
					if(sendText != null && !sendText.isEmpty() && portHelper.isWritable()){
						sendData(sendText);
					}else{
						UToast.create(mContext, "send Data Error");
					}
				}
				break;
			}
		};
	};

	@Override
	protected void setListener(){
		uartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				UToast.create(UartTestActivity.this, "Selected:" + position);
				mUartEntity.setUartPathIndex(position);
				ULog.i(TAG, "Uart:" + uartList[position]);
				resetSerialPort();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		baudrateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				UToast.create(UartTestActivity.this, "Selected:" + position);
				mUartEntity.setBaudrateIndex(position);
				ULog.i(TAG, "BAUDRATE:" + Integer.valueOf(baudrateList[position]));
				resetSerialPort();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		formatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mUartEntity.setFormatIndex(position);
				setFormat(position);
			}


			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(mTimer==null){
						mTimer = new Timer();
						mTimer.schedule(new TimerTask() {
							@Override
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								mHandler.sendEmptyMessage(SEND_MSG);								
							}
						}, 1000,1);
					}
					baudrateSpinner.setClickable(false);
					uartSpinner.setClickable(false);
				}else{
					mTimer.cancel();
					mTimer = null;
					baudrateSpinner.setClickable(true);
					uartSpinner.setClickable(true);
				}
			}
		});

		sendMsg.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				sendText = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		senMsgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String result = sendMsg.getText().toString();
				if(result.isEmpty()){
					UToast.create(mContext, R.string.is_empty);
					return;
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						sendData(result);
					}

				});	
			}
		});
	}

	private void setFormat(int position) {
		switch (position) {
		case 0:
			format = Format.Txt;
			break;
		case 1:
			format = Format.Dec;
			break;
		case 2:
			format = Format.Hex;
			break;
		case 3:
			format = Format.Bin;
			break;
		}
	}


	public void sendData(final String result) {
		sendMessage = DataConvert.hexStringToBytes(result);
		sendData(sendMessage);
	}

	public void sendData(final byte[] result) {
			if(portHelper.isWritable()){
				portHelper.sendDataToUart(result);
			}else{
				showContent(
						getResources().getString(R.string.write_failure)+"\n");
			}
	}

	private void showContent(String result) {
		if(mStringBuffer.length()>1000){
			mStringBuffer.delete(0, mStringBuffer.length());
		}
		mStringBuffer.append(result);
		receivedData.setText("");
		receivedData.setText(mStringBuffer);
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	protected void resetSerialPort(){
		closeSeralPort();
		initSerialPort();
	}

	private String formatData(byte[] data){
		return formatData(data, "");
	}

	private String formatData(byte[] data,String charSet){

		String result = "";
		if(format == Format.Txt){
			if(charSet.isEmpty() || charSet == null){
				result = DataConvert.bytesToHexString(data);
			}else{
				try {
					result = new String(data,charSet);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if(format == Format.Dec){
			result = DataConvert.bytesToDecString(data);
		}
		if(format == Format.Hex){
			result = DataConvert.bytesToHexString(data);
		}
		if(format == Format.Bin){
			result = DataConvert.bytesToBinString(data);
		}

		return result;
	}



	/**
	 * SerialPort Readed Data
	 */
	private void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				byte[] b = Arrays.copyOf(buffer, size);
				String result = formatData(b);
				ULog.i(TAG, "Read Data:" + result);
				showContent(getResources().getString(R.string.received)
						+ result + "\n");
			}
		});
	}

	@Override
	protected void onDestroy() {
		closeSeralPort();
		savaState();
		super.onDestroy();
	}

	private void savaState() {
		SPUtils.put(mContext, Constant.FORMAT_TYPE,mUartEntity.getFormatIndex());
		SPUtils.put(mContext, Constant.UART_BAUDRATE, mUartEntity.getBaudrateIndex());
		SPUtils.put(mContext, Constant.UART_NODES_PACH, mUartEntity.getUartPathIndex());
		if(!sendMsg.getText().toString().isEmpty()){
			SPUtils.put(mContext, Constant.SEND_CONTENT, sendMsg.getText().toString());
			ULog.i(TAG, "savaState sendMsg = " + sendMsg.getText().toString());
		}else{
			SPUtils.put(mContext, Constant.SEND_CONTENT, "ABCDEFG123456");
		}
	}

	/**
	 *exit,close all source
	 */
	private void closeSeralPort() {
		if(portHelper != null){
			portHelper.close();
		}
		portHelper = null;
	}

	public enum Format{
		Txt,
		Dec,
		Hex,
		Bin,
	}

	@Override
	public void onDataSend(byte[] data) {
		String resultStr = DataConvert.bytesToHexString(data);
		showContent(
				getResources().getString(R.string.send) + resultStr + "\n");	
	}	

	@Override
	public void onDataRead(byte[] data) {
		onDataReceived(data, data.length);
	}

	@Override
	public void onError(int errorCode, String errorStr) {
			showContent("Excption:" + errorStr + "\n");
	}

}
