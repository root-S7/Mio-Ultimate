package com.mio.launcher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.mio.mclauncher.mcdownload.ForgeDownload;
import com.mio.mclauncher.mcdownload.MioMcFile;
import com.mio.mclauncher.mcdownload.MioMcVersion;
import com.mio.mclauncher.mcdownload.OptifineDownload;
import com.mio.mclauncher.mcdownload.UrlSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityDownload extends AppCompatActivity {
	private TextView download_text_version;
	private ListView download_list_versions;
	private RadioGroup download_radio_group;
	private String url_version_manifest,
			url_libraries,
			url_version_json,
			url_version_jar,
			url_assets_index,
			url_assets_objs;
	private int versionType=MioMcVersion.RELEASE;
	private ArrayAdapter adapter;
	private MioMcVersion mioMcVersion;
	private MioMcFile mioMcFile;
	private List<String> listVersions;
	private TextView textFile,textProgress,textTotalProgress,textSpeed,textInfo;
	private ProgressBar progressBar,totalProgressBar;
	private AlertDialog mDialog;
	private String fabricLink="https://maven.fabricmc.net/net/fabricmc/fabric-installer/0.7.4/fabric-installer-0.7.4.jar";

	private ProgressDialog autodownloadProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_download_official);
//		FileDownloader.setup(this);
		download_text_version=findViewById(R.id.ac_download_official_version);
		download_list_versions=findViewById(R.id.ac_download_officialListView);
		download_radio_group=findViewById(R.id.ac_download_officialRadioGroup);
		List<String> mList=new ArrayList<>();
		mList.add("官方");
		mList.add("MCBBS");
		mList.add("BMCL");
		ListPopupWindow menu=new ListPopupWindow(this);
		menu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mList));
		menu.setDropDownGravity(Gravity.LEFT);
		menu.setAnchorView(download_text_version);
		menu.setModal(true);
		menu.setOnItemClickListener((parent, view, position, id) -> {
			download_text_version.setText(mList.get(position));
			setUrl(position);
			getVersionManifest();
			menu.dismiss();
		});

		download_text_version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.show();
			}
		});
		download_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.ac_download_officialRadioButton_release:
						versionType = MioMcVersion.RELEASE;
						break;
					case R.id.ac_download_officialRadioButton_snapshot:
						versionType = MioMcVersion.SNAPSHOT;
						break;
					case R.id.ac_download_officialRadioButton_old:
						versionType = MioMcVersion.BETA;
						break;
				}
				refreshAdapter();
			}
		});
		setUrl(0);
		getVersionManifest();

		try{
			Bundle data=getIntent().getExtras();
			if (!data.getString("version").equals("")){
				autodownloadProgressDialog=new ProgressDialog(this);
				autodownloadProgressDialog.setTitle("正在加载所需文件");
				autodownloadProgressDialog.setCanceledOnTouchOutside(false);
				autodownloadProgressDialog.show();
			}
		}catch (Exception e){

		}
	}
	public boolean checkApplication(String packageName) {

		if (packageName == null || "".equals(packageName)) {

			return false;

		}

		try {

			ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}

	}
	private ForgeDownload forgeDownload;
	private OptifineDownload optifineDownload;
	private void initAdapter(){
		if (mioMcVersion==null){
			mioMcVersion=new MioMcVersion();
			mioMcFile=new MioMcFile();
			listVersions=new ArrayList<String>();
		}else {
			listVersions.clear();
		}
		mioMcVersion.init(new File(MioInfo.DIR_TEMP,"version_manifest.json").getAbsolutePath());
		listVersions.addAll(mioMcVersion.getVersionsID(versionType));
		if (adapter==null){
			adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listVersions);
			download_list_versions.setAdapter(adapter);
			download_list_versions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					AlertDialog dialog=new AlertDialog.Builder(ActivityDownload.this)
							.setMessage("是否要下载版本："+listVersions.get(position)+"?")
							.setPositiveButton("是", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									doDownload(listVersions.get(position));
								}
							})
							.setNegativeButton("否", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							}).create();
					dialog.show();
				}
			});
			download_list_versions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					String[] items=new String[]{"Forge安装","高清修复安装","Fabric安装"};
					AlertDialog dialog = new AlertDialog.Builder(ActivityDownload.this)
							.setItems(items, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dia, int which) {
									switch (which){
										case 0:
											installForge(position);
											toast("正在获取文件列表...");
											break;
										case 1:
											installOptiFine(position);
											toast("正在获取文件列表...");
											break;
										case 2:
											installFabric();
											break;
									}
								}
							})
							.setNegativeButton("取消", null)
							.create();
					dialog.show();
					return false;
				}
			});
		}else {
			refreshAdapter();
		}
	}
	//    https://meta.fabricmc.net/v2/versions/installer
	private void installFabric(){
		initDialog();
		textInfo.setText("正在下载");
		FileDownloader.getImpl().create(fabricLink)
				.setPath(new File(MioInfo.DIR_TEMP).getAbsolutePath(),true)
				.setForceReDownload(false)
				.setListener(new FileDownloadListener() {
					@Override
					protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

					}

					@Override
					protected void started(BaseDownloadTask task) {
						textFile.setText(task.getFilename());
					}

					@Override
					protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
						long progress=soFarBytes * 100 / totalBytes;
						progressBar.setProgress((int)progress);
						textProgress.setText(progress + "%");
						totalProgressBar.setProgress((int)progress);
						textTotalProgress.setText(progress + "%");
					}

					@Override
					protected void blockComplete(BaseDownloadTask task) {
					}

					@Override
					protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
					}

					@Override
					protected void completed(BaseDownloadTask task) {
						mDialog.dismiss();
						openInstaller(new File(MioInfo.DIR_TEMP,task.getFilename()).getAbsolutePath());
					}

					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						toast("错误："+e.toString());
					}

					@Override
					protected void warn(BaseDownloadTask task) {
					}
				}).start();
	}

	private void openInstaller(String modFile){
		if (!checkApplication("com.mio.mclauncher")){
			toast("未安装插件：澪-Installer，无法启动安装器。");
			return;
		}
		ComponentName componentName = new ComponentName(
				"com.mio.mclauncher",   //要去启动的App的包名
				"com.mio.mclauncher.MioActivity");
		//要去启动的App中的Activity的类名
		// ComponentName : 参数说明
		//组件名称，第一个参数是包名，也是主配置文件Manifest里设置好的包名
		//第二个是类名，要带上包名

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		intent.setComponent(componentName);
		bundle.putString("modFile", modFile);
		intent.putExtra("modFile", bundle);
		startActivity(intent);

	}
	private void installForge(int position){
		new Thread(()->{
			forgeDownload=new ForgeDownload(listVersions.get(position));
			List<String> a=new ArrayList<>();
			a.addAll(forgeDownload.getForgeVersions());
			String[] ss=new String[a.size()];
			for (int i=0;i<a.size();i++){
				ss[i]=a.get(a.size()-1-i);
			}
			runOnUiThread(()->{
				String[] items=ss;
				AlertDialog dialog = new AlertDialog.Builder(ActivityDownload.this)
						.setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dia, int which) {
								AlertDialog dialog = new AlertDialog.Builder(ActivityDownload.this)
										.setMessage("已选中："+ss[which]+"，是否下载？")
										.setPositiveButton("是", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which00000) {
												new Thread(()->{
													String link=forgeDownload.getDownloadLink(ss[which]);
													runOnUiThread(()->{
														initDialog();
														textInfo.setText("正在下载");
														FileDownloader.getImpl().create(link)
																.setPath(new File(MioInfo.DIR_TEMP).getAbsolutePath(),true)
																.setForceReDownload(false)
																.setListener(new FileDownloadListener() {
																	@Override
																	protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void started(BaseDownloadTask task) {
																		textFile.setText(task.getFilename());
																	}

																	@Override
																	protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																		long progress=soFarBytes * 100 / totalBytes;
																		progressBar.setProgress((int)progress);
																		textProgress.setText(progress + "%");
																		totalProgressBar.setProgress((int)progress);
																		textTotalProgress.setText(progress + "%");
																	}

																	@Override
																	protected void blockComplete(BaseDownloadTask task) {
																	}

																	@Override
																	protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
																	}

																	@Override
																	protected void completed(BaseDownloadTask task) {
																		mDialog.dismiss();
																		openInstaller(new File(MioInfo.DIR_TEMP,task.getFilename()).getAbsolutePath());

																	}

																	@Override
																	protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void error(BaseDownloadTask task, Throwable e) {
																		toast("错误："+e.toString());
																	}

																	@Override
																	protected void warn(BaseDownloadTask task) {
																	}
																}).start();
													});
												}).start();
											}
										})
										.setNegativeButton("取消", null)
										.create();
								dialog.show();
							}
						})
						.setNegativeButton("取消", null)
						.create();
				dialog.show();
			});
		}).start();
	}
	private void installOptiFine(int position){
		new Thread(()->{
			optifineDownload=new OptifineDownload(listVersions.get(position));
			List<String> a=new ArrayList<>();
			a.addAll(optifineDownload.getVersion());
			String[] ss=new String[a.size()];
			for (int i=0;i<a.size();i++){
				ss[i]=a.get(a.size()-1-i);
			}
			runOnUiThread(()->{
				String[] items=ss;
				AlertDialog dialog = new AlertDialog.Builder(ActivityDownload.this)
						.setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dia, int which) {
								AlertDialog dialog = new AlertDialog.Builder(ActivityDownload.this)
										.setMessage("已选中："+ss[which]+"，是否下载？")
										.setPositiveButton("是", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which00000) {
												new Thread(()->{
													String link=optifineDownload.getDownloadLink(ss[which]);
													runOnUiThread(()->{
														initDialog();
														textInfo.setText("正在下载");
														FileDownloader.getImpl().create(link)
																.setPath(new File(MioInfo.DIR_TEMP).getAbsolutePath(),true)
																.setForceReDownload(false)
																.setListener(new FileDownloadListener() {
																	@Override
																	protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void started(BaseDownloadTask task) {
																		textFile.setText(task.getFilename());
																	}

																	@Override
																	protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																		long progress=soFarBytes * 100 / totalBytes;
																		progressBar.setProgress((int)progress);
																		textProgress.setText(progress + "%");
																		totalProgressBar.setProgress((int)progress);
																		textTotalProgress.setText(progress + "%");
																	}

																	@Override
																	protected void blockComplete(BaseDownloadTask task) {
																	}

																	@Override
																	protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
																	}

																	@Override
																	protected void completed(BaseDownloadTask task) {
																		mDialog.dismiss();
																		openInstaller(new File(MioInfo.DIR_TEMP,task.getFilename()).getAbsolutePath());
																	}

																	@Override
																	protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
																	}

																	@Override
																	protected void error(BaseDownloadTask task, Throwable e) {
																		toast("错误："+e.toString());
																	}

																	@Override
																	protected void warn(BaseDownloadTask task) {
																	}
																}).start();
													});
												}).start();
											}
										})
										.setNegativeButton("取消", null)
										.create();
								dialog.show();
							}
						})
						.setNegativeButton("取消", null)
						.create();
				dialog.show();
			});
		}).start();
	}
	private void refreshAdapter(){
		listVersions.clear();
		listVersions.addAll(mioMcVersion.getVersionsID(versionType));
		adapter.notifyDataSetChanged();
	}
	private void getVersionManifest() {
		FileDownloader.getImpl().create(url_version_manifest)
				.setPath(new File(MioInfo.DIR_TEMP,"version_manifest.json").getAbsolutePath())
				.setForceReDownload(false)
				.setAutoRetryTimes(2)
				.setListener(new FileDownloadListener() {
					@Override
					protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void started(BaseDownloadTask task) {
						toast("正在获取版本列表...");
					}

					@Override
					protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void blockComplete(BaseDownloadTask task) {
					}

					@Override
					protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
					}

					@Override
					protected void completed(BaseDownloadTask task) {
						initAdapter();
						try{
							Bundle data=getIntent().getExtras();
							if (!data.getString("version").equals("")){
								autodownloadProgressDialog.dismiss();
								doDownload(data.getString("version"));
							}
						}catch (Exception e){

						}
					}

					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						toast("重试");
					}

					@Override
					protected void warn(BaseDownloadTask task) {
					}
				}).start();
	}
	private void doDownload(final String version) {
		View v=LayoutInflater.from(ActivityDownload.this).inflate(R.layout.alert_download_progress, null);
		textFile = v.findViewById(R.id.alert_downloadTextViewFile);
		textProgress = v.findViewById(R.id.alert_downloadTextViewProgress);
		progressBar = v.findViewById(R.id.alert_downloadProgressBar);
		textTotalProgress=v.findViewById(R.id.alert_downloadTextViewTotalProgress);
		textSpeed=v.findViewById(R.id.alert_downloadTextViewSpeed);
		totalProgressBar=v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
		textInfo=v.findViewById(R.id.alert_download_info);
		mDialog = new AlertDialog.Builder(ActivityDownload.this)
				.setView(v)
				.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2) {
						mDialog.dismiss();
					}
				})
				.create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				FileDownloader.getImpl().pauseAll();
				FileDownloader.getImpl().clearAllTaskData();
			}
		});
		downloadVersionJson(version);
	}
	private void initDialog(){
		View v=LayoutInflater.from(ActivityDownload.this).inflate(R.layout.alert_download_progress, null);
		textFile = v.findViewById(R.id.alert_downloadTextViewFile);
		textProgress = v.findViewById(R.id.alert_downloadTextViewProgress);
		progressBar = v.findViewById(R.id.alert_downloadProgressBar);
		textTotalProgress=v.findViewById(R.id.alert_downloadTextViewTotalProgress);
		textSpeed=v.findViewById(R.id.alert_downloadTextViewSpeed);
		totalProgressBar=v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
		textInfo=v.findViewById(R.id.alert_download_info);
		mDialog = new AlertDialog.Builder(ActivityDownload.this)
				.setView(v)
				.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2) {
						FileDownloader.getImpl().pauseAll();
						mDialog.dismiss();
					}
				})
				.create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				FileDownloader.getImpl().pauseAll();
				FileDownloader.getImpl().clearAllTaskData();
			}
		});
	}
	private File versionJsonPath;
	private void downloadVersionJson(String version) {
		textInfo.setText("正在下载版本文件");
		versionJsonPath = new File(MioInfo.DIR_VERSIONS, version + "/" + version + ".json");
		FileDownloader.getImpl().create(mioMcVersion.getURL(version, versionType).replace(UrlSource.OFFICIAL_VERSION_JSON,url_version_json).replace(UrlSource.OFFICIAL_VERSION_JSON.replace("https","http"),url_version_json))
				.setPath(versionJsonPath.getAbsolutePath())
				.setAutoRetryTimes(5)
				.setListener(new FileDownloadListener() {
					@Override
					protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void started(BaseDownloadTask task) {
						textFile.setText(task.getFilename());
					}

					@Override
					protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
						long progress=soFarBytes * 100 / totalBytes;
						progressBar.setProgress((int)progress);
						textProgress.setText(progress + "%");
					}

					@Override
					protected void blockComplete(BaseDownloadTask task) {
					}

					@Override
					protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
					}

					@Override
					protected void completed(BaseDownloadTask task) {
						downloadLibraries(versionJsonPath.getAbsolutePath());
					}

					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						toast("错误："+e.toString());
					}

					@Override
					protected void warn(BaseDownloadTask task) {
					}
				}).start();

	}
	private int fileCount=0,fileCurrentCount=0;
	private void downloadLibraries(String versionJsonPath) {
		textInfo.setText("正在下载依赖库");
		fileCurrentCount=0;
		FileDownloadListener queueTarget= new FileDownloadListener() {
			@Override
			protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
			}

			@Override
			protected void started(BaseDownloadTask task) {
				textFile.setText(task.getFilename());
			}

			@Override
			protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
			}

			@Override
			protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				long progress=soFarBytes * 100 / totalBytes;
				progressBar.setProgress((int)progress);
				textProgress.setText(progress + "%");
				textSpeed.setText(task.getSpeed()+"kb/s");

			}

			@Override
			protected void blockComplete(BaseDownloadTask task) {

			}

			@Override
			protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
				//toast("重试");
			}

			@Override
			protected void completed(BaseDownloadTask task) {
				fileCurrentCount++;
				totalProgressBar.setProgress(fileCurrentCount);
				textTotalProgress.setText(fileCurrentCount*100/fileCount+"%");
				if(fileCurrentCount==totalProgressBar.getMax()){
					downloadClient();
				}
			}

			@Override
			protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				//toast("暂停");
			}

			@Override
			protected void error(BaseDownloadTask task, Throwable e) {
				toast("错误："+e.toString());
			}

			@Override
			protected void warn(BaseDownloadTask task) {
				//toast("警告");
			}
		};
		final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
		final List<BaseDownloadTask> tasks = new ArrayList<>();
		mioMcFile.init(versionJsonPath);
		Set<String> keySet=mioMcFile.getLibraries().keySet();
		for(String key:keySet){
			tasks.add(FileDownloader.getImpl().create(mioMcFile.getLibraries().get(key).replace(UrlSource.OFFICIAL_LIBRARIES,url_libraries).replace(UrlSource.OFFICIAL_LIBRARIES.replace("https","http"),url_libraries)).setTag(key).setPath(new File(MioInfo.DIR_LIBRARIES,key).getAbsolutePath()).setAutoRetryTimes(5));
		}
		fileCount=keySet.size();
		totalProgressBar.setMax(fileCount);
		queueSet.setAutoRetryTimes(5);
//        queueSet.downloadSequentially(tasks);
		queueSet.downloadTogether(tasks);
		queueSet.start();

	}
	private void downloadClient() {
		textInfo.setText("正在下载核心");
		FileDownloader.getImpl().create(mioMcFile.getClient().replace(UrlSource.OFFICIAL_VERSION_JAR,url_version_jar).replace(UrlSource.OFFICIAL_VERSION_JAR.replace("https","http"),url_version_jar))
				.setPath(new File(versionJsonPath.getParent(),versionJsonPath.getParentFile().getName()+".jar").getAbsolutePath())
				.setAutoRetryTimes(5)
				.setListener(new FileDownloadListener() {
					@Override
					protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void started(BaseDownloadTask task) {
						textFile.setText(task.getFilename());
					}

					@Override
					protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
						long progress=soFarBytes * 100 / totalBytes;
						progressBar.setProgress((int)progress);
						textProgress.setText(progress + "%");
						textSpeed.setText(task.getSpeed()+"kb/s");
						textTotalProgress.setText(progress+"%");
					}

					@Override
					protected void blockComplete(BaseDownloadTask task) {
					}

					@Override
					protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
					}

					@Override
					protected void completed(BaseDownloadTask task) {
						downloadAssetsIndex();
					}

					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						toast("错误："+e.toString());
					}

					@Override
					protected void warn(BaseDownloadTask task) {
					}
				}).start();
	}
	private void downloadAssetsIndex() {
		textInfo.setText("正在下载资源索引");
		FileDownloader.getImpl().create(mioMcFile.getAssetsIndex().replace(UrlSource.OFFICIAL_ASSETS_INDEX_JSON,url_assets_index).replace(UrlSource.OFFICIAL_ASSETS_INDEX_JSON.replace("https","http"),url_assets_index))
				.setPath(new File(MioInfo.DIR_INDEXES).getAbsolutePath(),true)
				.setAutoRetryTimes(5)
				.setListener(new FileDownloadListener() {
					@Override
					protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void started(BaseDownloadTask task) {
						textFile.setText(task.getFilename());
					}

					@Override
					protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
						long progress=soFarBytes * 100 / totalBytes;
						progressBar.setProgress((int)progress);
						textProgress.setText(progress + "%");
					}

					@Override
					protected void blockComplete(BaseDownloadTask task) {
					}

					@Override
					protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
					}

					@Override
					protected void completed(BaseDownloadTask task) {
						downloadAssets(new File(new File(MioInfo.DIR_INDEXES).getAbsolutePath(),mioMcFile.getId()+".json").getAbsolutePath());

					}

					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						toast("错误："+e.toString());
					}

					@Override
					protected void warn(BaseDownloadTask task) {
					}
				}).start();
	}
	private void downloadAssets(String path){
		fileCurrentCount=0;
		textInfo.setText("正在下载游戏文件");
		FileDownloadListener queueTarget= new FileDownloadListener() {
			@Override
			protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
			}

			@Override
			protected void started(BaseDownloadTask task) {
				textFile.setText(task.getFilename());
			}

			@Override
			protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
			}

			@Override
			protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				long progress=soFarBytes * 100 / totalBytes;
				progressBar.setProgress((int)progress);
				textProgress.setText(progress + "%");
				textSpeed.setText(task.getSpeed()+"kb/s");
			}

			@Override
			protected void blockComplete(BaseDownloadTask task) {

			}

			@Override
			protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
//                toast("重试");
			}

			@Override
			protected void completed(BaseDownloadTask task) {
				fileCurrentCount++;
				totalProgressBar.setProgress(fileCurrentCount);
				textTotalProgress.setText(fileCurrentCount*100/fileCount+"%");
				if(fileCurrentCount==totalProgressBar.getMax()){
					mDialog.dismiss();
					Toast.makeText(ActivityDownload.this, "下载完毕。", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				//toast("暂停");
			}

			@Override
			protected void error(BaseDownloadTask task, Throwable e) {
				toast("错误："+e.toString());
			}

			@Override
			protected void warn(BaseDownloadTask task) {
				//toast("警告");
			}
		};
		final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
		final List<BaseDownloadTask> tasks = new ArrayList<>();
		Map<String,String> map=mioMcFile.getAssets(path);
		Set<String> keySet=map.keySet();
		for(String key:keySet){
			tasks.add(FileDownloader.getImpl().create(url_assets_objs+"/"+map.get(key)).setTag(key).setPath(new File(new File(MioInfo.DIR_OBJECTS).getAbsolutePath(),key).getAbsolutePath()).setAutoRetryTimes(5));
		}
		fileCount=keySet.size();
		totalProgressBar.setMax(fileCount);
		queueSet.setAutoRetryTimes(5);
//        queueSet.downloadSequentially(tasks);
		queueSet.downloadTogether(tasks);
		queueSet.start();


	}
	private void setUrl(int i) {
		switch (i) {
			case UrlSource.OFFICIAL:
				url_version_manifest = UrlSource.OFFICIAL_VERSION_MANIFEST;
				url_version_json = UrlSource.OFFICIAL_VERSION_JSON;
				url_version_jar = UrlSource.OFFICIAL_VERSION_JAR;
				url_libraries = UrlSource.OFFICIAL_LIBRARIES;
				url_assets_index = UrlSource.OFFICIAL_ASSETS_INDEX_JSON;
				url_assets_objs = UrlSource.OFFICIAL_ASSETS_OBJS;
				break;
			case UrlSource.MCBBS:
				url_version_manifest = UrlSource.MCBBS_VERSION_MANIFEST;
				url_version_json = UrlSource.MCBBS_VERSION_JSON;
				url_version_jar = UrlSource.MCBBS_VERSION_JAR;
				url_libraries = UrlSource.MCBBS_LIBRARIES;
				url_assets_index = UrlSource.MCBBS_ASSETS_INDEX_JSON;
				url_assets_objs = UrlSource.MCBBS_ASSETS_OBJS;
				break;
			case UrlSource.BMCL:
				url_version_manifest = UrlSource.BMCLAPI_VERSION_MANIFEST;
				url_version_json = UrlSource.BMCLAPI_VERSION_JSON;
				url_version_jar = UrlSource.BMCLAPI_VERSION_JAR;
				url_libraries = UrlSource.BMCLAPI_LIBRARIES;
				url_assets_index = UrlSource.BMCLAPI_ASSETS_INDEX_JSON;
				url_assets_objs = UrlSource.BMCLAPI_ASSETS_OBJS;
				break;
		}
	}
	private void toast(String s){
		try {
			Toast.makeText(this,s,Toast.LENGTH_LONG).show();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		FileDownloader.getImpl().pauseAll();
		FileDownloader.getImpl().clearAllTaskData();
	}
}