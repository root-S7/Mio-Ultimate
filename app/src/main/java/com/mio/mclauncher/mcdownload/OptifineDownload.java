package com.mio.mclauncher.mcdownload;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OptifineDownload {
    private String mcversion;
    private ArrayList<String> version;
    public OptifineDownload(String mcversion){
        this.mcversion=mcversion;
        init();
    }
//    https://bmclapi2.bangbang93.com/optifine/:mcversion/:type/:patch
    private void init(){
        try {
            version=new ArrayList<>();
            JSONArray temp=new JSONArray(ForgeDownload.doGet("https://bmclapi2.bangbang93.com/optifine/" + mcversion));
            for(int i=0;i<temp.length();i++){
                version.add(mcversion+"/"+temp.getJSONObject(i).getString("type")+"/"+temp.getJSONObject(i).getString("patch"));
            }
        } catch (JSONException e) {
            System.out.println("error:"+e);
        }
    }
    public ArrayList<String> getVersion(){
        return version;
    }
    public String getDownloadLink(String v){
        return ForgeDownload.doGet("https://bmclapi2.bangbang93.com/optifine/"+v);
    }

}
