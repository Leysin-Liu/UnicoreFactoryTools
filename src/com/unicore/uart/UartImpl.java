package com.unicore.uart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.os.SystemClock;
import android_serialport_api.SerialPort;

import com.unicore.uart.Const.Error;
import com.unicore.utils.DataConvert;
import com.unicore.utils.ULog;

/**
 * UART通讯类
 * @author Leysin
 */
public abstract class UartImpl extends UartWrapper{
	
	public int effectiveValue = 2;
	public static final String TAG = "UartImpl";
	private SerialPort mSerialPort = null;
	private UartReadThread mUartReadThread = null;
	public int interval = 50;
	@Override
	public void sendDataToUart(byte[] data) throws IOException {
		if(mOutputStream != null){
			mOutputStream.write(data);
			mOutputStream.flush();
		}
		ULog.i(TAG, "Send Data:" + DataConvert.bytesToHexString(data));
	}
	@Override
	public int open() {
		if(parameter == null){
			onException(Error.PARAMETER_IS_NULL,new NullPointerException("Parameter is null"));
			return Error.PARAMETER_IS_NULL;
		}
		try {
			mSerialPort = new SerialPort
					(new File(parameter.getUartPath()),parameter.getBaudrate(), 0);
		} catch (SecurityException e) {
			onException(Error.SECURITY_EXCEPTION, e);
			return Error.SECURITY_EXCEPTION;
		} catch (IOException e) {
			onException(Error.IO_EXCEPTION, e);
			return Error.IO_EXCEPTION;
		}
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();
		mUartReadThread = new UartReadThread();
		mUartReadThread.start();
		ULog.i(TAG, "open");
		return Const.Success.OPEN_SUCCESS;
	}
	
	@Override
	public void close() {
		if(mUartReadThread != null){
			mUartReadThread.interrupt();
			mUartReadThread = null;
		}
		try {
			if(mOutputStream != null){
				mOutputStream.close();
				mOutputStream = null;
			}
			if(mInputStream != null){
				mInputStream.close();
				mInputStream = null;
			}
		} catch (IOException e) {
			onException(Const.Error.IO_EXCEPTION,e);
		}
		ULog.i(TAG, "close");
	}

	/**
	 * This thread is responsible for reading data from the serial port
	 * @author leysin
	 */
	final class UartReadThread extends Thread{
		private byte[] buffer = new byte[512];
		@Override
		public void run() {
			ULog.i(TAG, "ReadThread Start");
			int size = 0;
			while(!interrupted()){
				try {
					//判断串口中是否有数据返回
					while(mInputStream != null 
							&& mInputStream.available()<1){
						SystemClock.sleep(interval);
					}
					if(mInputStream == null)return;
					//delay 100ms 解决数据两次返回问题
					SystemClock.sleep(100);
				} catch (IOException e) {
					onException(Const.Error.IO_EXCEPTION,e);
				}
				try {
					Arrays.fill(buffer, (byte)0);
					size = mInputStream.read(buffer);
					ULog.i(TAG, "Read Bytes Length = " + size);
					if (size > effectiveValue) {
						onDataReceived(buffer,size);
					}
				} catch (IOException e1) {
					onException(Error.IO_EXCEPTION, e1);
				}
			}
		}
	}
	
	public void setEffectiveValueSize(int effectiveValue){
				this.effectiveValue = effectiveValue;
	}
}
