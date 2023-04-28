package com.mio.launcher;

import android.os.Environment;
import java.io.File;
import cosine.boat.LauncherConfig;

public class MioInfo {
    public static LauncherConfig config;
    public static String DIR_MAIN = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "MioLauncher").getAbsolutePath();
    public static String DIR_GAME = new File(DIR_MAIN, ".minecraft").getAbsolutePath();
    public static String DIR_TEMP = new File(DIR_GAME,"temp").getAbsolutePath();
    public static String DIR_ASSETS = new File(DIR_GAME,"assets").getAbsolutePath();
    public static String DIR_OBJECTS = new File(DIR_ASSETS,"objects").getAbsolutePath();
    public static String DIR_INDEXES = new File(DIR_ASSETS,"indexes").getAbsolutePath();
    public static String DIR_VERSIONS = new File(DIR_GAME,"versions").getAbsolutePath();
    public static String DIR_LIBRARIES = new File(DIR_GAME,"libraries").getAbsolutePath();
    public static String DIR_GAMEDIR_JSON = new File(DIR_MAIN,"gamedir.json").getAbsolutePath();
    public static String DIR_DATA="/data/data/com.mio.launcher";
    public static void setPath(String s){
        DIR_MAIN = new File(s,"MioLauncher").getAbsolutePath();
        DIR_GAME = new File(DIR_MAIN, ".minecraft").getAbsolutePath();
        DIR_TEMP = new File(DIR_GAME,"temp").getAbsolutePath();
        DIR_ASSETS = new File(DIR_GAME,"assets").getAbsolutePath();
        DIR_OBJECTS = new File(DIR_ASSETS,"objects").getAbsolutePath();
        DIR_INDEXES = new File(DIR_ASSETS,"indexes").getAbsolutePath();
        DIR_VERSIONS = new File(DIR_GAME,"versions").getAbsolutePath();
        DIR_LIBRARIES = new File(DIR_GAME,"libraries").getAbsolutePath();
    }
}
