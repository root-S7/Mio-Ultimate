package com.mio.mclauncher.mcdownload;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;

public class ForgeDownload
{
    private String mcversion;
    private ArrayList<String> forgeVersion;
    private HashMap<String,String> forgeList;
    public ForgeDownload(String mcversion){
        this.mcversion=mcversion;
        initForgeList(mcversion);
    }

    private void initForgeList(String mcversion){
        this.mcversion=mcversion;
        forgeVersion=new ArrayList<String>();
        forgeList=new HashMap<String,String>();
        try {
            JSONArray temp=new JSONArray(doGet("https://bmclapi2.bangbang93.com/forge/minecraft/" + mcversion));
            for(int i=0;i<temp.length();i++){
                forgeVersion.add(temp.getJSONObject(i).getString("version"));
                forgeList.put(temp.getJSONObject(i).getString("version"),temp.getJSONObject(i).getString("build"));
                System.out.println(temp.getJSONObject(i).getString("version"));
            }
        } catch (JSONException e) {
            System.out.println("error:"+e);
        }
    }
    public String getDownloadLink(String forgeVersion){
        return "https://bmclapi2.bangbang93.com/forge/download/"+forgeList.get(forgeVersion);
    }
    public static String parseLibNameToPath(String libName){
        String[] tmp=libName.split(":");
        return tmp[0].replace(".","/")+"/"+tmp[1]+"/"+tmp[2]+"/"+tmp[1]+"-"+tmp[2]+".jar";
    }
    public ArrayList<String> getForgeVersions(){
        return forgeVersion;
    }
    public static String doGet(String url) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }else if(connection.getResponseCode()==302){
                Log.d("错误",302+"");
                return connection.getHeaderField("Location");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            assert connection != null;
            connection.disconnect();
        }
        return result;
    }
}
