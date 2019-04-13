package com.unicore.uart;

import java.io.IOException;

/**
 * 串口通讯类顶层接口
 * @author leysin 2018-12-21
 *
 */
public interface Uart extends UartListener{
	
	/**
	 * 设置串口参数
	 * @param parameter
	 */
	void setParameter(UartParameter parameter);
	
	/**
	 * 发送数据到串口
	 * @param data
	 * @throws IOException 
	 */
	void sendDataToUart(byte[] data) throws IOException;
	
	
	/**
	 * 接收串口收到的数据
	 * @param data
	 */
	void onDataReceived(byte[] data,int size);
	
	
	/**
	 * 打开串口
	 * 打开串口前必须先设置串口参数
	 */
     int open();
     
     /**
      * 关闭串口，释放资源
      */
     void close();
     
     /**
      * 设置发送、接收数据监听
      */
     void setListener(UartListener listener);
}
