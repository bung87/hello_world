package com.example.helloworld;

import java.io.UnsupportedEncodingException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;


import android.content.Context;
import android.content.Intent;

import android.view.Menu;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.Toast;


public class StartUpActivity extends BaseActivity {
	private final String WEATHER_API = "http://bungos.me/weather.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_up);
		
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			class InitialTask extends AsyncTask<String, Void, String>{		
				@Override
				protected String doInBackground(String... urls) {
					final String url=urls[0];
					String data = "";
					HttpRequest loadWeatherInfo = new HttpRequest(url);
					Bundle bd = loadWeatherInfo.getResult();
					data = bd.getString("result");
					
					return data;
				}
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}
				@Override
				protected void onPostExecute(String result) {
					 writeInternalStoragePrivate("weatherInfo.json",result.getBytes());		
					 
					 StartMainActivity(result);
				}
			}
			InitialTask li = new InitialTask();
			li.execute(WEATHER_API);
			
		} else {
			Toast.makeText(this, "尚未联网，将使用缓存数据", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				public void run() {
					byte[] bytes = readInternalStoragePrivate("weatherInfo.json");
					String result;
//					result = bytes.toString();
						try {
							result = new String(bytes,"UTF-8");
							
							StartMainActivity(result);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					
				}
			}, 1000);

		}
	}

	public void StartMainActivity(String result){
		
		 Intent itent=new Intent();
		 itent.putExtra("initialInfo", result);
		 itent.setClass(StartUpActivity.this, MainActivity.class);
//		 setResult(RESULT_OK, itent);
//		 startActivityForResult(itent,RESULT_OK);
		 ImageView iv=(ImageView)findViewById(R.id.startup_image);

		 Animation ani=AnimationUtils.loadAnimation(this, R.anim.push_left_out);
		 iv.startAnimation(ani);

		 startActivity(itent);
		 overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		 StartUpActivity.this.finish();	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_up, menu);
		return true;
	}

}
