package com.unicore.utils;

import java.math.BigInteger;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale") 
public class DataConvert {

	public static byte[] hexStringToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 数组转成十六进制字符串
	 * @param byte[]
	 * @return HexString
	 */
	public static String bytesToHexString3(byte[] b){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i){
			buffer.append(byteToHexString(b[i]));
		}
		return buffer.toString();
	}


	/**
	 * 数组转成十进制字符串
	 * @param byte[]
	 * @return HexString
	 */
	public static String bytesToDecString(byte[] b){
		return new BigInteger(1,b).toString(10);
	}

	public static String byteToHexString(byte b){
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1){
			return "0" + s;
		}else{
			return s;
		}
	}

	public static String bytesToBinString(byte[] data){
		return new BigInteger(1,data).toString(2);
	}
	
	
	public static String bytesToHexString(byte[] data){
		return new BigInteger(1,data).toString(16);
	}


	public static String bytesToHexString2(byte[] src){  
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		for (int i = 0; i < src.length; i++) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append(0);  
			}  
			stringBuilder.append(hv);  
		}  
		return stringBuilder.toString();  
	}  
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes2(String hexString) {  
		if (hexString == null || hexString.equals("")) {  
			return null;  
		}  
		hexString = hexString.toUpperCase();  
		int length = hexString.length() / 2;  
		char[] hexChars = hexString.toCharArray();  
		byte[] d = new byte[length];  
		for (int i = 0; i < length; i++) {  
			int pos = i * 2;  
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		}  
		return d;  
	}

	private static byte charToByte(char c) {  
		return (byte) "0123456789ABCDEF".indexOf(c);  
	}


}
