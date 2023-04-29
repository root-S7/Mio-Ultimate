package com.mio.launcher;

import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import com.google.gson.Gson;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.liulishuo.filedownloader.*;
import com.mio.launcher.adapter.ListGame;
import com.mio.launcher.adapter.ListUser;
import com.mio.mclauncher.mcdownload.AssetsJson;
import com.mio.mclauncher.mcdownload.UrlSource;
import com.wingsofts.guaguale.WaveLoadingView;
import com.xw.repo.BubbleSeekBar;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;
import cosine.boat.*;

public class MioLauncher extends AppCompatActivity implements OnClickListener {
    int version = 1;
    //数据储存
    SharedPreferences msh;
    SharedPreferences.Editor mshe;
    public static boolean fullScreen;
    //主界面
    ConstraintLayout background;
    TextView tip;
    ScrollView tip_container;
    //toolbar区域
    Toolbar main_toolbar;
    Button toolbar_button_home, toolbar_button_exit;
    TextView toolbar_text_state, toolbar_text_info;
    //开始游戏
    LinearLayout startGame;
    Button startGame_choose;
    TextView versiontext;
    //左侧功能区域
    Button left_btn_user, left_btn_version, left_btn_gamelist, left_btn_plugin, left_btn_setting, left_btn_about, left_btn_log;
    LinearLayout layout_user, layout_version, layout_gamelist, layout_gamedir, layout_setting, layout_about, layout_log;
    List<LinearLayout> list_layouts;
    //用户区域
    LinearLayout layout_user_adduser;
    ListView layout_user_listview;
    ListUser adaptwr_user;
    List<UserBean> userList;
    //游戏管理区域
    LinearLayout layout_version_modctrl, layout_version_packctrl, layout_version_refresh, layout_version_reset;
    EditText layout_version_editJvm;
    Button layout_version_save;
    //游戏列表区域
    LinearLayout layout_gamelist_addgame, layout_gamelist_refresh, layout_gamelist_move;
    ListView layout_gamelist_listview;
    ListGame adapter_game;
    //游戏插件区域
    ImageView layout_plugin_install_installer;
    TextView layout_plugin_text_state_installer;
    LinearLayout layout_plugin_open_installer;
    //日志区域
    LinearLayout layout_log_share;
    LinearLayout layout_log_refresh;
    EditText layout_log_edit;
    //启动器设置区域
    Button layout_settingButtonMouse, layout_settingButtonBackground, layout_settingButtonFix, layout_settingButtonHelp, layout_settingButtonToCmd, layout_settingButtonChooseGif, layout_settingButtonChangeFbl;
    CheckBox layout_settingCheckBoxFull,layout_settingCheckBoxRender,layout_settingCheckBoxCaci;
    ImageView layout_settingImageViewDeleteRuntime;
    //关于
    Button layout_about_donate,layout_about_futureplan;
    TextView layout_about_donationlist;
    //其他
    PopupWindow popupWindow;
    //主题效果
    //初始化加载动画
    WaveLoadingView wave;
    private boolean flag_first = true;
    private TextView textFile, textProgress, textTotalProgress, textSpeed, textInfo;
    private ProgressBar progressBar, totalProgressBar;
    private AlertDialog mDialog;
    private int fileCount = 0, fileCurrentCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MioUtils.hideBottomMenu(this, true);
        setContentView(R.layout.activity_main);
        //初始化数据储存
        msh = getSharedPreferences("Mio", MODE_PRIVATE);
        mshe = msh.edit();
        //初始化UI
        background = findViewById(R.id.activity_mainRelativeLayout);
        if (new File(MioInfo.defaultMioLauncherDir_Public, "bg.png").exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(MioInfo.defaultMioLauncherDir_Public, "bg.png").getAbsolutePath());
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            background.setBackground(bd);
        }
        initUI();
        initOthers();
        initLoadingView();
        if (msh.getBoolean("Ultimate", true)) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告").setMessage("如果您是首次使用澪-Ultimate请先观看教程，新版本有一些操作变化").setPositiveButton("观看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://b23.tv/EVcXx9");
                    intent.setData(content_url);
                    Toast.makeText(MioLauncher.this, "澪Ultimate发布的视频就是教程。", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            mshe.putBoolean("Ultimate", false);
            mshe.commit();
        }
//        new Thread(()->{
//            try {
//                CurseAPIMinecraft.initialize();
////                Log.e("URL",CurseAPI.fileDownloadURL(316059, 2839369).get().toString());
////                CurseAPI.downloadFileToDirectory(316059, 2839369, Paths.get(MioInfo.DIR_TEMP));
////                CurseModpack modpack= CurseModpack.fromJSON(Paths.get("/storage/emulated/0/MioLauncher/.minecraft/temp/manifest.json"));
////                CurseFiles<CurseFile> curseFiles= modpack.files();
////
////                for (CurseFile curseFile:curseFiles){
////                    CurseAPI.downloadFileToDirectory(curseFile.projectID(), curseFile.id(), Paths.get(MioInfo.DIR_TEMP));
////                    Log.e("文件下载完成",""+curseFile.id());
////                }
////                Log.e("全部下载完成","完成");
//                //mc id 432
////                for(CurseGameVersion version:CurseAPI.gameVersions(432).get()){
////                    Log.e("mc",version.versionString());
////                }
//                for(CurseCategory category:CurseAPI.game(432).get().categories()){
//                    Log.e("mc",category.toString());
//                }
//                Log.e("mc",CurseAPI.game(432).get().toString());
//                CurseAPI.searchProjects()
//            } catch (CurseException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MioUtils.hideBottomMenu(this, true);
        if (flag_first) {
            flag_first = false;
        }
    }
    private void initOthers() {
        tip = findViewById(R.id.home_text_content);
        tip_container = findViewById(R.id.tip_container);
        if (MioUtils.isNetworkConnected(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str = "";
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL("https://icraft.ren:90/titles/newMio.txt").openConnection();
                        con.setConnectTimeout(10000);
                        InputStream in = con.getInputStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        String temp = null;

                        while ((temp = bfr.readLine()) != null) {
                            str += temp + "\n";
                        }
                        bfr.close();
                        in.close();
                        con.disconnect();

                    } catch (IOException e) {
                        str += e.toString();
                    }
                    final String finalStr = str;
                    runOnUiThread(() -> {
                        tip.setText(finalStr);
                    });
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL("https://icraft.ren:90/titles/zz.txt").openConnection();
                        con.setConnectTimeout(10000);
                        InputStream in = con.getInputStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        String temp = null;
                        String str = "";
                        while ((temp = bfr.readLine()) != null) {
                            str += temp + "\n";
                        }
                        bfr.close();
                        in.close();
                        con.disconnect();
                        final String result = str;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout_about_donationlist.setText(result);
                            }
                        });
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout_about_donationlist.setText("错误:" + e.toString());
                            }
                        });
                    }
                }
            }).start();
        }
    }
    private void initUI() {
        //toolbar区域
        main_toolbar = findViewById(R.id.main_toolbar);
        main_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(main_toolbar);
        toolbar_button_home = findButton(R.id.toolbar_button_home);
        toolbar_button_exit = findButton(R.id.toolbar_button_exit);
        toolbar_text_state = findViewById(R.id.main_text_state);
        toolbar_text_info = findViewById(R.id.main_text_info);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String str = Build.BRAND + " " + Build.MODEL + " Android " + Build.VERSION.RELEASE + " " + MioUtils.getCpuName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar_text_info.setText(str);
                    }
                });
            }
        }).start();
        //启动游戏
        startGame = findViewById(R.id.layout_startgame);
        startGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View p1) {
                final File splash = new File(MioInfo.config.get("currentVersion"), "config/splash.properties");
                try {
                    splash.getParentFile().mkdirs();
                    splash.createNewFile();
                }catch (IOException e) {
                }
                try{
                    FileInputStream in = new FileInputStream(splash);
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    in.close();
                    String ss = new String(b);
                    if (!ss.contains("enabled=false")) {
                        AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this).setTitle("警告").setMessage("检测到未禁用加载动画，游戏可能无法启动，是否禁用?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                try {
                                    InputStream in = getAssets().open("splash.properties");
                                    byte[] b = new byte[in.available()];
                                    in.read(b);
                                    in.close();
                                    FileOutputStream out = new FileOutputStream(splash);
                                    out.write(b);
                                    out.flush();
                                    out.close();
                                    gameStart();
                                } catch (IOException e) {
                                    toast(e.toString());
                                }
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                        gameStart();
                                    }
                        }).create();
                        dialog.show();
                    }else {
                        gameStart();
                    }
                } catch (Exception e) {
                }
            }
        });
        versiontext = findViewById(R.id.activity_main_versiontext);
        startGame_choose = findViewById(R.id.activity_main_choose_version);
        List<String> gamelist = new ArrayList<>();
        String[] versions = new File(MioInfo.DIR_VERSIONS).list();
        if (versions != null && versions.length != 0) {
            for (String s : versions) {
                gamelist.add(s);
            }
        }
        adapter_game = new ListGame(this, gamelist);
        startGame_choose.setOnClickListener((v) -> {
            ListPopupWindow menu = new ListPopupWindow(this);
            menu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapter_game.getList()));
            menu.setDropDownGravity(Gravity.LEFT);
            menu.setAnchorView(startGame);
            menu.setModal(true);
            menu.setOnItemClickListener((parent, view, position, id) -> {
                versiontext.setText(adapter_game.getList().get(position));
                MioInfo.config.set("currentVersion", new File(MioInfo.DIR_VERSIONS, adapter_game.getList().get(position)).getAbsolutePath());
                menu.dismiss();
            });
            menu.show();
        });
        if (MioInfo.config == null) {
            MioInfo.config = LauncherConfig.fromFile(new File(MioInfo.defaultMioLauncherDir_Public, "MioConfig.json").getAbsolutePath());
        }
        if (!new File(MioInfo.config.get("currentVersion")).exists()) {
            versiontext.setText("未选择版本");
        } else {
            versiontext.setText(new File(MioInfo.config.get("currentVersion")).getName());
        }
        //左侧功能区域
        left_btn_user = findButton(R.id.main_button_user);
        left_btn_version = findButton(R.id.main_button_version);
        left_btn_gamelist = findButton(R.id.main_button_gamelist);
        left_btn_plugin = findButton(R.id.main_button_plugin);
        left_btn_setting = findButton(R.id.main_button_setting);
        left_btn_about = findButton(R.id.main_button_about);
        left_btn_log = findButton(R.id.main_button_log);
        layout_user = findViewById(R.id.layout_user);
        layout_version = findViewById(R.id.layout_version);
        layout_gamelist = findViewById(R.id.layout_gamelist);
        layout_gamedir = findViewById(R.id.layout_gamedir);
        layout_setting = findViewById(R.id.layout_setting);
        layout_about = findViewById(R.id.layout_about);
        layout_log = findViewById(R.id.layout_log);
        list_layouts = new ArrayList<LinearLayout>();
        list_layouts.add(layout_user);
        list_layouts.add(layout_version);
        list_layouts.add(layout_gamelist);
        list_layouts.add(layout_gamedir);
        list_layouts.add(layout_setting);
        list_layouts.add(layout_about);
        list_layouts.add(layout_log);
        for (View layout : list_layouts) {
            layout.setVisibility(View.INVISIBLE);
        }
        //用户区域
        layout_user_adduser = findViewById(R.id.layout_user_adduser);
        layout_user_adduser.setOnClickListener(this);
        layout_user_listview = findViewById(R.id.layout_user_listview);
        parseJsonToUser();
        adaptwr_user = new ListUser(this, userList);
        layout_user_listview.setAdapter(adaptwr_user);
        //游戏管理区域
        layout_version_modctrl = findViewById(R.id.layout_version_modctrl);
        layout_version_modctrl.setOnClickListener(this);
        layout_version_packctrl = findViewById(R.id.layout_version_packctrl);
        layout_version_packctrl.setOnClickListener(this);
        layout_version_editJvm = findViewById(R.id.layout_version_editJvm);
        layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
        layout_version_save = findViewById(R.id.layout_version_save);
        layout_version_save.setOnClickListener(this);
        layout_version_refresh = findViewById(R.id.layout_version_refresh);
        layout_version_refresh.setOnClickListener(this);
        layout_version_reset = findViewById(R.id.layout_version_reset);
        layout_version_reset.setOnClickListener(this);
        //游戏列表区域
        layout_gamelist_addgame = findViewById(R.id.layout_gamelist_addgame);
        layout_gamelist_addgame.setOnClickListener(this);
        layout_gamelist_refresh = findViewById(R.id.layout_gamelist_refresh);
        layout_gamelist_refresh.setOnClickListener(this);
        layout_gamelist_move = findViewById(R.id.layout_gamelist_move);
        layout_gamelist_move.setOnClickListener(this);
        layout_gamelist_listview = findViewById(R.id.layout_gamelist_listview);
        layout_gamelist_listview.setAdapter(adapter_game);
        //游戏插件区域
        layout_plugin_install_installer = findViewById(R.id.layout_plugin_install_installer);
        layout_plugin_install_installer.setOnClickListener(this);
        layout_plugin_text_state_installer = findViewById(R.id.layout_plugin_text_state_installer);
        if (checkApplication("com.mio.mclauncher")) {
            layout_plugin_text_state_installer.setText("已安装");
        }
        layout_plugin_open_installer = findViewById(R.id.layout_plugin_open_installer);
        layout_plugin_open_installer.setOnClickListener(this);
        //日志区域
        layout_log_share = findViewById(R.id.layout_log_share);
        layout_log_share.setOnClickListener(this);
        layout_log_refresh = findViewById(R.id.layout_log_refresh);
        layout_log_refresh.setOnClickListener(this);
        layout_log_edit = findViewById(R.id.layout_log_edit);
        new Thread(() -> {
            String s = MioUtils.readTxt(new File(MioInfo.defaultMioLauncherDir_Public, "boat_output.txt").getAbsolutePath());
            runOnUiThread(() -> layout_log_edit.setText(s.contains("错误") ? "无日志" : s));
        }).start();
        //启动器设置区域
        layout_settingButtonMouse = findButton(R.id.layout_settingButtonMouse);
        layout_settingButtonBackground = findButton(R.id.layout_settingButtonBackground);
        layout_settingButtonFix = findButton(R.id.layout_settingButtonFix);
        layout_settingButtonHelp = findButton(R.id.layout_settingButtonHelp);
        layout_settingButtonChangeFbl = findButton(R.id.layout_settingButtonChangeFbl);
        layout_settingCheckBoxFull = findViewById(R.id.layout_settingCheckBoxFull);
        layout_settingCheckBoxFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                mshe.putBoolean("刘海", p2);
                mshe.commit();
                fullScreen = p2;
                System.out.println("我被改成了" + fullScreen);
            }
        });
        layout_settingCheckBoxRender = findViewById(R.id.layout_settingCheckBoxRender);
        layout_settingCheckBoxRender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                if (p2) {
                    mshe.putString("渲染器", "gl4es_1.1.5.so");
                    LoadMe.render = "gl4es_1.1.5.so";
                } else {
                    mshe.putString("渲染器", "gl4es_1.1.4.so");
                    LoadMe.render = "gl4es_1.1.4.so";
                }
                mshe.commit();
            }
        });
        LoadMe.render = msh.getString("渲染器", "gl4es_1.1.4.so");
        layout_settingCheckBoxRender.setChecked(msh.getString("渲染器", "gl4es_1.1.4.so").equals("gl4es_1.1.5.so"));
        layout_settingCheckBoxCaci = findViewById(R.id.layout_settingCheckBoxCaci);
        layout_settingCheckBoxCaci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        new File(MioInfo.jre8Dir, "8").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    new File(MioInfo.jre8Dir, "8").delete();
                }
            }
        });
        layout_settingCheckBoxCaci.setChecked(new File(MioInfo.jre8Dir, "8").exists());
        layout_settingButtonToCmd = findViewById(R.id.layout_settingButtonToCmd);
        layout_settingButtonToCmd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View p1) {
                startActivity(new Intent(MioLauncher.this, MainActivity.class));
            }
        });
        layout_settingButtonChooseGif = findViewById(R.id.layout_settingButtonChooseGif);
        layout_settingButtonChooseGif.setOnClickListener(this);
        layout_settingImageViewDeleteRuntime = findViewById(R.id.layout_settingImageViewDeleteRuntime);

        layout_settingImageViewDeleteRuntime.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View p1) {
                AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this)
                        .setTitle("提示")
                        .setMessage("是否确定删除已安装的运行库？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                MioUtils.DeleteFolder(new File(MioLauncher.this.getApplicationContext().getFilesDir().getParent(), "/app_runtime").getAbsolutePath());
                                toast("已删除运行库，将进入导入界面，请再次导入运行库。");
//								startActivity(new Intent(MioLauncher.this, Activity_Download.class));
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setNeutralButton("删除运行库及其缓存文件", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                MioUtils.DeleteFolder(new File(MioLauncher.this.getApplicationContext().getFilesDir().getParent(), "/app_runtime").getAbsolutePath());
                                MioUtils.DeleteFolder(new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/runtime/mioruntimev4.zip").getAbsolutePath());
                                toast("已删除运行库及其缓存文件，将进入导入界面，请再次导入运行库。");
//								startActivity(new Intent(MioLauncher.this, Activity_Download.class));
                                finish();
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });
        //关于
        layout_about_donate = findButton(R.id.layout_about_donate);
        layout_about_futureplan = findButton(R.id.layout_about_futureplan);
        layout_about_donationlist = findViewById(R.id.layout_about_donationlist);
    }
    private void gameStart() {
        if (!versiontext.getText().toString().equals("未选择版本")) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在检查游戏文件完整性...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            boolean 禁用检测 = new File(MioInfo.config.get("currentVersion"), "禁用检测").exists();
            boolean 完全禁用检测 = new File(MioInfo.config.get("currentVersion"), "完全禁用检测").exists();
            new Thread(() -> {
                List<String> libs = new ArrayList<>();
                List<String> objects = new ArrayList<>();
                if (!完全禁用检测) {
                    MinecraftVersion mv = MinecraftVersion.fromDirectory(new File(MioInfo.config.get("currentVersion")));
                    if (mv == null) {
                        runOnUiThread(() -> {
                            toast("当前所选版本文件已损坏，请删除后重新下载");
                        });
                        return;
                    }
                    if (mv.inheritsFrom != null) {
                        if (!new File(MioInfo.DIR_VERSIONS, mv.inheritsFrom).exists()) {
                            runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告").setMessage("缺少原版文件：" + mv.inheritsFrom + "，是否下载？(只能下载官方版本)").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioLauncher.this, ActivityDownload.class);
                                        Bundle b = new Bundle();
                                        b.putString("version", mv.inheritsFrom);
                                        i.putExtras(b);//传递Bundle打包的数据
                                        startActivity(i);//启动第二个activity并把i传递过去
                                    }
                                }).setNegativeButton("取消", null).create();
                                dialog.show();
                            });
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    for (String lib : mv.getLibraries()) {
                        if (!禁用检测) {
                            String sha1 = MioUtils.getFileSha1(new File(MioInfo.DIR_LIBRARIES, lib).getAbsolutePath());
                            if (sha1 != null) {
                                if (!sha1.equals(mv.getSHA1(lib))) {
                                    if (mv.getSHA1(lib) != null) {
                                        new File(MioInfo.DIR_LIBRARIES, lib).delete();
                                    }
                                }
                            }
                        }
                        if (!new File(MioInfo.DIR_LIBRARIES, lib).exists()) {
                            libs.add(lib);
                        }
                    }
                    if (mv.assets != null) {
                        if (new File(MioInfo.DIR_INDEXES, mv.assets + ".json").exists()) {
                            for (String obj : getAssets(new File(MioInfo.DIR_INDEXES, mv.assets + ".json").getAbsolutePath())) {
                                if (new File(MioInfo.DIR_OBJECTS, obj).exists()) {
                                    if (!禁用检测) {
                                        String sha1 = MioUtils.getFileSha1(new File(MioInfo.DIR_OBJECTS, obj).getAbsolutePath());
                                        if (sha1 != null) {
                                            if (!sha1.equals(new File(MioInfo.DIR_OBJECTS, obj).getName())) {
                                                new File(MioInfo.DIR_OBJECTS, obj).delete();
                                            }
                                        }
                                    }
                                }
                                if (!new File(MioInfo.DIR_OBJECTS, obj).exists()) {
                                    objects.add(obj);
                                }
                            }
                        } else {
                            runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告").setMessage("缺少清单文件：" + new File(MioInfo.DIR_INDEXES, mv.assets + ".json").getName() + "，是否下载？(只能下载官方版本)").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioLauncher.this, ActivityDownload.class);
                                        Bundle b = new Bundle();
                                        b.putString("version", mv.assets.equals("legacy") ? "1.6.4" : mv.inheritsFrom);
                                        i.putExtras(b);//传递Bundle打包的数据
                                        startActivity(i);//启动第二个activity并把i传递过去
                                    }
                                }).setNegativeButton("取消", null).create();
                                dialog.show();

                            });
                            progressDialog.dismiss();
                            return;
                        }

                    }

                }
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    if (libs.size() != 0) {
                        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告").setMessage("检测到缺少依赖库文件，无法启动游戏，是否下载？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                              FileDownloader.setup(MioLauncher.this);
                                initDialog();
                                downloadLibraries(libs);
                            }
                        }).setNegativeButton("否", null).create();
                        dialog.show();
                    } else if (objects.size() != 0) {
                        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("警告").setMessage("检测到缺少游戏资源文件，无法启动游戏，是否下载？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                              FileDownloader.setup(MioLauncher.this);
                                initDialog();
                                downloadObjects(objects);
                            }
                        }).setNegativeButton("否", null).create();
                        dialog.show();
                    }else {
                        if (adaptwr_user.getSelected() == null) {
                            toast("未创建用户");
                            return;
                        }
                        if (adaptwr_user.getSelected().getUserState().equals("正版登录")) {
                            View temlView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.view_progress, null);
                            final AlertDialog tempdialog = new AlertDialog.Builder(MioLauncher.this).setTitle("登录中......").setView(temlView).create();
                            tempdialog.setCancelable(false);
                            tempdialog.setCanceledOnTouchOutside(false);
                            final MioLogin tempMioLogin = new MioLogin();
                            tempMioLogin.setUrl(adaptwr_user.getSelected().getUrl().equals("") ? "https://authserver.mojang.com" : adaptwr_user.getSelected().getUrl());
                            tempMioLogin.setListener(new MioLogin.LoginListener() {
                                @Override
                                public void onStart() {
                                    tempdialog.show();
                                }

                                @Override
                                public void onSucceed(ArrayMap<String, String> map) {
                                    MioInfo.config.set("auth_access_token", map.get("accessToken"));
                                    MioInfo.config.set("auth_uuid", map.get("uuid"));
                                    try {
                                        JSONObject json = new JSONObject(msh.getString("users", ""));
                                        UserBean bean = adaptwr_user.getSelected();
                                        JSONObject json2 = json.getJSONObject(bean.getUserName());
                                        json2.put("token", map.get("accessToken"));
                                        json.put(bean.getUserName(), json2);
                                        mshe.putString("users", json.toString());
                                        mshe.commit();
                                    } catch (JSONException e) {

                                    }
                                    adaptwr_user.notifyDataSetChanged();
                                    tempdialog.dismiss();
                                    startActivity(new Intent(MioLauncher.this, BoatActivity.class));
                                }
                                @Override
                                public void onError(String error) {
                                    tempdialog.dismiss();
                                    AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this).setTitle("提示").setMessage("登录错误：" + error + "\n是否重新登录？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dia, int which) {
                                            tempMioLogin.login(adaptwr_user.getSelected().getUserAccount(), adaptwr_user.getSelected().getUserPass());
                                        }
                                    }).setNegativeButton("否", null).create();
                                    dialog.show();
                                }
                            });
                            tempMioLogin.checkOrRefresh(adaptwr_user.getSelected().getToken());
                        }
                        Intent intent = new Intent(MioLauncher.this, BoatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(intent);
                    }
                });
            }).start();
        } else {
            toast("未选择游戏版本");
        }
    }
    private void initDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.alert_download_progress, null);
        textFile = v.findViewById(R.id.alert_downloadTextViewFile);
        textProgress = v.findViewById(R.id.alert_downloadTextViewProgress);
        progressBar = v.findViewById(R.id.alert_downloadProgressBar);
        textTotalProgress = v.findViewById(R.id.alert_downloadTextViewTotalProgress);
        textSpeed = v.findViewById(R.id.alert_downloadTextViewSpeed);
        totalProgressBar = v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
        textInfo = v.findViewById(R.id.alert_download_info);
        mDialog = new AlertDialog.Builder(this).setView(v).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                        mDialog.dismiss();
                    }
        }).create();
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
    public List<String> getAssets(String filePath) {
        AssetsJson mAssetsJson;
        List<String> assets = new ArrayList<>();
        try {
            FileReader reader = new FileReader(filePath);
            mAssetsJson = new Gson().fromJson(reader, AssetsJson.class);
            reader.close();
        }catch (Exception e) {
            return null;
        }
        HashMap<String, AssetsJson.MinecraftAssetInfo> objects = mAssetsJson.getObjects();
        Set<String> keySet = objects.keySet();
        for (String key : keySet) {
            AssetsJson.MinecraftAssetInfo info = objects.get(key);
            String path = info.getHash().substring(0, 2);
            String url = path + "/" + info.getHash();
            assets.add(url);
        }
        return assets;
    }
    private void downloadLibraries(List<String> libs) {
        textInfo.setText("正在下载依赖库");
        fileCurrentCount = 0;
        FileDownloadListener queueTarget = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {}
            @Override
            protected void started(BaseDownloadTask task) {
                textFile.setText(task.getFilename());
            }
            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {}
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                long progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress((int) progress);
                textProgress.setText(progress + "%");
                textSpeed.setText(task.getSpeed() + "kb/s");
            }
            @Override
            protected void blockComplete(BaseDownloadTask task) {}
            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                //toast("重试");
            }
            @Override
            protected void completed(BaseDownloadTask task) {
                fileCurrentCount++;
                totalProgressBar.setProgress(fileCurrentCount);
                textTotalProgress.setText(fileCurrentCount * 100 / fileCount + "%");
                if (fileCurrentCount == totalProgressBar.getMax()) {
                    mDialog.dismiss();
                    Toast.makeText(MioLauncher.this, "下载完成。", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //toast("暂停");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                toast("错误：" + e.toString());
            }
            @Override
            protected void warn(BaseDownloadTask task) {
                //toast("警告");
            }
        };
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String lib : libs) {
            tasks.add(FileDownloader.getImpl().create(UrlSource.MCBBS_LIBRARIES + "/" + lib).setTag(lib).setPath(new File(MioInfo.DIR_LIBRARIES, lib).getAbsolutePath()).setAutoRetryTimes(5));
        }
        fileCount = libs.size();
        totalProgressBar.setMax(fileCount);
        queueSet.setAutoRetryTimes(5);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
    private void downloadObjects(List<String> objects) {
        textInfo.setText("正在下载游戏资源文件");
        fileCurrentCount = 0;
        FileDownloadListener queueTarget = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {}
            @Override
            protected void started(BaseDownloadTask task) {
                textFile.setText(task.getFilename());
            }
            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {}
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                long progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress((int) progress);
                textProgress.setText(progress + "%");
                textSpeed.setText(task.getSpeed() + "kb/s");
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
                textTotalProgress.setText(fileCurrentCount * 100 / fileCount + "%");
                if (fileCurrentCount == totalProgressBar.getMax()) {
                    mDialog.dismiss();
                    Toast.makeText(MioLauncher.this, "下载完成。", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //toast("暂停");
            }
            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                toast("错误：" + e.toString());
            }
            @Override
            protected void warn(BaseDownloadTask task) {
                //toast("警告");
            }
        };
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String obj : objects) {
            tasks.add(FileDownloader.getImpl().create(UrlSource.MCBBS_ASSETS_OBJS + "/" + obj).setTag(obj).setPath(new File(MioInfo.DIR_OBJECTS, obj).getAbsolutePath()).setAutoRetryTimes(5));
        }
        fileCount = objects.size();
        totalProgressBar.setMax(fileCount);
        queueSet.setAutoRetryTimes(5);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
    //解析用户信息
    private void parseJsonToUser() {
        if (userList == null) {
            userList = new ArrayList<UserBean>();
        } else {
            userList.clear();
        }
        if ((!msh.getString("users", "").equals("")) && !msh.getString("users", "").equals("{}")) {
            try {
                JSONObject json = new JSONObject(msh.getString("users", ""));
                JSONArray jsa = json.names();
                for (int i = 0; i < jsa.length(); i++) {
                    UserBean user = new UserBean();
                    user.setUserName(jsa.getString(i));
                    JSONObject json2 = json.getJSONObject(jsa.getString(i));
                    user.setUserState(json2.getString("state"));
                    user.setIsSelected(json2.getBoolean("isSelected"));
                    user.setUuid(json2.getString("uuid"));
                    user.setToken(json2.getString("token"));
                    user.setUrl(json2.getString("url"));
                    JSONArray jsa2 = json2.getJSONArray("loginInfo");
                    user.setUserAccount(jsa2.getString(0));
                    user.setUserPass(jsa2.getString(1));
                    userList.add(user);
                }
            } catch (JSONException e) {
                toast(e.toString());
            }
        }


    }

    private void initLoadingView() {
        View base = LayoutInflater.from(this).inflate(R.layout.waveloading, null);
        wave = base.findViewById(R.id.wave);
        ImageButton exit = base.findViewById(R.id.waveloadingImageButtonExit);
        exit.setVisibility(View.INVISIBLE);
        popupWindow = new PopupWindow();
        popupWindow.setWidth(LayoutParams.FILL_PARENT);
        popupWindow.setHeight(LayoutParams.FILL_PARENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(base);

    }

    //find按键并绑定点击事件
    private Button findButton(int id) {
        Button temp = findViewById(id);
        temp.setOnClickListener(this);
        return temp;
    }
    @Override
    public void onClick(View v) {
        if (v == toolbar_button_home) {
            toolbar_text_state.setText("主页");
            leftBtnKeep(null);
            startGame.setVisibility(View.VISIBLE);
            tip_container.setVisibility(View.VISIBLE);
            AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
            disappearAnimation.setDuration(500);
            for (final View vv : list_layouts) {
                if (vv.getVisibility() == View.VISIBLE) {
                    vv.startAnimation(disappearAnimation);
                    disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation p1) {
                        }

                        @Override
                        public void onAnimationEnd(Animation p1) {
                            vv.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation p1) {
                        }
                    });
                }
            }
        } else if (v == toolbar_button_exit) {
            finish();
            System.exit(0);
        } else if (v == left_btn_user) {
            showTargetView(layout_user);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_version) {
            showTargetView(layout_version);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_gamelist) {
            showTargetView(layout_gamelist);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_plugin) {
            showTargetView(layout_gamedir);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_setting) {
            showTargetView(layout_setting);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_log) {
            showTargetView(layout_log);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_about) {
            showTargetView(layout_about);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == layout_user_adduser) {
//            if(Splash.specialMode&&Splash.serverMode){
//                toast("此功能已禁用，用户在登录时已自动添加");
//                return;
//            }
            View tempView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.alert_login, null);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("添加用户")
                    .setView(tempView)
                    .create();
            dialog.show();
            final EditText tempUser = tempView.findViewById(R.id.alert_login_edit_account);
            final EditText tempPass = tempView.findViewById(R.id.alert_login_edit_password);
            Button tempLogin = tempView.findViewById(R.id.alert_login_btn_login);
            Button tempCancle = tempView.findViewById(R.id.alert_login_btn_cancle);
            CheckBox tempCheck = tempView.findViewById(R.id.alert_login_check);
            final LinearLayout tempPassLinear = tempView.findViewById(R.id.alert_login_linear_pass);
            EditText serverUrl = tempView.findViewById(R.id.alert_login_edit_url);

            tempLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    final String temp1 = tempUser.getText().toString();
                    final String temp2 = tempPass.getText().toString();
                    final String temp3 = serverUrl.getText().toString();
                    if (tempCheck.isChecked()) {
                        if (temp1.equals("")) {
                            toast("用户名不能为空");
                        } else {
                            addUserToJson(temp1, "离线模式", false, "", "", "", "");
                            adaptwr_user.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    } else {
                        if (temp1.trim().equals("") || temp2.trim().equals("")) {
                            toast("账号和密码都不能为空");
                        } else {
                            View temlView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.view_progress, null);
                            final AlertDialog tempdialog = new AlertDialog.Builder(MioLauncher.this)
                                    .setTitle("登录中......")
                                    .setView(temlView)
                                    .create();
                            tempdialog.setCancelable(false);
                            tempdialog.setCanceledOnTouchOutside(false);

                            MioLogin tempMioLogin = new MioLogin();
                            if (!serverUrl.getText().toString().trim().equals("")) {
                                tempMioLogin.setUrl(serverUrl.getText().toString());
                            }
                            tempMioLogin.setListener(new MioLogin.LoginListener() {
                                @Override
                                public void onStart() {
                                    tempdialog.show();
                                }

                                @Override
                                public void onSucceed(ArrayMap<String, String> map) {
                                    addUserToJson(map.get("name"), "正版登录", false, temp1, temp2, temp3, map.get("uuid"), map.get("accessToken"));
                                    adaptwr_user.notifyDataSetChanged();
                                    tempdialog.dismiss();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    tempdialog.dismiss();
                                    toast("登录错误：" + error);
                                }
                            });
                            tempMioLogin.login(temp1, temp2);

                        }
                    }
                }
            });
            tempCancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    dialog.dismiss();
                }
            });
            tempCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    if (p2) {
                        tempPassLinear.setVisibility(View.GONE);
                    } else {
                        tempPassLinear.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else if (v == layout_version_modctrl) {
//            startActivity(new Intent(MioLauncher.this, MioMod.class));
            Toast.makeText(this, "这个功能已经弃用，等我什么时候想修了再开放。", Toast.LENGTH_SHORT).show();
        } else if (v == layout_version_packctrl) {
//            startActivity(new Intent(MioLauncher.this, MioPack.class));
            Toast.makeText(this, "这个功能已经弃用，等我什么时候想修了再开放。", Toast.LENGTH_SHORT).show();
        } else if (v == layout_version_save) {
            MioInfo.config.set("extraJavaFlags", layout_version_editJvm.getText().toString());
            toast("已保存");
        } else if (v == layout_version_refresh) {
            layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
            toast("已刷新");
        } else if (v == layout_version_reset) {
            MioInfo.config.set("extraJavaFlags", "-Xmx1024M");
            layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
            toast("已重置");
        } else if (v == layout_gamelist_addgame) {
            startActivity(new Intent(MioLauncher.this, ActivityDownload.class));
        } else if (v == layout_gamelist_refresh) {
            adapter_game.getList().clear();
            String[] versions = new File(MioInfo.DIR_VERSIONS).list();
            if (versions.length != 0) {
                for (String s : versions) {
                    adapter_game.getList().add(s);
                    adapter_game.notifyDataSetChanged();
                }
            }
        } else if (v == layout_gamelist_move) {
            new LFilePicker()
                    .withActivity(MioLauncher.this)
                    .withRequestCode(6666)
                    .withStartPath("/storage/emulated/0/")
                    .withFileFilter(new String[]{".zip"})
                    .start();
        } else if (v == layout_plugin_install_installer) {
            if (layout_plugin_text_state_installer.getText().equals("未安装")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://icraft.ren:90/titles/Mio-Installer-0.1.apk");
                intent.setData(content_url);
                startActivity(intent);
            } else {
                Toast.makeText(this, "已安装", Toast.LENGTH_SHORT).show();
            }

        } else if (v == layout_plugin_open_installer) {
            if (layout_plugin_text_state_installer.getText().equals("未安装")) {
                Toast.makeText(this, "未安装", Toast.LENGTH_SHORT).show();
                return;
            }
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MioLauncher.this).setMessage("如需启动澪-Installer自定义安装功能请点击选择文件。").setPositiveButton("选择文件", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new LFilePicker().withActivity(MioLauncher.this).withRequestCode(666).withStartPath("/storage/emulated/0/").withFileFilter(new String[]{".jar"}).start();
                }
            }).setNegativeButton("取消", null).create();
            dialog.show();
        } else if (v == layout_log_share) {
            File file = new File(MioInfo.defaultMioLauncherDir_Public, "boat_output.txt");
            if (null != file && file.exists()) {
                Intent share = new Intent(Intent.ACTION_SEND);
                //android7以上用FileProvider
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                    share.putExtra(Intent.EXTRA_STREAM, contentUri);
                } else {
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                }
                share.setType("*/*");
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "分享文件"));
            } else {
                Toast.makeText(this, "分享文件不存在", Toast.LENGTH_SHORT).show();
            }

        } else if (v == layout_log_refresh) {
            new Thread(() -> {
                String s = MioUtils.readTxt(new File(MioInfo.defaultMioLauncherDir_Public, "boat_output.txt").getAbsolutePath());
                runOnUiThread(() -> layout_log_edit.setText(s.contains("错误") ? "无日志" : s));
            }).start();
        } else if (v == layout_settingButtonMouse) {
            String[] items = {"重置", "选择"};
            AlertDialog dialog = new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    if (which == 0) {
                        new File(MioUtils.getExternalFilesDir(MioLauncher.this), "澪/cursor.png").delete();
                    } else if (which == 1) {
                        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
                        intent.setType("image/*");
                        startActivityForResult(intent, 100);
                    }
                }
            }).setNegativeButton("取消", null).create();
            dialog.show();
        } else if (v == layout_settingButtonBackground) {
            String[] items = {"重置", "选择"};
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            if (which == 0) {
                                background.setBackgroundResource(R.drawable.background);
                                new File(MioUtils.getExternalFilesDir(MioLauncher.this), "澪/bg.png").delete();
                            } else if (which == 1) {
                                Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
                                intent.setType("image/*");
                                startActivityForResult(intent, 101);
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        } else if (v == layout_settingButtonFix) {
            final String[] items = {"GBK", "GB2312", "GB18030", "UTF-8"};
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("请选择游戏启动编码")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            MioInfo.config.set("extraJavaFlags", MioInfo.config.get("extraJavaFlags") + " -Dfile.encoding=" + items[which]);
                            layout_version_editJvm.append(" -Dfile.encoding=" + items[which]);
                            Toast.makeText(MioLauncher.this, "已选择编码:" + items[which], Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        }else if (v == layout_settingButtonChooseGif) {
            String[] items = {"重置", "选择"};
            AlertDialog dialog = new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    if (which == 0) {
                        background.setBackgroundResource(R.drawable.background);
                        new File(MioUtils.getExternalFilesDir(MioLauncher.this), "澪/mygif.gif").delete();
                    } else if (which == 1) {
                        new LFilePicker().withActivity(MioLauncher.this).withRequestCode(102).withStartPath(MioUtils.getStoragePath()).withFileFilter(new String[]{".gif"}).start();
                    }
                }
            }).setNegativeButton("取消", null).create();
            dialog.show();
        } else if (v == layout_settingButtonHelp) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://b23.tv/EVcXx9");
            intent.setData(content_url);
            Toast.makeText(MioLauncher.this, "圆圆的东西(疯狂暗示)", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else if (v == layout_settingButtonChangeFbl) {
            final View view = LayoutInflater.from(this).inflate(R.layout.alert_change_screen_size, null);
            BubbleSeekBar seekBar = view.findViewById(R.id.alert_change_screen_size_seekBar);
            seekBar.setProgress((int) (msh.getFloat("分辨率缩放", 1.0f) * 100));
            seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                }
            });
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setTitle("设置缩放大小")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mshe.putFloat("分辨率缩放", ((float) seekBar.getProgress()) / 100);
                            mshe.commit();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        } else if (v == layout_about_donate) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://afdian.net/@boatmio");
            intent.setData(content_url);
            Toast.makeText(MioLauncher.this, "即将跳转至浏览器...", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }

    private void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "cosine.launcher.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);

    }

    //添加用户
    private void addUserToJson(String name, String state, boolean isSelected, String account, String pass, String uuid, String token) {
        try {
            JSONObject json = null;
            if (msh.getString("users", "").equals("")) {
                json = new JSONObject();
            } else {
                json = new JSONObject(msh.getString("users", ""));
            }
            JSONObject userData = new JSONObject();
            userData.put("state", state);
            userData.put("isSelected", isSelected);
            userData.put("loginInfo", new JSONArray().put(0, account).put(1, pass));
            userData.put("uuid", uuid);
            userData.put("token", token);
            userData.put("url", "");
            json.put(name, userData);
            mshe.putString("users", json.toString());
            mshe.commit();
            parseJsonToUser();
        } catch (JSONException e) {
            toast(e.toString());
        }

    }

    private void addUserToJson(String name, String state, boolean isSelected, String account, String pass, String url, String uuid, String token) {
        try {
            JSONObject json = null;
            if (msh.getString("users", "").equals("")) {
                json = new JSONObject();
            } else {
                json = new JSONObject(msh.getString("users", ""));
            }
            JSONObject userData = new JSONObject();
            userData.put("state", state);
            userData.put("isSelected", isSelected);
            userData.put("loginInfo", new JSONArray().put(0, account).put(1, pass));
            userData.put("uuid", uuid);
            userData.put("token", token);
            userData.put("url", url);
            json.put(name, userData);
            mshe.putString("users", json.toString());
            mshe.commit();
            parseJsonToUser();
        } catch (JSONException e) {
            toast(e.toString());
        }

    }

    //消失/出现动画
    private void showTargetView(View v) {
        startGame.setVisibility(View.INVISIBLE);
        tip_container.setVisibility(View.INVISIBLE);
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);
        if (v.getVisibility() == View.INVISIBLE) {
            v.startAnimation(appearAnimation);
            v.setVisibility(View.VISIBLE);
        }
        for (View vv : list_layouts) {
            if (vv != v && vv.getVisibility() == View.VISIBLE) {
                vv.startAnimation(disappearAnimation);
                vv.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void leftBtnKeep(View b) {
        left_btn_user.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_version.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_gamelist.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_plugin.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_setting.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_about.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_log.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_plugin.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_about.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_gamelist.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_setting.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_user.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_version.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_log.setTextColor(Color.parseColor("#FFF97297"));
        if (b != null) {
            b.setBackgroundResource(R.drawable.layout_button_background_pressed);
            ((Button) b).setTextColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            Uri uri = data.getData();
            if (requestCode == 100) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").mkdirs();
                }
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/cursor.png");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 101) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").mkdirs();
                }
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/bg.png");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(this) + "/澪/bg.png");
                    BitmapDrawable bd = new BitmapDrawable(bitmap);
                    background.setBackground(bd);
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 102) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/").mkdirs();
                }
                try {
                    final File path = new File((String) data.getStringArrayListExtra("paths").get(0));
                    InputStream in = new FileInputStream(path);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/澪/mygif.gif");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 233) {
            } else if (requestCode == 666) {
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
                bundle.putString("modFile", (String) data.getStringArrayListExtra("paths").get(0));
                intent.putExtra("modFile", bundle);
                startActivity(intent);
            } else if (requestCode == 6666) {
                String path = (String) data.getStringArrayListExtra("paths").get(0);
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("正在进行迁移，请等待...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                new Thread(() -> {
                    File dir = new File(MioInfo.DIR_TEMP, "迁移");
                    try {
                        FileUtils.forceDelete(dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        new ZipFile(path).extractAll(dir.getAbsolutePath());
                    } catch (ZipException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            toast("迁移失败：" + e.toString());
                        });
                    }
                    File gamedir;
                    if (new File(dir, "boat").exists()) {
                        gamedir = new File(dir, "boat/gamedir");
                    } else if (new File(dir, "gamedir").exists()) {
                        gamedir = new File(dir, "gamedir");
                    } else {
                        runOnUiThread(() -> {
                            toast("未找到符合boat目录的文件信息");
                            dialog.dismiss();
                        });
                        return;
                    }
                    runOnUiThread(() -> {
                        String[] items = new File(gamedir, "versions").list();
                        AlertDialog dialog1 = new AlertDialog.Builder(MioLauncher.this).setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                new Thread(() -> {
                                    try {
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"assets"),new File(MioInfo.defaultGameDir_Public),true);
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"libraries"),new File(MioInfo.defaultGameDir_Public),true);
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"versions"),new File(MioInfo.defaultGameDir_Public),true);
//                                      FileUtils.moveDirectory(gamedir,new File(MioInfo.DIR_VERSIONS,items[which]));
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "assets").getAbsolutePath() + " " + new File(MioInfo.defaultGameDir_Public).getAbsolutePath());
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "libraries").getAbsolutePath() + " " + new File(MioInfo.defaultGameDir_Public).getAbsolutePath());
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "versions").getAbsolutePath() + " " + new File(MioInfo.defaultGameDir_Public).getAbsolutePath());
                                        for (File s:gamedir.listFiles()){
                                            MioShell.doShell("cp -r -f " + s.getAbsolutePath() + " " + new File(MioInfo.DIR_VERSIONS, items[which]).getAbsolutePath());
                                        }
//                                          MioShell.doShell("cp -r -f " + gamedir.getAbsolutePath() + "/* " + new File(MioInfo.DIR_VERSIONS, items[which]).getAbsolutePath());
                                        MioShell.doShell("rm -rf "+gamedir.getAbsolutePath());
                                        runOnUiThread(() -> {
                                            toast("迁移完成，请刷新。");
                                            dialog.dismiss();
                                        });
                                    } catch (Exception e) {
                                        Log.e("移动文件出现错误：", e.toString());
                                        runOnUiThread(() -> {
                                            toast("移动文件出现错误：" + e.toString());
                                            dialog.dismiss();
                                        });
                                    }
                                }).start();
                            }
                        }).setTitle("请选择需要迁移的非原版版本").setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                dialog.dismiss();
                            }
                        }).create();dialog1.show();
                    });
                }).start();
            }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
        FileDownloader.getImpl().clearAllTaskData();
    }
}
