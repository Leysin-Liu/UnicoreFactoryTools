package com.unicore.ctrl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.SystemClock;

import com.unicore.uart.Const;
import com.unicore.uart.GPIOUtils;

/**
 * 切换控制
 * @author leysin
 * @date Apr 12, 2019
 * @Time 6:24:56 PM
 */
public class SwitchUtils {
	
	private static SwitchUtils instance;
	private SwitchUtils(){
	}
	
	public static SwitchUtils getDefault(){
		if(instance == null){
			synchronized (SwitchUtils.class) {
				if(instance == null){
					instance = new SwitchUtils();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 切换到QR
	 */
	public void switchToQR(){
		GPIOUtils.writeValueToNode(Const.Path.UART_SWT_1,GPIOUtils.LOW);
	}
	
	/**
	 * 切换到Psam1
	 */
	public void switchToPsam1(){
		GPIOUtils.writeValueToNode(Const.Path.PSAM_PWR_EN,GPIOUtils.HIGH);
		GPIOUtils.writeValueToNode(Const.Path.UART_SWT_1,GPIOUtils.HIGH);
	}
	
	/**
	 * 切换到Psam2
	 */
	public void switchToPsam2(){
		GPIOUtils.writeValueToNode(Const.Path.PSAM_PWR_EN,GPIOUtils.HIGH);
		GPIOUtils.writeValueToNode(Const.Path.UART_SWT_2,GPIOUtils.HIGH);
	}
	
	/**
	 * 切换到RFID
	 */
	public void switchToRFID(){
		GPIOUtils.writeValueToNode(Const.Path.RFID_PWR_EN, GPIOUtils.HIGH);
		GPIOUtils.writeValueToNode(Const.Path.UART_SWT_2, GPIOUtils.LOW);
	}
	
	/**
	 * psam 下电
	 */
	public void psamPowerOff(){
		GPIOUtils.writeValueToNode(Const.Path.PSAM_PWR_EN,GPIOUtils.LOW);
	}
	
	/**
	 * RFId 下电
	 */
	public void rfidPowerOff(){
		GPIOUtils.writeValueToNode(Const.Path.RFID_PWR_EN, GPIOUtils.LOW);
	}
	
	
	/**
	 * QR 扫描
	 */
	public ExecutorService executorService = Executors.newSingleThreadExecutor();
	public synchronized void startScan(){
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				if(!isScanRunning()){
					scan();
				}else{
					setLow();
					scan();
				}
			}
		});
	}

	private  void setHigh(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GPIOUtils.writeValueToNode(Const.Path.QR_STRING,GPIOUtils.HIGH);
	}
	private Handler handler = new Handler();
	private synchronized void scan() {
		handler.removeCallbacks(mRunnable);
		SystemClock.sleep(100);
		setScanStatus(true);
		setHigh();
		handler.postDelayed(mRunnable,3000);
	}

	private  boolean isScanRunning(){
		return isScan;
	}

	private boolean isScan = false;
	private synchronized void setScanStatus(boolean isScan){
		this.isScan = isScan;
	}
	
	private synchronized void setLow(){
		SystemClock.sleep(100);
		GPIOUtils.writeValueToNode(Const.Path.QR_STRING,GPIOUtils.LOW);
	}
	
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			synchronized (mRunnable) {
				setScanStatus(false);
				setLow();
				SystemClock.sleep(200);
				//if(isContin())startScan();
			}
		}

	};
	
	public void release(){
		executorService.shutdownNow();
		handler.removeCallbacksAndMessages(null);
	}
	
}
