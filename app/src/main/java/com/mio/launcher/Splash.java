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
import android.os.*;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import cosine.boat.LauncherConfig;

/**
 * @author mio
**/
public class Splash extends Activity {
	private Handler handler;
	TextView waitText;
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MioUtils.hideBottomMenu(this, true);
		setContentView(R.layout.splash);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {//安卓>=11
			if (Environment.isExternalStorageManager()) {//有权限
				fileCheck();
			}else {//没权限
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
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {//有权限
				fileCheck();
			}else {//没权限
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
	private void fileCheck(){
		handler = new Handler();
		new Thread(() -> {
			waitText = findViewById(R.id.wait_text_info);
			MioInfo.initializeMioInfo(Splash.this);
			File runtime = new File(MioInfo.DIR_DATA,"app_runtime");
			File config1 = new File(MioInfo.defaultMioLauncherDir_Public,"MioConfig.json");
			File busybox = new File(runtime,"busybox");
			if ((!new File(MioInfo.jre8Dir).exists()  || !new File(MioInfo.runtimeDir + "/version").exists())){
				Splash.this.runOnUiThread(() -> waitText.setText("正在补全必要文件请耐心等待"));
				MioUtils.copyFilesFromAssets(Splash.this,"app_runtime",runtime.getAbsolutePath());
				busybox.setExecutable(true);
				MioUtils.DeleteFolder(MioInfo.defaultGameDir_Public);
				MioUtils.copyFilesFromAssets(Splash.this,".minecraft",MioInfo.defaultGameDir_Public);
				Splash.this.runOnUiThread(() -> waitText.setText("安装完毕"));
			}if(!new File(MioInfo.defaultMioLauncherDir_Public + "/MioConfig.json").exists()){
				MioUtils.copyFilesFromAssets(Splash.this,"gamedir.json",new File(MioInfo.DIR_GAMEDIR_JSON).getAbsolutePath());
				MioUtils.copyFilesFromAssets(Splash.this,"MioConfig.json",config1.getAbsolutePath());
				MioUtils.copyFilesFromAssets(Splash.this,"launcher_profiles.json",new File(MioInfo.defaultGameDir_Public,"launcher_profiles.json").getAbsolutePath());
			}
			MioInfo.config = LauncherConfig.fromFile(config1.getAbsolutePath());
			handler.post(runnableUi);
		}).start();
	}
	Runnable runnableUi = new Runnable(){
		@Override
		public void run() {
			//在这里写更新UI的操作
			startActivity(new Intent(Splash.this,MioLauncher.class));
			finish();
		}
	};
	private void toast(String s){
		runOnUiThread(()-> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		MioUtils.hideBottomMenu(this, true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){//安卓 >= 11
			if(Environment.isExternalStorageManager()){
				//用户同意[即有权限]，执行操作
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
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 233) {
			if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
				toast("读写权限获取成功");
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

