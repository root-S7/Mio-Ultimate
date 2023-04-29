package com.mio.launcher;

import android.content.Context;
import android.os.Environment;
import cosine.boat.LauncherConfig;

public class MioInfo {
    public static LauncherConfig config;
    //应用MioLauncher路径(公有目录)
    public static String defaultMioLauncherDir_Public = Environment.getExternalStorageDirectory() + "/MioLauncher";
    //默认游戏资源文件存放路径(公有目录)
    public static String defaultGameDir_Public = defaultMioLauncherDir_Public + "/.minecraft";
    public static String DIR_TEMP = defaultMioLauncherDir_Public + "/temp";
    public static String DIR_ASSETS = defaultGameDir_Public + "/assets";
    public static String DIR_OBJECTS = DIR_ASSETS + "/objects";
    public static String DIR_INDEXES = DIR_ASSETS+ "/indexes";
    public static String DIR_VERSIONS = defaultGameDir_Public + "/versions";
    public static String DIR_LIBRARIES = defaultGameDir_Public + "/libraries";
    public static String DIR_GAMEDIR_JSON = defaultMioLauncherDir_Public + "/gamedir.json";
    public static String DIR_DATA;
    //运行库解压后所在目录
    public static String runtimeDir;
    //cache目录位置
    public static String cacheDir;
    //JRE8目录
    public static String jre8Dir;
    public static void initializeMioInfo (Context context){
        MioUtils.createDirectory(defaultGameDir_Public);
        DIR_DATA = context.getFilesDir().getAbsolutePath().replace("/files","");
        runtimeDir = context.getDir("runtime",Context.MODE_PRIVATE).getAbsolutePath();
        jre8Dir = runtimeDir + "/j2re-image";
        cacheDir = context.getCacheDir().getAbsolutePath();
    }
    public static void setPath(String s){
        defaultMioLauncherDir_Public = Environment.getExternalStorageDirectory() + "/MioLauncher";
        defaultGameDir_Public = defaultMioLauncherDir_Public + "/.minecraft";
        DIR_TEMP = defaultMioLauncherDir_Public + "/temp";
        DIR_ASSETS = defaultGameDir_Public + "/assets";
        DIR_OBJECTS = DIR_ASSETS + "/objects";
        DIR_INDEXES = DIR_ASSETS+ "/indexes";
        DIR_VERSIONS = defaultGameDir_Public + "/versions";
        DIR_LIBRARIES = defaultGameDir_Public + "/libraries";
    }
}
