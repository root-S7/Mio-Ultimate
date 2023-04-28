package com.mio.mclauncher.mcdownload;
import java.io.FileReader;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.io.File;

public class MioMcFile
{
	private VersionJson mVersionJson;
	private AssetsJson mAssetsJson;
	public void init(String filePath){
		try {
			FileReader reader=new FileReader(filePath);
			mVersionJson=new Gson().fromJson(reader, VersionJson.class);
			reader.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public String getAssetsIndex(){
		return mVersionJson.getAssetIndex().getUrl();
	}
	public String getClient(){
		return mVersionJson.getDownloads().getClient().getUrl();
	}
	public String getId(){
		return mVersionJson.getAssetIndex().getId();
	}
	public String getForgeId(){
		return mVersionJson.getId();
	}
	public Map<String,String> getLibraries(){
		Map<String,String> map=new HashMap<String,String>();
		for(VersionJson.DependentLibrary dep:mVersionJson.getLibraries()){
			if(dep.getDownloads()!=null){
				if(dep.getDownloads().getArtifact()!=null){
					String path=dep.getDownloads().getArtifact().getPath();
					String url=dep.getDownloads().getArtifact().getUrl();
					if(!url.equals("")&&!path.contains("jinput")&&!path.contains("lwjgl")&&!path.contains("platform")){
						map.put(path,url);
						System.out.println("path:"+path);
						System.out.println("url:"+url);
						System.out.println();
					}

				}
			}else{
				String path=dep.getName();
				String url="http://files.minecraftforge.net/maven/";
				if(!path.contains("net/minecraftforge/forge")&&!path.contains("platform")){
					String[] tmp=path.split(":");
					path=tmp[0].replace(".","/")+"/"+tmp[1]+"/"+tmp[2]+"/"+tmp[1]+"-"+tmp[2]+".jar";
					url=url+path;
					map.put(path,url);
					System.out.println("path:"+path);
					System.out.println("url:"+url);
					System.out.println();
				}

			}

		}
		return map;
	}
	public Map<String,String> getAssets(String filePath){
		Map<String,String> map=new HashMap<String,String>();
		if(mAssetsJson==null){
			try {
				FileReader reader=new FileReader(filePath);
				mAssetsJson=new Gson().fromJson(reader, AssetsJson.class);
				reader.close();
			} catch (Exception e) {
				return null;
			}
		}
		HashMap<String, AssetsJson.MinecraftAssetInfo> objects=mAssetsJson.getObjects();
		Set<String> keySet=objects.keySet();
		for(String key:keySet){
			AssetsJson.MinecraftAssetInfo info=objects.get(key);
			String path=info.getHash().substring(0,2);
			String url=path+"/"+info.getHash();
			map.put(url,url);
			System.out.println("path:"+url);
			System.out.println("url:"+url);
			System.out.println();
		}
		return map;
	}
}
