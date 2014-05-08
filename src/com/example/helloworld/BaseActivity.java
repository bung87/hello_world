package com.example.helloworld;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;



import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity  extends Activity {
	public static byte SHORT_TOAST = 0;
    public static byte LONG_TOAST = 1;
    private byte back_count=0;
    Timer tExit = new Timer();
    TimerTask tt;
    private void beginTask() {
    	  if (tt != null) {
    	   tt.cancel();
    	  }
    	  tt = new TimerTask() {
    	   @Override
    	   public void run() {   	   
    		   back_count = 0;	   
    	   }
    	  };
    	  tExit.schedule(tt, 2000);
    	 }
	public static void displayToast(Context caller, String toastMsg, int toastType){

	        try {// try-catch to avoid stupid app crashes
	            LayoutInflater inflater = LayoutInflater.from(caller);
	            View mainLayout = inflater.inflate(R.layout.toast_layout, null);
	            View rootLayout = mainLayout.findViewById(R.id.toast_layout_root);
	            TextView text = (TextView) mainLayout.findViewById(R.id.toast_text);
	            text.setText(toastMsg);

	            Toast toast = new Toast(caller);
//	            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	            toast.setGravity(Gravity.BOTTOM,0, 100);
	            if (toastType==SHORT_TOAST)//(isShort)
	                toast.setDuration(Toast.LENGTH_SHORT);
	            else
	                toast.setDuration(Toast.LENGTH_LONG);
	            toast.setView(rootLayout);
	            toast.show();
	        }
	        catch(Exception ex) {// to avoid stupid app crashes
	           
	        }
	    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		  if (keyCode == KeyEvent.KEYCODE_BACK) {

			  back_count++;
			  switch (back_count){
				  case 1:
					  displayToast(getApplicationContext(),getString(R.string.confirmexit),SHORT_TOAST);
					  beginTask();
					 
				  break;
				  case 2:
					  finish();
					  overridePendingTransition(0,R.anim.zoom_exit2);
					 //  System.exit(0);
				  break;
			  }
			 if(back_count!=2) return false;
		  }
		 
		return super.onKeyDown(keyCode, event);
	}
	public void writeInternalStoragePrivate(String filename, byte[] content) {
		try {
			// MODE_PRIVATE creates/replaces a file and makes
			// it private to your application. Other modes:
			// MODE_WORLD_WRITEABLE
			// MODE_WORLD_READABLE
			// MODE_APPEND
			FileOutputStream fos = openFileOutput(filename,
					Context.MODE_PRIVATE);

			fos.write(content);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<ResolveInfo> getShareApps(Context context) {	
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
//		intent.setType("*/*");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, 
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		
		return mApps;
	}
	protected List<AppInfo> getShareAppList() {	
		List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
		PackageManager packageManager = getPackageManager();
		Context mContext = getApplicationContext();
		List<ResolveInfo> resolveInfos = getShareApps(mContext);
		if (null == resolveInfos) {
			return null;
		} else {
			for (ResolveInfo resolveInfo : resolveInfos) {
				String appName=resolveInfo.loadLabel(packageManager).toString();
				if(appName.equalsIgnoreCase("WLAN") || resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.android.bluetooth")) continue;
		
				AppInfo appInfo = new AppInfo();
				appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);

				appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
				appInfo.setAppName(appName);
				
				appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
				shareAppInfos.add(appInfo);
			}
		}		
		return shareAppInfos;
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    displayToast(getApplicationContext(), "context menu show",Toast.LENGTH_LONG);
	/*    new Handler().post(new Runnable() {
            @SuppressLint("ResourceAsColor")
			public void run() {
                // set the background drawable
            	 v.setBackgroundColor(R.color.blue);
            }
        });*/
	   
	 /*   MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);*/
	}
	public byte[] readInternalStoragePrivate(String filename) {
		int len = 1024;
		byte[] buffer = new byte[len];
		try {
			FileInputStream fis = openFileInput(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int nrb = fis.read(buffer, 0, len); // read up to len bytes
			while (nrb != -1) {
				baos.write(buffer, 0, nrb);
				nrb = fis.read(buffer, 0, len);
			}
			buffer = baos.toByteArray();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

}
