package com.example.atcommandstest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsActivity extends Activity implements OnClickListener {
	final String CMD1 = "CMD1";
	final String CMD2 = "CMD2";
	final String CMD3 = "CMD3";
	final String CMD4 = "CMD4";
	final String CMD5 = "CMD5";
	final String DriverPath = "DriverPath";
	
	public EditText CMD1EditText;
	public EditText CMD2EditText;
	public EditText CMD3EditText;
	public EditText CMD4EditText;
	public EditText CMD5EditText;
	public EditText DriverPathEditText;
	
	public Button btnSave1;
	public Button btnSave2;
	public Button btnSave3;
	public Button btnSave4;
	public Button btnSave5;
	public Button btnSaveDriverPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		CMD1EditText = (EditText)findViewById(R.id.editText1);
		CMD2EditText = (EditText)findViewById(R.id.editText2);
		CMD3EditText = (EditText)findViewById(R.id.editText3);
		CMD4EditText = (EditText)findViewById(R.id.editText4);
		CMD5EditText = (EditText)findViewById(R.id.editText5);
		DriverPathEditText = (EditText)findViewById(R.id.editText11);
		
		btnSave1 = (Button)findViewById(R.id.buttonSave1);
		btnSave1.setOnClickListener(this);
		btnSave2 = (Button)findViewById(R.id.buttonSave2);
		btnSave2.setOnClickListener(this);
		btnSave3 = (Button)findViewById(R.id.buttonSave3);
		btnSave3.setOnClickListener(this);
		btnSave4 = (Button)findViewById(R.id.buttonSave4);
		btnSave4.setOnClickListener(this);
		btnSave5 = (Button)findViewById(R.id.buttonSave5);
		btnSave5.setOnClickListener(this);
		btnSaveDriverPath = (Button)findViewById(R.id.buttonSave11);
		btnSaveDriverPath.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.buttonSave1:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button1 is pressed");
			savePreference(CMD1, CMD1EditText.getText().toString());
			break;
		case R.id.buttonSave2:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button2 is pressed");
			savePreference(CMD2, CMD2EditText.getText().toString());
			break;
		case R.id.buttonSave3:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button3 is pressed");
			savePreference(CMD3, CMD3EditText.getText().toString());
			break;
		case R.id.buttonSave4:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button4 is pressed");
			savePreference(CMD4, CMD4EditText.getText().toString());
			break;
		case R.id.buttonSave5:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button5 is pressed");
			savePreference(CMD5, CMD5EditText.getText().toString());
			break;
		case R.id.buttonSave11:
			Log.i("ATCommander", ">>>>>>>>>>>>>>>>>>>>>>>>>>button11 is pressed");
			savePreference(DriverPath, DriverPathEditText.getText().toString());
			break;
		default:
			break;
		}
			
	}

	private void savePreference(String key, String value) {
		SharedPreferences sharedPref = getSharedPreferences("ATCommanderPreferences", MODE_PRIVATE);
		Editor sharedPreferencesEditor = sharedPref.edit();
		sharedPreferencesEditor.putString(key, value).commit();
		
	}

}
