package com.mio.launcher;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class MioUtils 
{
    public static boolean moveFile(String src, String dest) {
        File fs=new File(src);
        if (!fs.exists()) {
            return false;
        }
		new File(dest).getParentFile().mkdirs();
        if (fs.isDirectory()) {
            File fss=new File(dest, fs.getName());
            fss.mkdir();
            for (File f:fs.listFiles()) {
                moveFile(f.getAbsolutePath(), dest + fss.getName());
            }
        } else {
            fs.renameTo(new File(dest, fs.getName()));
            return true;
        }
        fs.renameTo(new File(dest, fs.getName()));
        return true;
	}
    public static boolean writeTxt(String str,String filePath){
        try {
            BufferedWriter bfw=new BufferedWriter(new FileWriter(filePath));
            bfw.write(str);
            bfw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static String readTxt(String filePath){
        String str="";
        try {
            BufferedReader bfr=new BufferedReader(new FileReader(filePath));
            String temp;
            while((temp=bfr.readLine())!=null){
                str+=temp;
                str+="\n";
            }
            bfr.close();
        } catch (IOException e) {
            str+= "错误："+e.toString();
        }
        return str;
    }
	//static String path="";
    public static String getStoragePath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
        
    }
    public static String getExternalFilesDir(Context context){
        return context.getExternalFilesDir(null).getParentFile().getAbsolutePath();
    }
    public static void copyFromAssets(Context context, String source,
                                      String destination) throws IOException {
        /**
         * 获取assets目录下文件的输入流
         *
         * 1、获取AssetsManager : 调用 Context 上下文对象的 context.getAssets() 即可获取 AssetsManager对象;
         * 2、获取输入流 : 调用 AssetsManager 的 open(String fileName) 即可获取对应文件名的输入流;
         */
        InputStream is = context.getAssets().open(source);
        // 获取文件大小
        int size = is.available();
        // 创建文件的缓冲区
        byte[] buffer = new byte[size];
        // 将文件读取到缓冲区中
        is.read(buffer);
        // 关闭输入流
        is.close();
        /*
         *  打开app安装目录文件的输出流
         *  Context.MODE_PRIVATE  设置该文件只能被本应用使用，为私有数据
         */
        FileOutputStream output = new FileOutputStream(destination);
        // 将文件从缓冲区中写出到内存中
        output.write(buffer);
        //关闭输出流
        output.close();
    }
    public static boolean deleteFile(String src) {
        File fs=new File(src);
        if (!fs.exists()) {
            return false;
        }
        if (fs.isDirectory()) {
            for (File f:fs.listFiles()) {
                deleteFile(f.getAbsolutePath());
            }
        } else {
            fs.delete();
            return true;
        }
        fs.delete();
        return true;
	}
	public static void deleteFolder(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFolder(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
    public static boolean dirCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        for (File s : src.listFiles()) {
            if (s.isFile()) {
                fileCopy(s.getPath(), destPath + File.separator + s.getName());
            } else {
                dirCopy(s.getPath(), destPath + File.separator + s.getName());
            }
        }
        return true;
    }

    public static boolean fileCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(src));
            OutputStream out =new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[2*1024];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                out.write(flush, 0, len);
            }
            out.flush();
            out.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	public static void hideBottomMenu(Activity ac,boolean all) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = ac.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = ac.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
        if (all && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = ac.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            ac.getWindow().setAttributes(lp);
        }
    }
	public static boolean isNetworkConnected(Context context){
        //判断上下文是不是空的
        if (context!=null){
            //获取连接管理器
            ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态mConnectivityManager.getActiveNetworkInfo();
            NetworkInfo mNnetNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if (mNnetNetworkInfo!=null){
                //判断网络是否可用//如果可以用就是 true  不可用就是false
                return mNnetNetworkInfo.isAvailable();
            }
        }
        return false;
    }
	//获取可用运存大小
	public static String getAvailMemory(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(context, memoryInfo.availMem);
    }
//获取总运存大小
	public static String getTotalMemory(Context context){
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        }catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }
	public static String getCpuName() {
        String readLine;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            do {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return null;
                }
            } while (!readLine.contains("Hardware"));
            return readLine.split(":")[1];
        } catch (IOException e) {
        }
		return null;
	}
	public static boolean checkUsername(String username){
        if (username == null){
            return  false;
        }
        return  username.matches("^[a-zA-Z]\\w&{3,19}&");
    }
    public static boolean checkPwd(String pwd){
        if (pwd == null){
            return  false;
        }
        return  pwd.matches("^[0-9]{3,20}&");
    }
    public static String parseLibNameToPath(String libName){
        String[] tmp=libName.split(":");
        return tmp[0].replace(".","/")+"/"+tmp[1]+"/"+tmp[2]+"/"+tmp[1]+"-"+tmp[2]+".jar";
    }

    /**
     * 复制assets目录下所有文件及文件夹到指定路径
     * @param  mActivity 上下文
     * @param  mAssetsPath Assets目录的相对路径
     * @param  mSavePath 复制文件的保存路径
     * @return void
     */
    public static boolean copyAssetsFiles(android.app.Activity mActivity,java.lang.String mAssetsPath,java.lang.String mSavePath) {
        try {
            // 获取assets目录下的所有文件及目录名
            java.lang.String[] fileNames=mActivity.getResources().getAssets().list(mAssetsPath);
            if(fileNames.length>0) {
                // 若是目录
                for(java.lang.String fileName:fileNames) {
                    java.lang.String newAssetsPath="";
                    // 确保Assets路径后面没有斜杠分隔符，否则将获取不到值
                    if((mAssetsPath==null)||"".equals(mAssetsPath)||"/".equals(mAssetsPath)) {
                        newAssetsPath=fileName;
                    }
                    else {
                        if(mAssetsPath.endsWith("/")) {
                            newAssetsPath=mAssetsPath+fileName;
                        }
                        else {
                            newAssetsPath=mAssetsPath+"/"+fileName;
                        }
                    }
                    // 递归调用
                    copyAssetsFiles(mActivity,newAssetsPath,mSavePath+"/"+fileName);
                }
            }
            else {
                // 若是文件
                java.io.File file=new java.io.File(mSavePath);
                // 若文件夹不存在，则递归创建父目录
                file.getParentFile().mkdirs();
                java.io.InputStream is=mActivity.getResources().getAssets().open(mAssetsPath);
                java.io.FileOutputStream fos=new java.io.FileOutputStream(new java.io.File(mSavePath));
                byte[] buffer=new byte[1024];
                int byteCount=0;
                // 循环从输入流读取字节
                while((byteCount=is.read(buffer))!=-1) {
                    // 将读取的输入流写入到输出流
                    fos.write(buffer,0,byteCount);
                }
                // 刷新缓冲区
                fos.flush();
                fos.close();
                is.close();
            }
        }
        catch(java.lang.Exception e) {
            Log.e("assets复制错误",e.toString());
            return false;
        }
        return true;
    }
    public static String getFileSha1(String path) {

        try {
            File file=new File(path);
            FileInputStream in = new FileInputStream(file);
            MessageDigest messagedigest;
            messagedigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) >0) {
//该对象通过使用 update()方法处理数据
                messagedigest.update(buffer, 0, len);
            }
//对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
            return bytesToHex(messagedigest.digest()).toLowerCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 字节数组转Hex
     * @param bytes 字节数组
     * @return Hex
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < bytes.length; i++) {
                String hex = byteToHex(bytes[i]);
                sb.append(hex);
            }
        }
        return sb.toString();
    }
    /**
     * Byte字节转Hex
     * @param b 字节
     * @return Hex
     */
    public static String byteToHex(byte b) {
        String hexString = Integer.toHexString(b & 0xFF);
        //由于十六进制是由0~9、A~F来表示1~16，所以如果Byte转换成Hex后如果是<16,就会是一个字符（比如A=10），通常是使用两个字符来表示16进制位的,
        //假如一个字符的话，遇到字符串11，这到底是1个字节，还是1和1两个字节，容易混淆，如果是补0，那么1和1补充后就是0101，11就表示纯粹的11
        if (hexString.length() < 2) {
            hexString = new StringBuilder(String.valueOf(0)).append(hexString).toString();
        }
        return hexString.toUpperCase();
    }

    
}
