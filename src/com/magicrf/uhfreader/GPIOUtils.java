package com.magicrf.uhfreader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

/**
 * @author leysin
 * Dec 21, 2018
 * 6:36:12 PM
 */
public class GPIOUtils {
	
	
	public static String high = "1";
	
	public static String low = "0";
	
	private static final String TAG = "GPIOUtils";
	/**
	 * write Value to Node 
	 * @param node  node path
	 * @param cmd 0 or 1   0-low  1-high
	 */
	public static void writeValueToNode(String node,String cmd){
		BufferedWriter bufWriter = null;
		try {
			bufWriter = new BufferedWriter(new FileWriter(node));
			bufWriter.write(cmd);
			bufWriter.close();
			Log.i(TAG, "writeValueToNode:" + node + ">" + cmd);
		} catch (IOException e) {
			Log.i(TAG, "writeValueToNode Excption!");
			e.printStackTrace();
		}
	}
	
	/**
	 * get nodes status
	 * @param node
	 * @return
	 */
	public static String getNodeStatus(String node){
		BufferedReader bufReader = null;
		String state = "";
		try {
			bufReader = new BufferedReader(new FileReader(node));
			state = bufReader.readLine();
			bufReader.close();
			if(state.isEmpty()){
				state = "No access to node state";
			}
		} catch (IOException e) {
			e.printStackTrace();
			state = "Not get state!";
			Log.i(TAG, "getNodeStatus Exception!");
		}
		return state;
	}

}
