package com.example.helloworld;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.EncodingUtils;
public class HttpRequest implements Runnable {
	private String requestUrl="";
	private Handler handler=null;
	public Bundle result;
	public HttpRequest(String murl, Handler handler) {
		this.requestUrl = murl;
		this.handler = handler;
	}
	public HttpRequest(String murl){
		this.requestUrl = murl;
	}
	public Bundle getResult(){
		this.run();
		return this.result;
	}
	public void run() {
		{
			HttpURLConnection urlConn;
			InputStreamReader in;
			BufferedReader buffer;
			String inputLine = "";
			URL url = null;
			Message msg=new Message();
			Bundle bd =new Bundle();
			String text="";
			try {
				url = new URL(this.requestUrl);
				try {
					urlConn = (HttpURLConnection) url.openConnection();
				/*	urlConn.setConnectTimeout(5000);
					urlConn.setReadTimeout(5000);*/
					try {
						
						// 执行请求参数项
						String ct=urlConn.getContentType();
						
						if(ct.indexOf("image")==0){
							int len=0;
							InputStream ins = urlConn.getInputStream();
							        byte buf[]=new byte[1024];

							        ByteArrayOutputStream out=new ByteArrayOutputStream();

							        while((len=ins.read(buf))!=-1){
							            out.write(buf,0,len);  //把数据写入内存
							        }
							        
							        bd.putByteArray("result", out.toByteArray());
						}else{
							in = new InputStreamReader(urlConn.getInputStream());
							buffer = new BufferedReader(in);
							while (((inputLine = buffer.readLine()) != null)) {

								text += inputLine;
							}
							bd.putString("result", EncodingUtils.getString(text.getBytes(),"utf-8"));
							
						}

						result=bd;
						msg.setData(bd);
						if(handler != null){
							handler.sendMessage(msg);
						}
						
						// return resultData;
						urlConn.disconnect();
					} catch (IOException e) {
					
//						Toast.makeText(null, "网络访问失败，请检查您机器的联网设备!",Toast.LENGTH_LONG).show();
					} finally {
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
		
	}
}