package com.unicore.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.unicore.tools.R;
import com.unicore.ui.NFCTestActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.os.Parcelable;

@SuppressLint("NewApi")
public class NFCUtils {  

	//nfc  
	public static NfcAdapter mNfcAdapter;  
	public static IntentFilter[] mIntentFilter = null;  
	public static PendingIntent mPendingIntent = null;  
	public static String[][] mTechList = null;  

	/** 
	 * 构造函数，用于初始化nfc
	 * @return 
	 */  
	public static void init(Activity activity) {  
		mNfcAdapter = NfcCheck(activity);  
		NfcInit(activity);  
	}  

	/** 
	 * 检查NFC是否打开 
	 */  
	public static NfcAdapter NfcCheck(Activity activity) {  
		NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);  
		if (mNfcAdapter == null) {  
			return null;  
		} else {  
			if(NFCUtils.mNfcAdapter!= null && !NFCUtils.mNfcAdapter.isEnabled()){
				showNFCDialog(activity);
			}
		}  
		return mNfcAdapter;  
	}  

	private static void showNFCDialog(final Activity activity) {
		final AlertDialog.Builder normalDialog = 
				new AlertDialog.Builder(activity);
		normalDialog.setIcon(R.drawable.ic_launcher);
		normalDialog.setTitle("NFC");
		normalDialog.setMessage("系统NFC功能未打开,是否跳转至NFC界面开启");
		normalDialog.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent setNfc= new Intent(Settings.ACTION_NFC_SETTINGS);
				activity.startActivity(setNfc);
			}
		});
		normalDialog.setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		normalDialog.setCancelable(false);
		normalDialog.create().show();
	}

	/** 
	 * 初始化nfc设置 
	 */  
	public static void NfcInit(Activity activity) {  
		mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);  
		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);  
		IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);  
		try {  
			filter.addDataType("*/*");  
		} catch (IntentFilter.MalformedMimeTypeException e) {  
			e.printStackTrace();  
		}  
		mIntentFilter = new IntentFilter[]{filter, filter2};  
		mTechList = null;  
	}  

	/**  
	 * 读取NFC的数据  
	 */  
	public static String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {  
		Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);  
		if (rawArray != null) {  
			NdefMessage mNdefMsg = (NdefMessage) rawArray[0];  
			NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];  
			if (mNdefRecord != null) {  
				String readResult = new String(mNdefRecord.getPayload(), "UTF-8");  
				return readResult;  
			}  
		}  
		return "";  
	}  


	/** 
	 * 往nfc写入数据 
	 */  
	public static void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {  
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		Ndef ndef = Ndef.get(tag);  
		ndef.connect();  
		NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);  
		NdefRecord[] records = {ndefRecord};  
		NdefMessage ndefMessage = new NdefMessage(records);  
		ndef.writeNdefMessage(ndefMessage);  
	}  

	/** 
	 * 读取nfcID 
	 */  
	public static String readNFCId(Intent intent) throws UnsupportedEncodingException {  
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		String id = ByteArrayToHexString(tag.getId());  
		return id;  
	}  

	/** 
	 * 将字节数组转换为字符串 
	 */  
	private static String ByteArrayToHexString(byte[] inarray) {  
		int i, j, in;  
		String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};  
		String out = "";  

		for (j = 0; j < inarray.length; ++j) {  
			in = (int) inarray[j] & 0xff;  
			i = (in >> 4) & 0x0f;  
			out += hex[i];  
			i = in & 0x0f;  
			out += hex[i];  
		}  
		return out;  
	}  


	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	public static String processIntent(Intent intent) {
		//取出封装在intent中的TAG
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		String CardId =ByteArrayToHexString(tagFromIntent.getId());
		String metaInfo = "";
		metaInfo+="卡片ID:"+CardId;
		for (String tech : tagFromIntent.getTechList()) {
			ULog.i("NFCUtils",tech);
		}
		boolean auth = false;
		//读取TAG
		MifareClassic mfc = MifareClassic.get(tagFromIntent);
		try {
			//Enable I/O operations to the tag from this TagTechnology object.
			mfc.connect();
			int type = mfc.getType();//获取TAG的类型
			int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
			String typeS = "";
			switch (type) {
			case MifareClassic.TYPE_CLASSIC:
				typeS = "TYPE_CLASSIC";
				break;
			case MifareClassic.TYPE_PLUS:
				typeS = "TYPE_PLUS";
				break;
			case MifareClassic.TYPE_PRO:
				typeS = "TYPE_PRO";
				break;
			case MifareClassic.TYPE_UNKNOWN:
				typeS = "TYPE_UNKNOWN";
				break;
			}
			metaInfo += "\n卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
					+ mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
			for (int j = 0; j < sectorCount; j++) {
				//Authenticate a sector with key A.
				auth = mfc.authenticateSectorWithKeyA(j,
						MifareClassic.KEY_DEFAULT);
				int bCount;
				int bIndex;
				if (auth) {
					metaInfo += "Sector " + j + ":验证成功\n";
					// 读取扇区中的块
					bCount = mfc.getBlockCountInSector(j);
					bIndex = mfc.sectorToBlock(j);
					for (int i = 0; i < bCount; i++) {
						byte[] data = mfc.readBlock(bIndex);
						metaInfo += "Block " + bIndex + " : "
								+ bytesToHexString(data) + "\n";
						bIndex++;
					}
				} else {
					metaInfo += "Sector " + j + ":验证失败\n";
				}
			}
			//Toast.makeText(this, metaInfo, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metaInfo;
	}

	//字符序列转换为16进制字符串
	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("0x");
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			//System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

}
