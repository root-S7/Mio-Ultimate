package com.mio.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Space;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cosine.boat.LauncherConfig;

/**
 * @author mio
 */
public class Splash extends Activity {
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MioUtils.hideBottomMenu(this, true);
		setContentView(R.layout.splash);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			if (Environment.isExternalStorageManager()) {
				init();
				fileCheck();
			}else {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.setData(Uri.parse("package:" + getPackageName()));
				/**
				 * 打开系统设置的对应页面给应用授予权限
				 * 授予权限后会调用onActivityResult方法进行判断
				**/
				startActivityForResult(intent, 233);
			}
		}else{//安卓<11
			String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				init();
				fileCheck();
			}else {
				AlertDialog dialog=new AlertDialog.Builder(this).setTitle("警告").setMessage("检测到应用无读写权限，请点击申请。").setPositiveButton("申请", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestPermissions(permissions, 233);
					}
				}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.R)
	public void requestPermission() {
		int permission_read = ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE);
		if(permission_read != PackageManager.PERMISSION_GRANTED) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
			intent.setData(Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, 233);
		}
	}

	public void init(){
		new File(MioInfo.DIR_VERSIONS).mkdirs();
		new File(MioInfo.DIR_LIBRARIES).mkdirs();
		new File(MioInfo.DIR_INDEXES).mkdirs();
		new File(MioInfo.DIR_OBJECTS).mkdirs();
		new File(MioInfo.DIR_TEMP,"version_manifest.json");
		try {
			new File(MioInfo.DIR_MAIN,".nomedia").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void fileCheck(){
		new Thread(()->{
			File runtime= new File(MioInfo.DIR_DATA,"app_runtime");
			File config1= new File(MioInfo.DIR_MAIN,"MioConfig.json");
			File profile= new File(MioInfo.DIR_GAME,"launcher_profiles.json");
			File busybox=new File(runtime,"busybox");
			File gamedir=new File(MioInfo.DIR_GAMEDIR_JSON);
//			File caciacavallo=new File(MioInfo.DIR_DATA,"caciacavallo");
			if (!runtime.exists()){
				toast("正在安装运行库。");
				MioUtils.copyAssetsFiles(Splash.this,"app_runtime",runtime.getAbsolutePath());
				toast("安装完毕。");
			}
			if(!busybox.exists()){
				MioUtils.copyAssetsFiles(Splash.this,"app_runtime/busybox",busybox.getAbsolutePath());
				busybox.setExecutable(true);
			}
			if (!gamedir.exists()){
				try {
					MioUtils.copyFromAssets(Splash.this,gamedir.getName(),gamedir.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!config1.exists()){
				try {
					MioUtils.copyFromAssets(Splash.this,config1.getName(),config1.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!profile.exists()){
				try {
					MioUtils.copyFromAssets(Splash.this,profile.getName(),profile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			MioInfo.config=LauncherConfig.fromFile(config1.getAbsolutePath());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(()->{
				startActivity(new Intent(Splash.this,MioLauncher.class));
				finish();
			});
		}).start();
	}
	private void toast(String s){
		runOnUiThread(()-> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
	}
	//安卓11开始特殊权限申请引入的回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		MioUtils.hideBottomMenu(this, true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){//安卓 >= 11
			if(Environment.isExternalStorageManager()){
				//用户同意[即有权限]，执行操作
				init();
				fileCheck();
			}else{
				//用户不同意，向用户展示该权限作用
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setCancelable(false);
				alert.setMessage("请授予读写权限，否则应用无法正常运行");
				alert.setPositiveButton("授予权限", (dialog1, which) -> requestPermission());
				alert.setNegativeButton("关闭应用", (dialog2, which) -> System.exit(0));
				final AlertDialog dialog = alert.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		}
	}
	//安卓6.0开始一般权限申请引入的回调
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 233) {
			if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
				toast("读写权限获取成功");
				init();
				fileCheck();
			}else{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setCancelable(false);
				alert.setMessage("读写权限获取失败,请允许本应用获取权限");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					alert.setPositiveButton("授予权限", (dialog1, which) -> requestPermissions(permissions, 233));
				}
				alert.setNegativeButton("关闭应用", (dialog2, which) -> System.exit(0));
				final AlertDialog dialog = alert.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		}
	}
}

