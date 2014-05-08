package com.example.helloworld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateManager {  
	  
    private Context mContext;  
      
    //��ʾ��  
    private String updateMsg = "�����µ������Ŷ���׿����ذ�~";  
      
    //���صİ�װ��url  
    private String apkUrl = "http://dldir1.qq.com/music/clntupate/QQMusicV3.5.apk";  
      
      
    private Dialog noticeDialog;  
      
    private Dialog downloadDialog;  
     /* ���ذ���װ·�� */  
    private static final String savePath = Environment.getExternalStorageDirectory().getPath()+"/updatedemo/";  
      
    private static final String saveFileName = savePath + "UpdateDemoRelease.apk";  
  
    /* ��������֪ͨuiˢ�µ�handler��msg���� */  
    private ProgressBar mProgress;  
  
      
    private static final int DOWN_UPDATE = 1;  
      
    private static final int DOWN_OVER = 2;  
      
    private int progress;  
      
    private Thread downLoadThread;  
      
    private boolean interceptFlag = false;  
      
    private Handler mHandler = new Handler(){  
        @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case DOWN_UPDATE:  
                mProgress.setProgress(progress);  
                break;  
            case DOWN_OVER:  
            	downloadDialog.dismiss();
                installApk();  
                break;  
            default:  
                break;  
            }  
        };  
    };  
      
    public UpdateManager(Context context) {  
        this.mContext = context;  
    }  
      
    //�ⲿ�ӿ�����Activity����  
    public void checkUpdateInfo(){  
        showNoticeDialog();  
    }  
      
      
    private void showNoticeDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("����汾����");  
        builder.setMessage(updateMsg); 
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {           
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                showDownloadDialog();             
            }  
        });  
        builder.setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {             
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();                 
            }  
        });  
        noticeDialog = builder.create();  
        noticeDialog.show();  
    }  
      
    private void showDownloadDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle(new String("����汾����"));  
          
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.progress, null);  
        mProgress = (ProgressBar)v.findViewById(R.id.progress);  
          
        builder.setView(v);  
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {   
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                interceptFlag = true;  
            }  
        });  
        downloadDialog = builder.create();  
        downloadDialog.show();  
          
        downloadApk();  
    }  
      
    private Runnable mdownApkRunnable = new Runnable() {      
        @Override  
        public void run() {  
            try {  
                URL url = new URL(apkUrl);  
              
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                conn.connect();  
                int length = conn.getContentLength();  
                InputStream is = conn.getInputStream();  
                  
                File file = new File(savePath);  
                if(!file.exists()){  
                    file.mkdir();  
                }  
                String apkFile = saveFileName;  
                File ApkFile = new File(apkFile);  
                FileOutputStream fos = new FileOutputStream(ApkFile);  
                  
                int count = 0;  
                byte buf[] = new byte[1024];  
                  
                do{                   
                    int numread = is.read(buf);  
                    count += numread;  
                    progress =(int)(((float)count / length) * 100);  
                    //���½���  
                    mHandler.sendEmptyMessage(DOWN_UPDATE);  
                    if(numread <= 0){      
                        //�������֪ͨ��װ  
                        mHandler.sendEmptyMessage(DOWN_OVER);  
                        break;  
                    }  
                    fos.write(buf,0,numread);  
                }while(!interceptFlag);//���ȡ����ֹͣ����.  
                  
                fos.close();  
                is.close();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch(IOException e){  
                e.printStackTrace();  
            }  
              
        }  
    };  
      
     /** 
     * ����apk 
     * @param url 
     */  
      
    private void downloadApk(){  
        downLoadThread = new Thread(mdownApkRunnable);  
        downLoadThread.start();  
    }  
     /** 
     * ��װapk 
     * @param url 
     */  
    private void installApk(){  
        File apkfile = new File(saveFileName);  
        if (!apkfile.exists()) {  
            return;  
        }      
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");   
        mContext.startActivity(i);  
      
    }  
}  
