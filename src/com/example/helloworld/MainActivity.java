package com.example.helloworld;

import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({"ResourceAsColor", "HandlerLeak"})
public class MainActivity extends BaseActivity {
//	private ImageView weather_image;
	public String imageUrl;
	private JSONObject jsonObject;
	public static Handler main_handler,changeRounder,weather_icon_handler;
	private String todayshareText;
	private String tomorrowshareText;
	private PopupWindow sharePopupWindow=null;
	public void render(String json) {
		TextView pubView = (TextView) findViewById(R.id.pub);
	
		TextView tempView = (TextView) findViewById(R.id.temprature);

		TextView chillView = (TextView) findViewById(R.id.wind_chill);
		TextView directionView = (TextView) findViewById(R.id.wind_direction);
		TextView speedView = (TextView) findViewById(R.id.wind_speed);
		TextView sunriseView = (TextView) findViewById(R.id.astronomy_sunrise);
		TextView sunsetView = (TextView) findViewById(R.id.astronomy_sunset);
		TextView humidityView = (TextView) findViewById(R.id.humidity);
		TextView visibilityView = (TextView) findViewById(R.id.visibility);
		TextView pressureView = (TextView) findViewById(R.id.pressure);
		TextView lowView = (TextView) findViewById(R.id.temperature_low);
		TextView highView = (TextView) findViewById(R.id.temperature_high);
		TextView lowView_tomo = (TextView) findViewById(R.id.temperature_low2);
		TextView highView_tomo = (TextView) findViewById(R.id.temperature_high2);
		TextView text = (TextView) findViewById(R.id.text);
		TextView text_tomo = (TextView) findViewById(R.id.text2);
		TextView cityView=(TextView) findViewById(R.id.titlebar_text);
		try {
			jsonObject = new JSONObject(json);
			pubView.append(jsonObject.getString("pub"));
			cityView.setText(jsonObject.getString("name"));
			JSONObject wind = jsonObject.getJSONObject("wind");

			String dg =  (String) getText(R.string.degrees);
			chillView.setText(wind.getString("chill")+dg);
			
			
			directionView.setText(wind.getString("direction"));
			speedView.setText(wind.getString("speed"));

			JSONObject astronomy = jsonObject.getJSONObject("astronomy");

			sunriseView.append(astronomy.getString("sunrise"));
			sunsetView.append(astronomy.getString("sunset"));

			JSONObject atmosphere = jsonObject.getJSONObject("atmosphere");

			humidityView.setText(atmosphere.getString("humidity")+"%");
			visibilityView.setText(atmosphere.getString("visibility")+"km");
			visibilityView.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.aboutvisivility).setMessage(R.string.aboutvisivility).show();
					return false;
				}
				
			});
			pressureView.append(atmosphere.getString("pressure"));
			JSONObject condition = jsonObject.getJSONObject("condition");
			tempView.setText(condition.getString("temp") + "°C");
			JSONArray two = jsonObject.getJSONArray("forecasts");
			JSONObject today = two.getJSONObject(0);
			final JSONObject tomorrow = two.getJSONObject(1);			
			
			lowView.append(today.getString("low") + "°C");
			highView.append(today.getString("high") + "°C");
		
			
			lowView_tomo.append(tomorrow.getString("low") + "°C");
			highView_tomo.append(tomorrow.getString("high") + "°C");
			
			text.setText(condition.getString("text"));
			text_tomo.setText(tomorrow.getString("text"));

			final int weatherCode1;
			weatherCode1 = condition.getInt("code");
			Log.i("AAA", weatherCode1+"");
//			new Thread( new HttpRequest("http://l.yimg.com/a/i/us/we/52/"+weatherCode1+".gif",weather_icon_handler)).start();

		Message msg = weather_icon_handler.obtainMessage();
			msg.what=weatherCode1;
			msg.arg1=1;
			weather_icon_handler.sendMessage(msg);
			
//		new Thread(new HttpRequest("http://l.yimg.com/a/i/us/we/52/"+tomorrow.getInt("code")+".gif",weather_icon_handler2)).start();
			Message msg2 = weather_icon_handler.obtainMessage();
			msg2.what=tomorrow.getInt("code");
			msg2.arg1=2;
			weather_icon_handler.sendMessage(msg2);
			todayshareText=getString(R.string.today);
			
			todayshareText+=getString(R.string.weather);
			todayshareText+=today.getString("text");
			todayshareText+=getString(R.string.temperature)+condition.getString("temp") + "°C ";
			todayshareText+=getString(R.string.temperature_low)+today.getString("low") + "°C ";
			todayshareText+=getString(R.string.temperature_hight)+today.getString("high") + "°C ";
			todayshareText+=getString(R.string.chill)+wind.getString("chill")+dg+" ";
			todayshareText+=getString(R.string.direction)+wind.getString("direction")+" ";
			todayshareText+=getString(R.string.speed)+wind.getString("speed")+" ";
			todayshareText+=getString(R.string.pressure)+atmosphere.getString("pressure")+",";
			todayshareText+=getString(R.string.visibility)+atmosphere.getString("visibility")+"km ";
			todayshareText+=getString(R.string.humidity)+atmosphere.getString("humidity")+"%,";
			todayshareText+=getString(R.string.sunrise)+astronomy.getString("sunrise")+" ";
			todayshareText+=getString(R.string.sunset)+astronomy.getString("sunset")+"。";
			tomorrowshareText=getString(R.string.tomorrow);
			tomorrowshareText+=getString(R.string.weather);
			tomorrowshareText+=tomorrow.getString("text");
			tomorrowshareText+=getString(R.string.temperature_low)+tomorrow.getString("low") + "°C ";
			tomorrowshareText+=getString(R.string.temperature_hight)+tomorrow.getString("high") + "°C。 ";
			// imageUrl=today.getString("image_large");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			displayToast(getApplicationContext(), "数据错误！",Toast.LENGTH_LONG);
			//Toast.makeText(MainActivity.this, "数据错误！", Toast.LENGTH_LONG).show();
			finish();
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.activity_main);
		findViewById(R.id.RelativeLayout1).setOnTouchListener(new OnTouchListener(){
			@Override  
            public boolean onTouch(View v, MotionEvent event)  
            {  
				if(sharePopupWindow!=null){sharePopupWindow.dismiss();}
				
                return false;  
            }  
			});
		UpdateManager  mUpdateManager = new UpdateManager(this);  
	        mUpdateManager.checkUpdateInfo();  
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		Intent intent = getIntent();
		final String str=intent.getStringExtra("initialInfo");
		new Handler().post(new Runnable(){
			public void run() {
				render(str);
			}
		});
		 changeRounder = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					ImageView rounder1 = (ImageView) findViewById(R.id.rounder1);
					ImageView rounder2 = (ImageView) findViewById(R.id.rounder2);
				if(msg!=null){
					switch (msg.what) {
						case 1 :
							rounder1.setImageResource(R.drawable.rounder_b);
							rounder2.setImageResource(R.drawable.rounder1);
							break;
						case 2 :
							rounder1.setImageResource(R.drawable.rounder1);
							rounder2.setImageResource(R.drawable.rounder_b);
							break;
						default:
								;
					}
					rounder1.setScaleType(ScaleType.FIT_XY);
					rounder2.setScaleType(ScaleType.FIT_XY);
				}
		
				}
		 };
	 SliderLayout s2 = (SliderLayout) findViewById(R.id.myslider);
	 ImageButton shareBtn=(ImageButton) findViewById(R.id.titlebar_share);
	
	 shareBtn.setOnTouchListener(new OnTouchListener(){
		 public boolean onTouch(View v,MotionEvent event){
			 initSharePopupWindow((View)findViewById(R.id.RelativeLayout1));
			 return false;
		 }
		 
	 });
		s2.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				SliderLayout s1 = (SliderLayout) v.findViewById(R.id.myslider);
				if (s1.getmVelocityTracker() == null) {
					s1.setmVelocityTracker(VelocityTracker.obtain());
				}
				s1.getmVelocityTracker().addMovement(event);

				final int action = event.getAction();
				final float x = event.getX();
//				final float y = event.getY();
				Message msg = changeRounder.obtainMessage();
				switch (action) {
					case MotionEvent.ACTION_DOWN :
					
						if (!s1.mScroller.isFinished()) {
							s1.mScroller.abortAnimation();
						}
						s1.mLastMotionX = x;
						break;

					case MotionEvent.ACTION_MOVE :
						
						int deltaX = (int) (s1.mLastMotionX - x);
						s1.mLastMotionX = x;

						s1.scrollBy(deltaX, 0);
						break;

					case MotionEvent.ACTION_UP :
					
						// if (mTouchState == TOUCH_STATE_SCROLLING) {
						final VelocityTracker velocityTracker = s1.getmVelocityTracker();
						velocityTracker.computeCurrentVelocity(1000);
						int velocityX = (int) velocityTracker.getXVelocity();
						if (velocityX > SliderLayout.SNAP_VELOCITY && s1.mCurScreen > 0) {
							// Fling enough to move left							
							msg.what=1;					
							changeRounder.sendMessage(msg);
							s1.snapToScreen(s1.mCurScreen - 1);
						} else if (velocityX < -SliderLayout.SNAP_VELOCITY
								&& s1.mCurScreen < s1.getChildCount() - 1) {
							// Fling enough to move right					
							msg.what=2;
							changeRounder.sendMessage(msg);								
							s1.snapToScreen(s1.mCurScreen + 1);
						} else {
							s1.snapToDestination();
						}
									
						if (s1.getmVelocityTracker() != null) {
							s1.getmVelocityTracker().recycle();
							s1.setmVelocityTracker(null);
						}
						// }
						s1.mTouchState = SliderLayout.TOUCH_STATE_REST;
						break;
					case MotionEvent.ACTION_CANCEL :
						s1.mTouchState = SliderLayout.TOUCH_STATE_REST;
						break;
				}

				return true;
			}
		});
		final ImageView weather_image = (ImageView) findViewById(R.id.weather_icon);
	
		final ImageView weather_image2 = (ImageView) findViewById(R.id.weather_icon2);

		weather_icon_handler = new Handler() {

			@Override
			public void handleMessage( Message msg) {
				Bitmap bmp = null;
				if (msg.what > 30 && msg.what < 35) {
					bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.sun);
				} else if (msg.what > 25 && msg.what < 31) {
					bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.cloudy);
				}else if(msg.what ==11|| msg.what ==12 || msg.what ==39 || msg.what ==40){
					bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.rain);
				}else if(msg.what==17 || msg.what==18||msg.what== 45 || msg.what==47){
					bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.thunderstorm );
				}
				if (null==bmp) return;
				switch(msg.arg1){
					case 1:
						weather_image.setImageBitmap(bmp);
						break;
					case 2:
						weather_image2.setImageBitmap(bmp);
						break;
					default:
						break;
				}
			/*	Bundle result= msg.getData();
				byte[] data = result.getByteArray("result");
				Bitmap bmp = null;
				bmp=BitmapFactory.decodeByteArray(data,0,data.length);
				*/
		
				
			}
		};
	
		

	}
	@Override
	@Deprecated
	public boolean onCreatePanelMenu (int featureId, Menu menu) {
	  if (   Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO
	      && Build.VERSION.SDK_INT < Build.VERSION_CODES.CUR_DEVELOPMENT
	      && featureId == Window.FEATURE_OPTIONS_PANEL) {
	    try {
	      final Field field = menu.getClass().getDeclaredField("THEME_RES_FOR_TYPE");
	      field.setAccessible(true);
	      final int[] THEME_RES_FOR_TYPE = (int[])field.get(menu);
	      THEME_RES_FOR_TYPE[0] = R.style.AppBaseTheme_IconMenu;
	      THEME_RES_FOR_TYPE[1] = R.style.AppBaseTheme_ExpandedMenu;
	    }
	    catch (Exception e) {
	      // ignore
	    }
	  }
	  return super.onCreatePanelMenu(featureId, menu);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
/*		  if (android.os.Build.VERSION.SDK_INT < 
			        android.os.Build.VERSION_CODES.CUR_DEVELOPMENT) {
				int count=menu.size();
				for(int i=0;i<count;i++){
					int itemid=menu.getItem(i).getItemId();
					 SpannableStringBuilder spannable = new SpannableStringBuilder();
					 CharSequence title= menu.getItem(i).getTitle();
					 spannable.append(menu.getItem(i).getTitle());
					 spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 
				                0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				        MenuItem item = menu.findItem(itemid);
				        item.setTitle(spannable );
				        
				}
			       
			   }*/
		return true;
	}

	protected void initSharePopupWindow(View parent) {
if(sharePopupWindow!=null){sharePopupWindow.showAtLocation( parent, Gravity.CENTER, 0, 0);return ;}

View view = null;
		ListView shareList = null;
		
			Context mContext = getApplicationContext();
			view = LayoutInflater.from(mContext).inflate(R.layout.popup_share,null);
	shareList = (ListView) view.findViewById(R.id.share_list);
			List<AppInfo> shareAppInfos = getShareAppList();	
			final ShareCustomAdapter adapter = new ShareCustomAdapter(mContext, shareAppInfos);
			shareList.setAdapter(adapter);
		
	
			shareList.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					AppInfo appInfo = (AppInfo) adapter.getItem(position);
					shareIntent.setComponent(new ComponentName(appInfo.getAppPkgName(), appInfo.getAppLauncherClassName()));
					shareIntent.setType("text/plain");

					//这里就是组织内容了，
					SliderLayout s1 = (SliderLayout) findViewById(R.id.myslider);
					int cur=s1.getCurScreen();
					String shareText;
					if(cur==0){
						shareText=todayshareText;
					}else{
						shareText=tomorrowshareText;
					}
					shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share");
					shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(shareIntent);
				}
			});

			final float density = getResources().getDisplayMetrics().density;
			sharePopupWindow = new PopupWindow(view, 
					(int)(200 * density), LinearLayout.LayoutParams.WRAP_CONTENT);
			
			//LinearLayout.LayoutParams.WRAP_CONTENT			
		//使其聚焦
		sharePopupWindow.setFocusable(true);
		//设置允许在外点击消失
		sharePopupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景

		sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		//xoff,yoff基于anchor的左下角进行偏移。正数表示下方右边，负数表示（上方左边）
		//showAsDropDown(parent, xPos, yPos);
//		sharePopupWindow.showAsDropDown(parent, 0,0);
		sharePopupWindow.showAtLocation( parent, Gravity.CENTER, 0, 0);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.share :
		/*		Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "share");
				intent.putExtra(Intent.EXTRA_TEXT,
						"I would like to share this with you...");
				startActivity(Intent.createChooser(intent, getTitle()));*/
				initSharePopupWindow((View)findViewById(R.id.RelativeLayout1));

				break;
			case R.id.action_settings :
				displayToast(getApplicationContext(), "2",Toast.LENGTH_LONG);
				break;
			case R.id.exit :
				finish();
				overridePendingTransition(0,R.anim.zoom_exit2);
				break;
			default :

				break;
		}

		return super.onOptionsItemSelected(item);
	}


}
