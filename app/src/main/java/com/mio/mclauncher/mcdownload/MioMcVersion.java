package com.mio.mclauncher.mcdownload;
import com.google.gson.Gson;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MioMcVersion 
{
	public static final int RELEASE=0,SNAPSHOT=1,BETA=2;
	private Map<String,String> versions_release;
	private Map<String,String> versions_snapshot;
	private Map<String,String> versions_beta;
	private List<String> list_releaseID;
	private List<String> list_snapshotID;
	private List<String> list_betaID;
	public void init(String filePath){
		try {
			FileReader reader=new FileReader(filePath);
			VersionManifestJson mVersionManifestJson=new Gson().fromJson(reader, VersionManifestJson.class);
			reader.close();
			VersionManifestJson.Version[] versions=mVersionManifestJson.getVersions();
			versions_release=new HashMap<String,String>();
			versions_snapshot=new HashMap<String,String>();
			versions_beta=new HashMap<String,String>();
			list_releaseID=new ArrayList<String>();
			list_snapshotID=new ArrayList<String>();
			list_betaID=new ArrayList<String>();
			for(VersionManifestJson.Version version:versions){
				if(version.getType().equals("release")){
					versions_release.put(version.getId(),version.getUrl());
					list_releaseID.add(version.getId());
				}else if(version.getType().equals("snapshot")){
					versions_snapshot.put(version.getId(),version.getUrl());
					list_snapshotID.add(version.getId());
				}else if(version.getType().equals("old_beta")||version.getType().equals("old_alpha")){
					versions_beta.put(version.getId(),version.getUrl());
					list_betaID.add(version.getId());
				}
			}
			for(String str:list_releaseID){
				System.out.println(str);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public List<String> getVersionsID(int type){
		switch(type){
			case RELEASE:
				return list_releaseID;

			case SNAPSHOT:
				return list_snapshotID;

			case BETA:
				return list_betaID;
		}
		return null;
	}
	public String getURL(String id,int type){
		switch(type){
			case RELEASE:
				return versions_release.get(id);

			case SNAPSHOT:
				return versions_snapshot.get(id);

			case BETA:
				return versions_beta.get(id);
		}
		return null;
	}
}
