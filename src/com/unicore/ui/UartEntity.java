package com.unicore.ui;


public class UartEntity {
	
	private int uartPathIndex;
	
	private int baudrateIndex;
	
	private int formatIndex;

	public UartEntity(int uartPathIndex,int baudrateIndex,int formatIndex) {
		this.uartPathIndex = uartPathIndex;
		this.baudrateIndex =baudrateIndex;
		this.formatIndex = formatIndex;
	}
	public int getUartPathIndex() {
		return uartPathIndex;
	}

	public void setUartPathIndex(int uartPathIndex) {
		this.uartPathIndex = uartPathIndex;
	}

	public int getBaudrateIndex() {
		return baudrateIndex;
	}

	public void setBaudrateIndex(int baudrateIndex) {
		this.baudrateIndex = baudrateIndex;
	}

	public int getFormatIndex() {
		return formatIndex;
	}

	public void setFormatIndex(int formatIndex) {
		this.formatIndex = formatIndex;
	}

}
