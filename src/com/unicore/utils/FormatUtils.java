package com.unicore.utils;

import java.math.BigInteger;

public class FormatUtils {
	
	
	/**
	 * 十六进制字符转byte数组
	 * @param String hex
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
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
	 * byte数组转成十六进制字符串
	 * @param byte[]
	 * @return HexString
	 */
	public static String byteToHexString(byte[] b){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i){
			buffer.append(toHexString(b[i]));
		}
		return buffer.toString();
	}
	public static String toHexString(byte b){
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1){
			return "0" + s;
		}else{
			return s;
		}
	}
	
	/**
	 * 将byte数组转换为指定进制的String
	 * @param b byte[]
	 * @param radix 
	 * @return
	 */
	public static String byteToFormatString(byte[] b,int radix){
		return new BigInteger(1,b).toString(radix);
	}
	
}
