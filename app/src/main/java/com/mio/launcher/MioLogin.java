package com.mio.launcher;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;


public class MioLogin{
    private LoginListener listener;
	private int SUCCESS=0,ERROR=1,START=2;
	private String url="https://authserver.mojang.com";
    public interface LoginListener {
		void onStart();
        void onSucceed(ArrayMap<String,String> map);
        void onError(String error);
    }
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==SUCCESS){
				ArrayMap<String,String> map=(ArrayMap<String,String>)msg.obj;
				listener.onSucceed(map);
			}
			if(msg.what==ERROR){
				listener.onError((String)msg.obj);
			}
			if(msg.what==START){
				listener.onStart();
			}
		}};
    public MioLogin() {
		
    }
	public void setListener(LoginListener listener){
		this.listener = listener;
	}
	public void setUrl(String url){
		this.url=url;
	}
	
    public void login(final String username, final String password) {
		listener.onStart();
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        JSONObject json=new JSONObject();
                        json.put("username", username);
                        json.put("password", password);
                        json.put("agent", new JSONObject().put("name", "Minecraft").put("version", 1));
                        json.put("requestUser", true);
                        json.put("clientToken", "mio_launcher");
                        String myUrl=url.contains("authserver.mojang.com")?url+"/authenticate":url+"/authserver/authenticate";
                        String str=getJsonData(json, myUrl, false);
						if(str.equals("")){
							Message msg=new Message();
							msg.obj="未能成功获取登录信息。"+str;
							msg.what=ERROR;
							mHandler.sendMessage(msg);
							return;
						}
                        JSONObject data=new JSONObject(str);
						ArrayMap<String,String> map=new ArrayMap<String,String>();
                        
                        //name
                        map.put("name",data.getJSONObject("selectedProfile").getString("name"));
                        //token
                        map.put("accessToken",data.getString("accessToken"));
                        //uuid
                        map.put("uuid",data.getJSONObject("selectedProfile").getString("id"));
                        
						Message msg=new Message();
						msg.obj=map;
						msg.what=SUCCESS;
						mHandler.sendMessage(msg);
						
                    } catch (Exception e) {
                        Message msg=new Message();
						msg.obj=e.toString();
						msg.what=ERROR;
						mHandler.sendMessage(msg);
                    }
                }
            }).start();

    }
	public void checkOrRefresh(final String accessToken) {
		listener.onStart();
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
						ArrayMap<String,String> map=new ArrayMap<String,String>();
                        JSONObject json=new JSONObject();
                        json.put("accessToken", accessToken);
                        json.put("clientToken", "mio_launcher");
                        String myUrl=url.contains("authserver.mojang.com")?url+"/validate":url+"/authserver/validate";
                        if (getJsonData(json, myUrl, true).equals("204")) {
                            //accessToken可用
							map.put("accessToken",accessToken);
							
                        } else {
                            String myUrl2=url.contains("authserver.mojang.com")?url+"/refresh":url+"/authserver/refresh";
                            String str=getJsonData(json, myUrl2, false);
							if(str.equals("")){
								Message msg=new Message();
								msg.obj="未能成功获取登录信息。"+str;
								msg.what=ERROR;
								mHandler.sendMessage(msg);
								return;
							}
                            JSONObject data=new JSONObject(str);
                            map.put("accessToken",data.getString("accessToken"));
                        }
						Message msg=new Message();
						msg.what = SUCCESS;
						msg.obj=map;
						mHandler.sendMessage(msg);

                    } catch (JSONException e) {
                        Message msg=new Message();
						msg.obj=e.toString();
						msg.what=ERROR;
						mHandler.sendMessage(msg);
                    }
                }
            }).start();
    }
	
    private String getJsonData(JSONObject jsonParam, String urls, boolean nodata) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			SSLContext sc=SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());

			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();        
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write(data);
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());
			if (nodata) {
				return conn.getResponseCode() + "";
			}
            // 请求返回的状态
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return sb.toString();

    }

    
	private class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
    public static void setResourcePack(String packName) {
        try {
            BufferedReader bfr=new BufferedReader(new FileReader(MioUtils.getStoragePath() + "/boat/gamedir/options.txt"));
            String temp=null;
            String options="";
            while ((temp = bfr.readLine()) != null) {
                if (temp.indexOf("resourcePacks") != -1 && temp.indexOf("incompatibleResourcePacks") == -1) {
                    temp = "resourcePacks:[\"" + packName + "\"]";
                }
                if (temp.indexOf("incompatibleResourcePacks") != -1) {
                    temp = "incompatibleResourcePacks:[\"" + packName + "\"]";
                }
                options += temp;
                options += "\n";
            }
            System.out.println(options);
            BufferedWriter bfw=new BufferedWriter(new FileWriter(MioUtils.getStoragePath() + "/boat/gamedir/options.txt"));
            bfw.write(options);
            bfw.flush();
            bfw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

