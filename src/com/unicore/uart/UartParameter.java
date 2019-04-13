package com.unicore.uart;

public class UartParameter {
	
	public String UartName;
	
	public String UartPath;
	
	public int Baudrate;
	
	
	public UartParameter() {
		
	}
	
	public UartParameter(String path,int baudrate) {
		this.Baudrate = baudrate;
		this.UartPath = path;
	}
	

	public String getUartName() {
		return UartName;
	}

	public void setUartName(String uartName) {
		UartName = uartName;
	}

	public String getUartPath() {
		return UartPath;
	}

	public void setUartPath(String uartPath) {
		UartPath = uartPath;
	}

	public int getBaudrate() {
		return Baudrate;
	}

	public void setBaudrate(int baudrate) {
		Baudrate = baudrate;
	}
	
}
