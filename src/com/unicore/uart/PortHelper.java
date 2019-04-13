package com.unicore.uart;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.unicore.utils.ULog;

public class PortHelper {
	private static final String TAG = "PortHelper";
	private Context mContext;
	private Uart mUartHelper;
	private HandlerThread mHandlerThread;
	private Handler threadHandler;
	private Handler mainHandler;
	private ExecutorService threadPool = Executors.newSingleThreadExecutor();
	public PortHelper(Context ctx) {
		mContext = ctx;
		mHandlerThread = new HandlerThread("UART-HandlerThread");
		mHandlerThread.start();
		threadHandler = new Handler(mHandlerThread.getLooper()){
			@Override
			public void handleMessage(Message msg) {
				final byte[] data = (byte[]) msg.obj;
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String threadName = Thread.currentThread().getName();
			                 ULog.i(TAG, "线程："+threadName);
							mUartHelper.sendDataToUart(data);
						} catch (IOException e) {
							mUartHelper.onError(Const.Error.IO_EXCEPTION, e.getMessage());
						}
					}
				});	
			}
		};
		mUartHelper = new UartHelper();
		mainHandler = new Handler(mContext.getMainLooper());
	}

	public void setParameter(UartParameter parameter) {
		mUartHelper.setParameter(parameter);
	}

	public void sendDataToUart(byte[] data){
		Message message = Message.obtain();
		message.obj = data;
		PortHelper.this.threadHandler.sendMessage(message);
		mUartHelper.onDataSend(data);
	}
	
	public boolean isWritable(){
		return ((UartImpl)mUartHelper).isWritable();
	}
	
	public boolean isReadable(){
		return ((UartImpl)mUartHelper).isReadable();
	}

	public int open() {
		return mUartHelper.open();
	}

	public void close() {
		mUartHelper.close();
		mainHandler.removeCallbacksAndMessages(null);
		mHandlerThread.interrupt();
		threadPool.shutdownNow();
	}

	public void setDataListener(UartListener listener) {
		mUartHelper.setListener(listener);
	}

	final class UartHelper extends UartImpl{
		@Override
		public void onDataReceived(byte[] data,int size) {
			ULog.i(TAG, "Thread Name:" + Thread.currentThread().getName());
			final byte[] retData = Arrays.copyOf(data,size);
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					onDataRead(retData);
					ULog.i(TAG, "Thread Name:" + Thread.currentThread().getName());
				}
			});
		}

	}

}
