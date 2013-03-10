package com.example.atcommandstest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements OnClickListener {
	public String TAG = "ATCommander";
	private boolean isFinished = false;
	
	public String ttyPath;
	public EditText PathField;
	public EditText CommandField;
	public TextView result;
	public TextView Logcat;
	
	final String CMD1 = "CMD1";
	final String CMD2 = "CMD2";
	final String CMD3 = "CMD3";
	final String CMD4 = "CMD4";
	final String CMD5 = "CMD5";
	
	public Button btnCMD1;
	public Button btnCMD2;
	public Button btnCMD3;
	public Button btnCMD4;
	public Button btnCMD5;
	public Button btnControlZ;
	
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0 ) {
				updateResult(msg.obj.toString());
			}
			if (msg.what == 1 ) {
				updateLogcat(msg.obj.toString());
			} 
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		result = (TextView) findViewById(R.id.textView4);
		result.setText("");
		
		Logcat = (TextView) findViewById(R.id.textView6);
		Logcat.setText("");
		
		PathField = (EditText)findViewById(R.id.editText1);
		CommandField = (EditText)findViewById(R.id.editText2);
		
		btnCMD1 = (Button)findViewById(R.id.buttonCMD1);
		btnCMD1.setOnClickListener(this);
		btnCMD2 = (Button)findViewById(R.id.buttonCMD2);
		btnCMD2.setOnClickListener(this);
		btnCMD3 = (Button)findViewById(R.id.buttonCMD3);
		btnCMD3.setOnClickListener(this);
		btnCMD4 = (Button)findViewById(R.id.buttonCMD4);
		btnCMD4.setOnClickListener(this);
		btnCMD5 = (Button)findViewById(R.id.buttonCMD5);
		btnCMD5.setOnClickListener(this);
		btnControlZ = (Button)findViewById(R.id.ControlZ);
		btnControlZ.setOnClickListener(this);
		
		String driverPath = getPreference("DriverPath");
		if (!driverPath.equals("")){
			PathField.setText(driverPath);
		}
		
		getLogcat();
	}
	
	protected void clearLogcat() {
		this.Logcat.setText("");
	}
	protected void updateLogcat(String logcatLine) {
		this.Logcat.setMovementMethod(new ScrollingMovementMethod());
        this.Logcat.append(logcatLine);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
    
	public void setPathToTTY() {
		ttyPath = PathField.getText().toString();
		Log.i(TAG, "path is set to " + ttyPath + "!");
	}
	
	//This method is invoked by Submit button click
	public void submitButtonClick(View view) {
		Log.i(TAG, "Submit button is clicked");
		setPathToTTY();
		checkRoot();
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true)
					try {
						Message localMessage = new Message();
						localMessage.what = 0;
						localMessage.obj = getCommandResult();
						handler.sendMessage(localMessage);
						Thread.sleep(200);
					} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					ToastMsg(e.toString());
					}
			}
		});
		t.start();
	}
	
	//This method is invoked by Go button click
	public void goButtonClick(View view) {
		Log.i(TAG, "GO button is clicked");
		clearLogcat();
		sendATCommand();
	}
	
	public void ToastMsg(String paramString) {
	    Toast localToast = Toast.makeText(this, paramString, Toast.LENGTH_SHORT);
	    localToast.setGravity(17, 0, 0);
	    localToast.show();
	}
	
	public boolean checkRoot() {
	    Process exec;
	    try {
	    	ToastMsg("Root checking...");
	        exec = Runtime.getRuntime().exec("su");

	        final OutputStreamWriter out = new OutputStreamWriter(exec.getOutputStream());
	        String changePropertiesCommand = "chmod 777 " + this.ttyPath + "\n";
	        out.write(changePropertiesCommand);
	        out.write("exit");
	        out.flush();

	        Log.i(TAG, "su command executed successfully");
	        ToastMsg("Your device has root.");
	        getLogcat();
	        return true;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }  
	    ToastMsg("Your device has no root.");
	    return false;
	}
	
	private void getLogcat() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				  try {
				      Process process2 = Runtime.getRuntime().exec("su");
		              DataOutputStream writer =  new DataOutputStream(process2.getOutputStream());
		              writer.flush();
		              writer.writeBytes("pm grant com.example.atcommandstest android.permission.READ_LOGS\n");
		              writer.flush();
		              writer.writeBytes("logcat -b radio\n");
				      Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Logcat");
				      BufferedReader bufferedReader = new BufferedReader(
				      new InputStreamReader(process2.getInputStream()));
				      //StringBuilder log=new StringBuilder();
				      String line = "";
				      Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Starting while");
				      while(true) {
				          //log.append(line);
				        	line = bufferedReader.readLine();
				        	Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>log is: " + line);
				        	if(line.toString().contains("AT<")){
								try {
									Message localMessage2 = new Message();
									localMessage2.what = 1;
									localMessage2.obj = line.toString();
									handler.sendMessage(localMessage2);
									Thread.sleep(200);
								} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								ToastMsg(e.toString());
								}
				        	}
				        }
				      } 
				  catch (IOException e) {Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Some error is occurred in logcat thread" + e.toString());} 
			}
		});
		t1.start();
		
	}

	public void sendATCommand() {
		String ATCommand = CommandField.getEditableText().toString() + "\r";
		RandomAccessFile localRandomAccessFile;
		try {
			localRandomAccessFile = new RandomAccessFile(ttyPath, "rw");
	        localRandomAccessFile.writeBytes(ATCommand);
	        localRandomAccessFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ToastMsg(e.toString());
		}

	}
	
	public String getCommandResult() {
		Log.i(TAG, "TTY file is  " + ttyPath + "!");
		if (!ttyPath.equals("")) {
			try {
				RandomAccessFile localRandomAccessFile = new RandomAccessFile(ttyPath, "r");
	        	byte[] arrayOfByte = new byte[1024];
				localRandomAccessFile.read(arrayOfByte);
	        	String resultString = new String(arrayOfByte);
	        	if (isFinished) {
	        		localRandomAccessFile.close();
	        	}
	        	return resultString;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return e.toString();
			}
		}
		else {
			return "Path to required file is not setup. Please, setup correct path and click Submit button again.";
		}

	}
	
	public void updateResult(String result) {
		this.result.setText("");
		this.result.setMovementMethod(new ScrollingMovementMethod());
        this.result.append(result);
	}
	
	public void onDestroy() {
		isFinished = true;
	    try {
	        android.os.Process.killProcess(android.os.Process.myPid());
	        System.exit(0);
	        return;
	    } catch (Exception localException) {
	    	localException.printStackTrace();
	    }
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonCMD1:
			String prefValue1 = getPreference(CMD1);
			CommandField.setText(prefValue1);
			break;
		case R.id.buttonCMD2:
			String prefValue2 = getPreference(CMD2);
			CommandField.setText(prefValue2);
			break;
		case R.id.buttonCMD3:
			String prefValue3 = getPreference(CMD3);
			CommandField.setText(prefValue3);
			break;
		case R.id.buttonCMD4:
			String prefValue4 = getPreference(CMD4);
			CommandField.setText(prefValue4);
			break;
		case R.id.buttonCMD5:;
			String prefValue5 = getPreference(CMD5);
			CommandField.setText(prefValue5);
			break;
		case R.id.ControlZ:
			CommandField.append(Character.toString((char)26));
			break;
		default:
			break;
		}
		
	}

	private String getPreference(String key) {
		SharedPreferences sharedPref = getSharedPreferences("ATCommanderPreferences", MODE_PRIVATE);
		String value = sharedPref.getString(key, "");
		return value;
	}

}
