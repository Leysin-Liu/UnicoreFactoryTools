package com.unicore.uart;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * UART通讯包装类
 * @author leysin 2018-12-21
 *
 */
public abstract class UartWrapper implements Uart{
	
	public InputStream mInputStream = null;
	public OutputStream mOutputStream = null;
	public UartParameter parameter = null;
	public UartListener listener = null;
	
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
	
	public void onException(int errCode,Exception e){
		// Exception e;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream pout = new PrintStream(out);
		e.printStackTrace(pout);
		String ret = new String(out.toByteArray());
		pout.close();
		try {
		    out.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		if(ret == null || ret.isEmpty()){
			onError(errCode,"Exception");
		}else{
			onError(errCode, ret);
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
