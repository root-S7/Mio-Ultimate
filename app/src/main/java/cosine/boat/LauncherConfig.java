package cosine.boat;

import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.UnsupportedEncodingException;
import android.util.Log;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class LauncherConfig extends HashMap<String, String>{
	private static String path;
	public static void toFile(String filePath, LauncherConfig config){
		try{
			//Log.i("Launcher", "Trying to save config to file.");
			Utils.writeFile(filePath, toPrettyFormat(new Gson().toJson(config, LauncherConfig.class)));

		}catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}
	public static LauncherConfig fromFile(String filePath){
		path=filePath;
		try {
			return new Gson().fromJson(new String(Utils.readFile(filePath), "UTF-8"), LauncherConfig.class);

		}catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}
	}
	public String get(String key){
		String value;
		if (super.get(key) == null){
			value = "";
			Log.w("Boat", "Value required can not found: key=" + key);
		}else{
			value = super.get(key);
		}
		return value;
	}
	public void set(String key,String value){
		super.put(key,value);
		toFile(path,this);
	}
	public static String toPrettyFormat(String json) {
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(jsonObject);
	}
}
