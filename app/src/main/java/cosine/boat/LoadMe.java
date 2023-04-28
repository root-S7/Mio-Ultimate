package cosine.boat;

import android.util.ArrayMap;
import android.util.Log;

import com.mio.launcher.MioInfo;
import com.mio.launcher.MioUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Vector;

public class LoadMe {
    public final static String TAG = "LoadMe";

    public static native int chdir(String str);

    public static native int jliLaunch(String[] strArr);

    public static native void redirectStdio(String file);

    public static native void setenv(String str, String str2);

    public static native void setupJLI();

    public static native int dlopen(String name);

    public static native void setLibraryPath(String path);

    public static native void patchLinker();

    public static WeakReference<LogReceiver> mReceiver;

    public static String render="gl4es_1.1.4.so";

    static {
        System.loadLibrary("boat");
    }

    public static void receiveLog(String str) {
        if (mReceiver == null) {
            Log.e(TAG, "LogReceiver is null. So use default receiver.");
            mReceiver = new WeakReference<LogReceiver>(new LogReceiver() {
                final StringBuilder builder = new StringBuilder();

                @Override
                public void pushLog(String log) {
                    Log.e(TAG, log);
                    builder.append(log);
                }

                @Override
                public String getLogs() {
                    return builder.toString();
                }
            });
        } else {
            mReceiver.get().pushLog(str);
        }
    }

    public static int exec(LauncherConfig config) {
        try {

            MinecraftVersion mcVersion = MinecraftVersion.fromDirectory(new File(config.get("currentVersion")));

            String runtimePath = "/data/data/com.mio.launcher/app_runtime";
            getAllLib(new File(runtimePath));

            String home = config.get("currentVersion");


            setenv("HOME", home);
            setenv("JAVA_HOME", runtimePath +"/j2re-image");
            //修复渲染
            setenv("LIBGL_MIPMAP", "3");
            setenv("LIBGL_NORMALIZE", "1");
            setenv("LIBGL_GL","21");
            //添加版本判断
            boolean isLwjgl3 = false;
            if (mcVersion.minimumLauncherVersion >= 21) {
                isLwjgl3 = true;
            }
            // openjdk
            dlopen(findLib("libfreetype.so"));
            dlopen(findLib("libjli.so"));
            dlopen(findLib("libjvm.so"));
            dlopen(findLib("libverify.so"));
            dlopen(findLib("libjava.so"));
            dlopen(findLib("libnet.so"));
            dlopen(findLib("libnio.so"));
            dlopen(findLib("libawt.so"));
            dlopen(findLib("libawt_headless.so"));
            dlopen(findLib("libtinyiconv.so"));
            dlopen(findLib("libinstrument.so"));

            String classPath;
            String libraryPath;
            dlopen(runtimePath + "/libopenal.so.1");
            dlopen(runtimePath + "/"+render);
//			dlopen(findLib("/libgl4es_115.so");
            //根据版本判断是否启用lwjgl3
            if (!isLwjgl3) {
                libraryPath = runtimePath + "/j2re-image/lib/server:"+runtimePath + "/j2re-image/lib:"+runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-2:"+runtimePath;

                classPath = runtimePath + "/lwjgl-2/lwjgl.jar:" + runtimePath + "/lwjgl-2/lwjgl_util.jar:" + mcVersion.getClassPath(config);

                dlopen(runtimePath + "/lwjgl-2/liblwjgl.so");
            } else {
                patchLinker();
                libraryPath = runtimePath + "/j2re-image/lib/server:"+runtimePath + "/j2re-image/lib:"+ runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-3:"+runtimePath;

                classPath = runtimePath + "/lwjgl-3/lwjgl-jemalloc.jar:" + runtimePath + "/lwjgl-3/lwjgl-tinyfd.jar:" + runtimePath + "/lwjgl-3/lwjgl-opengl.jar:" + runtimePath + "/lwjgl-3/lwjgl-openal.jar:" + runtimePath + "/lwjgl-3/lwjgl-glfw.jar:" + runtimePath + "/lwjgl-3/lwjgl-stb.jar:" + runtimePath + "/lwjgl-3/lwjgl.jar:" +  mcVersion.getClassPath(config);
                dlopen(runtimePath + "/libglfw.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
            }
            setupJLI();

            redirectStdio(new File(MioInfo.DIR_MAIN,"boat_output.txt").getAbsolutePath());
            chdir(home);


            Vector<String> args = new Vector<String>();


            args.add(runtimePath +"/j2re-image/bin/java");

            String extraJavaFlags[] = config.get("extraJavaFlags").split(" ");
            for (String flag : extraJavaFlags) {
                args.add(flag);
            }
            if (new File(runtimePath +"/j2re-image","8").exists()){
                args.add("-Djava.awt.headless=false");
                args.add("-Dcacio.managed.screensize="+BoatActivity.screenwidth+"x"+BoatActivity.screenheight);
                args.add("-Dcacio.font.fontmanager=sun.awt.X11FontManager");
                args.add("-Dcacio.font.fontscaler=sun.font.FreetypeFontScaler");
                args.add("-Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel");
                args.add("-Dawt.toolkit=net.java.openjdk.cacio.ctc.CTCToolkit");
                args.add("-Djava.awt.graphicsenv=net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");
                args.add("-Xbootclasspath/a:"+runtimePath+"/caciocavallo/cacio-shared-1.10-SNAPSHOT.jar:"+runtimePath+"/caciocavallo/ResConfHack.jar:"+runtimePath+"/caciocavallo/cacio-androidnw-1.10-SNAPSHOT.jar");

            }
            //高版本修复启动forge闪退问题
            if (isLwjgl3) {
                args.add("-Dfml.earlyprogresswindow=false");
            }
            //java参数
            args.add("-Djava.io.tmpdir=/data/data/com.mio.launcher/cache");
            //args.add("-Duser.home=null");
            args.add("-Dminecraft.launcher.brand=MioPro");
            args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
            args.add("-Dfml.ignorePatchDiscrepancies=true");
            args.add("-Djava.home=" + runtimePath +"/j2re-image");
            args.add("-Duser.language=zh");
            args.add("-Duser.country=CN");
            args.add("-Dnet.minecraft.clientmodname=Mio-Ultimate");
            args.add("-Djava.library.path=" + libraryPath);
//			args.add("-Dglfwstub.windowWidth="+((BoatActivity)MyApplication.getCurrentActivity()).screenwidth);
//			args.add("-Dglfwstub.windowHeight="+((BoatActivity)MyApplication.getCurrentActivity()).screenheight);

            args.add("-cp");
            args.add(classPath);

            args.add(mcVersion.mainClass);
            //mc参数
            String minecraftArgs[] = null;
            if (isLwjgl3) {
                minecraftArgs = mcVersion.getMinecraftArguments(config, true);
            } else {
                minecraftArgs = mcVersion.getMinecraftArguments(config, false);
            }

            for (String flag : minecraftArgs) {
                args.add(flag);
            }
            args.add("--width");
            args.add(Integer.toString(BoatInput.width));
            args.add("--height");
            args.add(Integer.toString(BoatInput.height));
            args.add("--fullscreenWidth");
            args.add(Integer.toString(BoatInput.width));
            args.add("--fullscreenHeight");
            args.add(Integer.toString(BoatInput.height));
            args.add("--fullscreen");
            String extraMinecraftArgs[] = config.get("extraMinecraftFlags").split(" ");
            for (String flag : extraMinecraftArgs) {
                if (!flag.equals("")) {
                    args.add(flag);
                }
            }
            String 最终参数 = "";
            String finalArgs[] = new String[args.size()];
            for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    最终参数 += finalArgs[i] + "\n";
                    System.out.println("MC启动参数:" + finalArgs[i]);
                }
            }
            //MioUtils.writeTxt(最终参数,"/sdcard/launcher/参数.txt");

            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

//    public static int openInstaller() {
//        try {
//            String runtimePath = "/data/data/com.mio.launcher/app_runtime";
//            getAllLib(new File(runtimePath));
//
//            setenv("HOME", "/storage/emulated/0/MioLauncher/.minecraft");
//            setenv("JAVA_HOME", runtimePath +"/j2re-image");
//            // openjdk
//            dlopen(findLib("libfreetype.so"));
//            dlopen(findLib("libjli.so"));
//            dlopen(findLib("libjvm.so"));
//            dlopen(findLib("libverify.so"));
//            dlopen(findLib("libjava.so"));
//            dlopen(findLib("libnet.so"));
//            dlopen(findLib("libnio.so"));
//            dlopen(findLib("libawt.so"));
//            dlopen(findLib("libawt_headless.so"));
//            dlopen(findLib("libtinyiconv.so"));
//            dlopen(findLib("libinstrument.so"));
//
//
//            setupJLI();
//
//            redirectStdio("/storage/emulated/0/MioLauncher/.minecraft/boat_output.txt");
//            chdir("/storage/emulated/0/MioLauncher/.minecraft/");
//
//
//            Vector<String> args = new Vector<String>();
//
//
//            args.add(runtimePath +"/j2re-image/bin/java");
//
//            args.add("-cp");
//            args.add("\"/storage/emulated/0/MioLauncher/.minecraft/forge-installer-headless-1.0.1.jar:/storage/emulated/0/MioLauncher/.minecraft/temp/forge-1.16.4-35.1.37-installer.jar\"");
//            args.add("me.xfl03.HeadlessInstaller");
//            args.add("-installClient");
//            args.add("/storage/emulated/0/MioLauncher/.minecraft/");
//
////            args.add("-cp");
////            args.add("\"/storage/emulated/0/MioLauncher/.minecraft/forge-install-bootstrapper.jar:/storage/emulated/0/MioLauncher/.minecraft/temp/forge-1.16.4-35.1.37-installer.jar\"");
////            args.add("com.bangbang93.ForgeInstaller");
////            args.add("/storage/emulated/0/MioLauncher/.minecraft/");
//
//            String 最终参数 = "";
//            String finalArgs[] = new String[args.size()];
//            for (int i = 0; i < args.size(); i++) {
//                if (!args.get(i).equals(" ")) {
//                    finalArgs[i] = args.get(i);
//                    最终参数 += finalArgs[i] + "\n";
//                    System.out.println("启动参数:" + finalArgs[i]);
//                }
//            }
//            MioUtils.writeTxt(最终参数,"/storage/emulated/0/MioLauncher/.minecraft/参数.txt");
//
//            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 1;
//        }
//        return 0;
//    }

    private static Map<String,File> libs;
    private static void getAllLib(File file){
        if(libs==null){
            libs=new ArrayMap<>();
        }
        File[] listFiles = file.listFiles();

        if(listFiles != null && listFiles.length >0){

            for (File file2 : listFiles) {

                if(file2.isDirectory()){
                    getAllLib(new File(file2.getAbsolutePath()));
                }

                if(file2.isFile()){

                    String path = file2.getAbsolutePath();

                    if(path.endsWith(".so")){
                        libs.put(file2.getName(),file2);
                    }
                }
            }
        }
    }
    private static String findLib(String libName){
        try {
            return libs.get(libName).getAbsolutePath();
        }catch (Exception E){
            Log.e("lib查找错误", libName.toString());
        }
        return "";
    }
    public interface LogReceiver {
        void pushLog(String log);

        String getLogs();
    }
}





