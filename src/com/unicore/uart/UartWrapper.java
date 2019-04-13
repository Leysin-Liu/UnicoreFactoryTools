package com.unicore.uart;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * UART通讯包装类
 * @author leysin 2018-12-21
 *
 */
public abstract class UartWrapper implements Uart{
	
	public InputStream mInputStream = null;
	public OutputStream mOutputStream = null;
	public UartParameter parameter;
	public UartListener listener;
	
	@Override
	public void setParameter(UartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public void setListener(UartListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onDataSend(byte[] data){
		if(listener != null){
			listener.onDataSend(data);
		}
	}
	
	@Override
	public void onDataRead(byte[] data){
		if(listener != null){
			listener.onDataRead(data);
		}
	}
	
	@Override
	public void onError(int errorCode,String errorStr){
		if(listener != null){
			listener.onError(errorCode, errorStr);
		}
	}
	
	public boolean isWritable(){
		return mOutputStream == null ? false : true;
	}
	
	public boolean isReadable(){
		return mInputStream == null ? false : true;
	}
	
}
