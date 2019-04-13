package com.unicore.uart;


public interface UartListener {
	
	void onDataSend(byte[] data);
	
	void onDataRead(byte[] data);
	
	void onError(int errorCode,String errorStr);

}
