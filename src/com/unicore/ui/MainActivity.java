package com.unicore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;
import android.view.View;

import com.magicrf.uhfreader.RFIDActivity;
import com.unicore.tools.R;
import com.unicore.utils.ULog;

/**
 * @author leysin
 * @date Apr 9, 2018
 * @Time 4:54:00 PM
 */
public class MainActivity extends BaseActivity {
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.KEYCODE_BACK){
			MainActivity.this.finish();
		}
		return super.dispatchKeyEvent(event);
	}
	@Override
	public <T extends View> int setLayout() {
		return R.layout.activity_main;
	}

	@Override
	public void findViews() {
		getFragmentManager().beginTransaction()
		.add(R.id.fragment_container,new FuncListFragment()).commit();
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void initData() {
		
	}
	
	
	public class FuncListFragment extends PreferenceFragment{
		public static final String LIST_FRAGMENT_PREFERENCE_NAME = "func_list_preference";
		public final String TAG = ULog.getClassName(this);
		
		private Preference uartPreference;
		private Preference qrPreference;
		private Preference keyPreference;
		private Preference psamPreference;
		private Preference rfidPreference;
		private Preference nfcPreference;
		private Preference identityPreference;
		private Preference versionPreference;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getPreferenceManager().setSharedPreferencesName(LIST_FRAGMENT_PREFERENCE_NAME);
			addPreferencesFromResource(R.xml.list_fragment);
			findPreferences();
		}
		
		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
				Preference preference) {
			ULog.i(TAG, "onPreferenceTreeClick=" + preference.getKey());
			
			if(preference.getKey().equals(getString(R.string.key_uart_test))){
				lunchActivity(MainActivity.this,UartTestActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_qr_test))) {
				lunchActivity(MainActivity.this,QRTestActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_key_test))) {
				lunchActivity(MainActivity.this,KeyTestActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_nfc_test))) {
				lunchActivity(MainActivity.this,NFCTestActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_psam_test))) {
				lunchActivity(MainActivity.this,PsamTestActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_rfid_test))) {
				lunchActivity(MainActivity.this,RFIDActivity.class);
			}else if (preference.getKey().equals(getString(R.string.key_version_info))) {
				showVersionDialog();
			}
			
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
		
		public void findPreferences() {
			uartPreference = findPreference(getString(R.string.key_uart_test));
			qrPreference = findPreference(getString(R.string.key_qr_test));
			keyPreference = findPreference(getString(R.string.key_key_test));
			psamPreference = findPreference(getString(R.string.key_psam_test));
			rfidPreference = findPreference(getString(R.string.key_rfid_test));
			nfcPreference = findPreference(getString(R.string.key_nfc_test));
			versionPreference = findPreference(getString(R.string.key_version_info));
			identityPreference = findPreference(getString(R.string.key_identity_test));
			//removePreference(rfidPreference);
			removePreference(identityPreference);
		}
		
		public void removePreference(Preference preference){
			((PreferenceGroup)findPreference("root_preference")).removePreference(preference);
		}
		
		public void showVersionDialog() {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setMessage(R.string.str_version_info)
			.setTitle(getString(R.string.sp_version_info))
			.setNegativeButton(getString(R.string.str_determine), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
				}
			}).create().show();
		}
	}


	public void lunchActivity(Activity activity,
			Class<? extends Activity> cls) {
		Intent intent = new Intent(activity, cls);
		MainActivity.this.startActivity(intent);
	}



}
