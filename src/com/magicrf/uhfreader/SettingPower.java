package com.magicrf.uhfreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magicrf.uhfreaderlib.reader.UhfReader;
import com.unicore.tools.R;
/**
 * 设置功率，调节距离
 * @author Administrator
 *
 */
public class SettingPower extends Activity implements OnClickListener{

	private Button buttonMin;
	private Button buttonPlus;
	private Button buttonSet;
	private EditText editValues ;
	private int value = 26 ;//初始值为最大，2600为26dbm(value范围16dbm~26dbm)
	private UhfReader reader ;
	
	private Button buttonSetPath;
	private String portPath = "/dev/ttyHSL0";
	private EditText editPortPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_power);
		super.onCreate(savedInstanceState);
		initView();
		reader = UhfReader.getInstance();
	}
	
	private void initView(){
		buttonMin = (Button) findViewById(R.id.button_min);
		buttonPlus = (Button) findViewById(R.id.button_plus);
		buttonSet = (Button) findViewById(R.id.button_set);
		editValues = (EditText) findViewById(R.id.editText_power);
		
		buttonSetPath = (Button) findViewById(R.id.button_set_path);
		buttonSetPath.setOnClickListener(this);
		editPortPath = (EditText) findViewById(R.id.editText_port_path);
		portPath = getSharedPortPath();
		editPortPath.setText(portPath);
		
		buttonMin.setOnClickListener(this);
		buttonPlus.setOnClickListener(this);
		buttonSet.setOnClickListener(this);
		value =  getSharedValue();
		editValues.setText("" +value);
		
	}

	//获取存储Value
	private int getSharedValue(){
		SharedPreferences shared = getSharedPreferences("power", 0);
		return shared.getInt("value", 26);
	}
	
	private String getSharedPortPath() {
		SharedPreferences shared = getSharedPreferences("portPath", 0);
		return shared.getString("portPath", "/dev/ttyHSL0");
	}

	private void saveSharedPortPath(String portPath) {
		SharedPreferences shared = getSharedPreferences("portPath", 0);
		Editor editor = shared.edit();
		editor.putString("portPath", portPath);
		editor.commit();
	}

	//保存Value
	private void saveSharedValue(int value){
		SharedPreferences shared = getSharedPreferences("power", 0);
		Editor editor = shared.edit();
		editor.putInt("value", value);
		editor.commit();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_min://减
			if(value > 16){
				value = value - 1;
			}
			editValues.setText(value + "");
			break;
		case R.id.button_plus://加
			if(value < 26){
				value = value + 1;
			}
			editValues.setText(value + "");
			break;
		case R.id.button_set://设置
			if(reader.setOutputPower(value)){
				saveSharedValue(value);
				Toast.makeText(getApplicationContext(), "设置成功", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), "设置失败", 0).show();
			}
		    break;
		case R.id.button_set_path: //设置串口设备路径
			portPath = editPortPath.getText().toString();
			reader.setPortPath(portPath);
			saveSharedPortPath(portPath);
			Toast.makeText(getApplicationContext(), "设置串口路径后需要重新启动软件才能生效!", 0).show();
			break;

		default:
			break;
		}
		
	}
	
}
