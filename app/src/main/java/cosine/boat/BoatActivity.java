package cosine.boat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.cardview.widget.CardView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mio.launcher.MioInfo;
import com.mio.launcher.MioMouseKeyboard;
import com.mio.launcher.MioUtils;
import com.mio.launcher.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import com.mio.launcher.Splash;
import com.mio.mclauncher.customcontrol.MioCrossingKeyboard;
import com.mio.mclauncher.customcontrol.MioCustomButton;
import com.mio.mclauncher.customcontrol.MioCustomManager;

import android.view.ViewGroup;



/**
 * @author mio
 */
public class BoatActivity extends Activity implements TextureView.SurfaceTextureListener, View.OnClickListener, TextView.OnEditorActionListener
{
	static{
		System.loadLibrary("boat");
	}
	public native static void setMioNativeWindow(Surface surface);
    private DrawerLayout base;
    private Button control1;
    private Button control2;
    private Button control3;
    private Button control4;
    private Button control5;
    private Button control6;
    private Button control7;
    private Button control8;
    private Button control9;
    private LinearLayout itemBar;
    private ImageView mouseCursor;

	public boolean mode = false;

	private DrawerLayout mDrawerLayout;
	private Button btq;
    private Button btw;
    private Button bte;
    private Button btr;
    private Button btt;
    private Button bty;
    private Button btu;
    private Button bti;
    private Button bto;
    private Button btp;
    private Button bta;
    private Button btns;
    private Button btd;
    private Button btf;
    private Button btg;
    private Button bth;
    private Button btj;
    private Button btk;
    private Button btl;
    private Button btz;
    private Button btx;
    private Button btc;
    private Button btv;
    private Button btb;
    private Button btn;
    private Button btm;
    private Button f1;
    private Button f2;
    private Button f3;
    private Button f4;
    private Button f5;
    private Button f6;
    private Button f7;
    private Button f8;
    private Button f9;
    private Button f10;
    private Button f11;
    private Button f12;
    private Button shift,space,btpr,btse;
    private long 按下_时间;
    private boolean 点击状态=false;
    private boolean 全键_潜行=false;
    public static int screenwidth,screenheight;
    private int dx,dy;
    private Handler handler=new Handler();
    private boolean 长按=false;

    private GifImageView mio;

    private SharedPreferences msh;
    private SharedPreferences.Editor mshe;
	
    private boolean 显示=true;


    private RelativeLayout overlay;

    private MioCustomManager mMioCustomManager;
    private MioCrossingKeyboard mioCrossingKeyboard;


    //陀螺仪
    Sensor 陀螺仪;
    SensorManager 传感器管理器;
    long 时间;
    boolean 传感器开关=false;
    SensorEventListener 传感器事件;
   
	//绘制
	private TextureView miosurface;
	
	//键鼠
	private MioMouseKeyboard mMioMouseKeyBoard;

	private boolean mouseTouchMode=false;
	private boolean 禁用手势=false;

	private Button openKeyboard;
	@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        base = (DrawerLayout) LayoutInflater.from(BoatActivity.this).inflate(R.layout.mio_overlay,null);
		setContentView(base);

        overlay=base.findViewById(R.id.overlayRelativeLayout);
        miosurface=overlay.findViewById(R.id.miosurface);
		miosurface.setSurfaceTextureListener(this);
		miosurface.setFocusable(true);
		miosurface.setOnTouchListener(ot);
        msh=getSharedPreferences("Mio",MODE_PRIVATE);
        mshe=msh.edit();
		
		mouseCursor = (ImageView)base.findViewById(R.id.mouse_cursor);

        禁用手势=msh.getBoolean("禁用手势",false);

		control1 = base.findViewById(R.id.control_1);
		control2 = base.findViewById(R.id.control_2);
		control3 = base.findViewById(R.id.control_3);
		control4 = base.findViewById(R.id.control_4);
		control5 = base.findViewById(R.id.control_5);
		control6 = base.findViewById(R.id.control_6);
		control7 = base.findViewById(R.id.control_7);
		control8 = base.findViewById(R.id.control_8);
		control9 = base.findViewById(R.id.control_9);
		
		control1.setOnTouchListener(ot);//(R.id.control_1);
		control2.setOnTouchListener(ot);//(R.id.control_2);
		control3.setOnTouchListener(ot);//(R.id.control_3);
		control4.setOnTouchListener(ot);//(R.id.control_4);
		control5.setOnTouchListener(ot);//(R.id.control_5);
		control6.setOnTouchListener(ot);//(R.id.control_6);
		control7.setOnTouchListener(ot);//(R.id.control_7);
		control8.setOnTouchListener(ot);//(R.id.control_8);
		control9.setOnTouchListener(ot);//(R.id.control_9);
        
		itemBar = (LinearLayout)base.findViewById(R.id.item_bar);
        //全键
        btq=findB(R.id.btq);
        btw=findB(R.id.btw);
        bte=findB(R.id.bte);
        btr=findB(R.id.btr);
        btt=findB(R.id.btt);
        bty=findB(R.id.bty);
        btu=findB(R.id.btu);
        bti=findB(R.id.bti);
        bto=findB(R.id.bto);
        btp=findB(R.id.btp);
        bta=findB(R.id.bta);
        btns=findB(R.id.bts);
        btd=findB(R.id.btd);
        btf=findB(R.id.btf);
        btg=findB(R.id.btg);
        bth=findB(R.id.bth);
        btj=findB(R.id.btj);
        btk=findB(R.id.btk);
        btl=findB(R.id.btl);
        btz=findB(R.id.btz);
        btx=findB(R.id.btx);
        btc=findB(R.id.btc);
        btv=findB(R.id.btv);
        btb=findB(R.id.btb);
        btn=findB(R.id.btn);
        btm=findB(R.id.btm);
        f1=findB(R.id.control_f1);
        f2=findB(R.id.control_f2);
        f3=findB(R.id.control_f3);
        f4=findB(R.id.control_f4);
        f5=findB(R.id.control_f5);
        f6=findB(R.id.control_f6);
        f7=findB(R.id.control_f7);
        f8=findB(R.id.control_f8);
        f9=findB(R.id.control_f9);
        f10=findB(R.id.control_f10);
        f11=findB(R.id.control_f11);
        f12=findB(R.id.control_f12);
        shift=(Button)base.findViewById(R.id.btshift);
        shift.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if(全键_潜行)
                    {
						((Button)p1).setTextColor(Color.parseColor("#FFF97297"));
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,true);
                        全键_潜行=false;
                    }
                    else
                    {
						((Button)p1).setTextColor(Color.WHITE);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,false);
                        全键_潜行=true;
                    }

                }
            });
		mDrawerLayout=base.findViewById(R.id.mDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
                @Override
                public void onDrawerSlide(View p1, float p2) {
                }

                @Override
                public void onDrawerOpened(View p1) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onDrawerClosed(View p1) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }

                @Override
                public void onDrawerStateChanged(int p1) {
                }
            });
        space=findB(R.id.btjump);
        btpr=findB(R.id.btpr);
        btse=findB(R.id.btse);

		mHandler = new MyHandler();
		
		
		initCard();

         
        if(new File(MioUtils.getExternalFilesDir(BoatActivity.this)+"/澪/cursor.png").exists()){
            
			new Thread(new Runnable(){
					@Override
					public void run() {
						final Bitmap bitmap=BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(BoatActivity.this)+"/澪/cursor.png");
						runOnUiThread(new Runnable(){
								@Override
								public void run() {
									mouseCursor.setImageBitmap(bitmap);
								}
							});
					}
				}).start();
        }
        openKeyboard=base.findViewById(R.id.openKeyboard);
        openKeyboard.setOnClickListener((v)->{
            showKeyboard();
        });
	}
    private int 灵敏度;
    private void initTLY(){
        时间=0;
        灵敏度=msh.getInt("灵敏度",8);
        if(传感器管理器==null){
            传感器管理器=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
            陀螺仪=传感器管理器.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            传感器事件=new SensorEventListener(){
                @Override
                public void onSensorChanged(SensorEvent event) {
                    //1z  2x  3y
                    if(时间!=0){
                        float 时间差=(event.timestamp-时间)/1000000000.0f;
                        if((!(Math.abs(Math.toDegrees(event.values[0]*时间差)*灵敏度)<0.2)||!(Math.abs(Math.toDegrees(event.values[1]*时间差)*灵敏度)<0.2))){
                            baseX+=-Math.toDegrees(event.values[0]*时间差)*灵敏度;
                            baseY+=Math.toDegrees(event.values[1]*时间差)*灵敏度;
                            BoatInput.setPointer(baseX, baseY);
                            mouseCursor.setX(baseX);
                            mouseCursor.setY(baseY);
                        }
                        
                    }
                    时间=event.timestamp;

                }

                @Override
                public void onAccuracyChanged(Sensor p1, int p2) {

                }
            };
        }
        
        传感器管理器.registerListener(传感器事件, 陀螺仪, SensorManager.SENSOR_DELAY_GAME);
    }

    public void initMio() {
        final RelativeLayout mio_gif_layout=base.findViewById(R.id.mio_gif_layout);
		mio = mio_gif_layout.findViewById(R.id.mio);
		
		if(new File(MioUtils.getExternalFilesDir(this),"澪/mygif.gif").exists()){
			try {
				GifDrawable gifDrawable=new GifDrawable(new File(MioUtils.getExternalFilesDir(this), "澪/mygif.gif").getAbsolutePath());
				mio.setImageDrawable(gifDrawable);
				gifDrawable.start();
			} catch (IOException e) {
				Toast.makeText(this,"gif加载失败："+e.toString(),Toast.LENGTH_LONG).show();
			}
		}else{
			try {
				GifDrawable gifDrawable  = new GifDrawable(getResources(), R.drawable.fbk);
				mio.setImageDrawable(gifDrawable);
				gifDrawable.start();
			} catch (Resources.NotFoundException e) {
				
			} catch (IOException e) {
				
			}
		}
		
		
        Button mio_gif_touch=mio_gif_layout.findViewById(R.id.mio_gif_touch);
        mio_gif_layout.setX((screenwidth / 2)-(mio.getWidth()/2));
        mio_gif_layout.setY((screenheight / 2)-(mio.getHeight()/2));

        mio_gif_touch.setOnTouchListener(new View.OnTouchListener(){
            private int 触摸点横坐标_gif;

            private int 触摸点纵坐标_gif;

            private int 横坐标_gif偏移;

            private int 纵坐标_gif偏移;

            private int 左边距_gif;

            private int 上边距_gif;

            private int 右边距_gif;

            private int 下边距_gif;

            private int 下边距;

            private Handler 容器事件回调长按计时器=new Handler();
            private Runnable 任务=new Runnable() {
                @Override
                public void run() {
                    if (mMioCustomManager.获取开关状态()){
                        mMioCustomManager.自定义开关();
                        mioCrossingKeyboard.自定义();
                        mshe.putFloat("方向键x",mioCrossingKeyboard.getX());
                        mshe.putFloat("方向键y",mioCrossingKeyboard.getY());
                        mshe.putFloat("方向键缩放",mioCrossingKeyboard.mScale);
                        mshe.commit();
                        mio_gif_layout.bringToFront();
                    }
                }
            };
            long 按下时间 = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    触摸点横坐标_gif = (int)event.getX();
                    触摸点纵坐标_gif = (int)event.getY();
                    按下时间=System.currentTimeMillis();
                    容器事件回调长按计时器.postDelayed(任务, 500);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (Math.abs(event.getX() - 触摸点横坐标_gif) >= 20 || Math.abs(event.getY() - 触摸点纵坐标_gif) >= 20) {
                        容器事件回调长按计时器.removeCallbacks(任务);
                    }
                    横坐标_gif偏移 = (int)event.getX() - 触摸点横坐标_gif;
                    纵坐标_gif偏移 = (int)event.getY() - 触摸点纵坐标_gif;
                    左边距_gif = mio_gif_layout.getLeft() + 横坐标_gif偏移;
                    上边距_gif = mio_gif_layout.getTop() + 纵坐标_gif偏移;
                    右边距_gif = mio_gif_layout.getRight() + 横坐标_gif偏移;
                    下边距_gif = mio_gif_layout.getBottom() +纵坐标_gif偏移;
                    下边距 = mio_gif_layout.getHeight() - mio_gif_layout.getBottom() - 纵坐标_gif偏移;
                    mio_gif_layout.layout(左边距_gif, 上边距_gif, 右边距_gif, 下边距_gif);
                    RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)mio_gif_layout.getLayoutParams();
//                    params.leftMargin=左边距_gif;
//                    params.bottomMargin=下边距;
                    params.setMargins(左边距_gif,上边距_gif,右边距_gif,下边距_gif);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    容器事件回调长按计时器.removeCallbacks(任务);
                    if ((System.currentTimeMillis() - 按下时间) < 100 && Math.abs(event.getX() - 触摸点横坐标_gif) <= 5 && Math.abs(event.getY() - 触摸点纵坐标_gif) <= 5) {
                        if (!mMioCustomManager.获取开关状态()){
                            mDrawerLayout.openDrawer(Gravity.LEFT);
                            mDrawerLayout.openDrawer(Gravity.RIGHT);
                        }else {
                            if (mMioCustomManager.获取按键选择模式()) {
                                mMioCustomManager.设置按键选择模式(false);
                                if (mMioCustomManager.获取按键修改模式()) {
                                    mMioCustomManager.自定义按键对话框(false);
                                    if (mMioCustomManager.获取当前按键().get按键类型().equals("显隐控制按键")) {
                                        for (String 标识:mMioCustomManager.获取当前按键().get目标按键标识()) {
                                            MioCustomButton b=mMioCustomManager.获取按键(标识);
                                            b.setTextColor(Color.parseColor(b.get文本颜色()));
                                            b.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    mMioCustomManager.自定义按键对话框(true);
                                }
                            } else {
                                mMioCustomManager.自定义按键对话框(true);
                            }
                        }

                    }

                }
                return true;
            }
        });
        File key=new File(MioInfo.config.get("currentVersion"),"Miokey.json");
        File options=new File(MioInfo.config.get("currentVersion"),"options.txt");
        if (!key.exists()){
            try {
                MioUtils.copyFromAssets(this,key.getName(),key.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!options.exists()){
            try {
                MioUtils.copyFromAssets(this,options.getName(),options.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMioCustomManager=new MioCustomManager();
        mMioCustomManager.初始化(this,overlay, new File(MioInfo.config.get("currentVersion")).getAbsolutePath());
        mioCrossingKeyboard=overlay.findViewById(R.id.mio_keyboard);
        float xxx=msh.getFloat("方向键x",0);
        float yyy=msh.getFloat("方向键y",0);
        if (xxx!=0&&yyy!=0){
            mioCrossingKeyboard.setX(xxx);
            mioCrossingKeyboard.setY(yyy);
        }
        mioCrossingKeyboard.setScale(msh.getFloat("方向键缩放",1.0f));
        mioCrossingKeyboard.setListener(new MioCrossingKeyboard.MioListener(){
            private boolean upFromCenter=false;
            private boolean upToCenter=false;
            private boolean isShift=false;

            @Override
            public void onLeftUp() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
            }

            @Override
            public void onUp() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
            }

            @Override
            public void onRightUp() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);
            }

            @Override
            public void onLeft() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
            }

            @Override
            public void onCenter(boolean direct) {
                if (direct){
//                    if (isShift){
//                        isShift=false;
//                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,false);
//                    }else {
//                        isShift=true;
//                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,true);
//                    }
                }else{
                    upFromCenter=true;
                }
            }

            @Override
            public void onRight() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);
            }

            @Override
            public void onLeftDown() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
            }

            @Override
            public void onDown() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
            }

            @Override
            public void onRightDown() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);
            }

            @Override
            public void onSlipLeftUp() {
                System.out.println("左上");
            }

            @Override
            public void onSlipUp() {
                System.out.println("上");
            }

            @Override
            public void onSlipRightUp() {
                System.out.println("右上");
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,false);
            }

            @Override
            public void onSlipLeft() {
                System.out.println("左");
            }

            @Override
            public void onSlipRight() {
                System.out.println("右");
            }

            @Override
            public void onSlipLeftDown() {
                System.out.println("左下");
                showKeyboard();
            }

            @Override
            public void onSlipDown() {
                System.out.println("下");
            }

            @Override
            public void onSlipRightDown() {
                System.out.println("右下");
            }

            @Override
            public void onUpToCenter() {
                if (!禁用手势){
                    upToCenter=true;
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Space, 0,true);
                }


            }

            @Override
            public void onFingerUp() {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
                if(upToCenter){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Space, 0,false);
                    upToCenter=false;
                }

            }
        });

        mMioCustomManager.设置自定义按键回调(new MioCustomManager.自定义按键回调(){

            @Override
            public void 命令接收事件(String 命令) {
                input(命令);
            }

            @Override
            public void 键值接收事件(int 键值, boolean 按下) {
                Log.e("状态",按下+"");
                BoatInput.setKey(键值, 0,按下);
            }

            @Override
            public void 控制鼠标指针移动事件(int x, int y) {
                baseX+=x;
                baseY+=y;
                BoatInput.setPointer(baseX, baseY);
                mouseCursor.setX(baseX);
                mouseCursor.setY(baseY);

            }

            @Override
            public void 鼠标回调(int 键值, boolean 按下) {
                BoatInput.setMouseButton(键值,按下);
            }

            @Override
            public void 按下() {
//                屏幕控制_按下=true;
            }

            @Override
            public void 抬起() {
//                屏幕控制_按下=false;
            }
        });
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                miosurface.setFocusable(true);
                mMioMouseKeyBoard=new MioMouseKeyboard(this,mouseCursor,miosurface);
            }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            BoatInput.setMouseButton(BoatInput.Button3, true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            BoatInput.setMouseButton(BoatInput.Button3, false);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    public void showKeyboard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        miosurface.requestFocusFromTouch();
        miosurface.requestFocus();
    }
    public void hideKeyboard() {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//分割线————————————————————————————————————————————————————————————
	private CardView custom,exit;
    private CardView command,commandSetting,destorySwitch,buttonVisible,liandianqi,tly,quan,mouseSwitch,guesture_disable;
    private boolean 全键=false;
	private void initCard(){
		OnClickListener card_listener=new OnClickListener(){
			@Override
			public void onClick(View p1) {
				if(p1==custom){
                    CharSequence[] items = {"进入自定义按键模式","重置按键"};
                    AlertDialog dialog = new AlertDialog.Builder(BoatActivity.this)
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    switch (which){
                                        case 0:
                                            mMioCustomManager.自定义开关();
                                            mioCrossingKeyboard.自定义();
                                            break;
                                        case 1:
                                            mMioCustomManager.清除按键();
                                            try {
                                                File key=new File(MioInfo.config.get("currentVersion"),"Miokey.json");
                                                MioUtils.copyFromAssets(BoatActivity.this,key.getName(),key.getAbsolutePath());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            mMioCustomManager=null;
                                            mMioCustomManager=new MioCustomManager();
                                            mMioCustomManager.初始化(BoatActivity.this,overlay, new File(MioInfo.config.get("currentVersion")).getAbsolutePath());
                                            mMioCustomManager.设置自定义按键回调(new MioCustomManager.自定义按键回调(){

                                                @Override
                                                public void 命令接收事件(String 命令) {
                                                    input(命令);
                                                }

                                                @Override
                                                public void 键值接收事件(int 键值, boolean 按下) {
                                                    BoatInput.setKey(键值, 0,按下);
                                                }

                                                @Override
                                                public void 控制鼠标指针移动事件(int x, int y) {
                                                    baseX+=x;
                                                    baseY+=y;
                                                    BoatInput.setPointer(baseX, baseY);
                                                    mouseCursor.setX(baseX);
                                                    mouseCursor.setY(baseY);

                                                }

                                                @Override
                                                public void 按下() {
//                                                    屏幕控制_按下=true;
                                                }

                                                @Override
                                                public void 抬起() {
//                                                    屏幕控制_按下=false;
                                                }

                                                @Override
                                                public void 鼠标回调(int 键值, boolean 按下) {
                                                    BoatInput.setMouseButton(键值,按下);
                                                }
                                            });
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                    dialog.show();
					
				}else if(p1==exit){
                    
					AlertDialog dialog=new AlertDialog.Builder(BoatActivity.this)
						.setTitle(getStr(R.string.tip))
						.setMessage(getStr(R.string.sureExit))
						.setPositiveButton(getStr(R.string.ok), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dia, int which) {
                                System.exit(0);
							}
						})
						.setNegativeButton(getStr(R.string.cancle), null)
						.create();
					dialog.show();
				}else if(p1==command){
                    List<String[]> clist=getCommandAndNames();
                    if(clist!=null){
                        final String[] items=clist.get(0);
                        final String[] comms=clist.get(1);
                        AlertDialog dialog=new AlertDialog.Builder(BoatActivity.this)
                            .setTitle("快捷命令菜单")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    input(comms[which]);
                                }
                            })
                            .setNeutralButton("删除", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    AlertDialog dialog=new AlertDialog.Builder(BoatActivity.this)
                                        .setTitle("删除命令")
                                        .setItems(items, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dia, int which) {
                                                try {
                                                    JSONObject temp=new JSONObject(msh.getString("命令", ""));
                                                    temp.remove(items[which]);
                                                    mshe.putString("命令", temp.toString());
                                                    mshe.commit();
                                                    Toast.makeText(BoatActivity.this,"移除命令:"+items[which],Toast.LENGTH_LONG).show();
                                                } catch (JSONException e) {}
                                            }
                                        })
                                        .setPositiveButton("关闭菜单", null)
                                        .create();
                                    dialog.show();
                                }
                            })
                            .setPositiveButton("关闭菜单", null)
                            .create();
                        dialog.show();
                    }
                    
                }else if(p1==commandSetting){
                    final LinearLayout add_command=(LinearLayout)LayoutInflater.from(BoatActivity.this).inflate(R.layout.alert_add_command,null);
                    new AlertDialog.Builder(BoatActivity.this)
                        .setTitle("新增命令")//提示框标题
                        .setView(add_command)
                        .setPositiveButton(getStr(R.string.ok),//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String c_name,c_c;
                                c_name=((EditText) add_command.findViewById(R.id.ed_add_command_name)).getText().toString();
                                c_c=((EditText) add_command.findViewById(R.id.ed_add_command)).getText().toString();
                                
                                if(!c_name.equals("")&&!c_c.equals(""))
                                {
                                    try {
                                        mshe.putString("命令", new JSONObject(msh.getString("命令", "")).put(c_name, c_c).toString());
                                        mshe.commit();
                                        Toast.makeText(BoatActivity.this,"新增命令:"+c_name,Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {}
                                }
                            }
                        }).setNegativeButton(getStr(R.string.cancle),null).create().show();
                }else if(p1==destorySwitch){
                    if(点击状态){
                        点击状态=false;
                        Toast.makeText(BoatActivity.this,"触屏已切换为放置",Toast.LENGTH_LONG).show();
                    }else{
                        点击状态=true;
                        Toast.makeText(BoatActivity.this,"触屏已切换为攻击",Toast.LENGTH_LONG).show();
                    }
                }else if(p1==buttonVisible){
                    mMioCustomManager.按键显示隐藏(mioCrossingKeyboard);
                    if (openKeyboard.getVisibility()==View.VISIBLE){
                        openKeyboard.setVisibility(View.INVISIBLE);
                    }else {
                        openKeyboard.setVisibility(View.VISIBLE);
                    }
                }else if(p1==tly){
                    if(传感器开关){
                        传感器管理器.unregisterListener(传感器事件);
                        Toast.makeText(BoatActivity.this,"陀螺仪已关闭",Toast.LENGTH_LONG).show();
                        传感器开关=false;
                    }else{
                        final View al_tly=LayoutInflater.from(BoatActivity.this).inflate(R.layout.alert_tly,null);
                        AlertDialog dialog=new AlertDialog.Builder(BoatActivity.this).setTitle("陀螺仪").setView(al_tly).setPositiveButton("开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                EditText temp=al_tly.findViewById(R.id.ed_tly);
                                mshe.putInt("灵敏度",Integer.valueOf(temp.getText().toString()));
                                mshe.commit();
                                initTLY();
                                Toast.makeText(BoatActivity.this,"陀螺仪已开启",Toast.LENGTH_LONG).show();
                                传感器开关=true;
                            }
                        }).setNegativeButton("取消", null).create();
                        dialog.show();
                        
                    }
                } else if (p1 == quan) {
                    mMioCustomManager.按键显示隐藏(mioCrossingKeyboard);
                    if (!全键) {
                        mioCrossingKeyboard.setVisibility(View.INVISIBLE);
                        f1.setVisibility(View.VISIBLE);
                        f2.setVisibility(View.VISIBLE);
                        f3.setVisibility(View.VISIBLE);
                        f4.setVisibility(View.VISIBLE);
                        f5.setVisibility(View.VISIBLE);
                        f6.setVisibility(View.VISIBLE);
                        f7.setVisibility(View.VISIBLE);
                        f8.setVisibility(View.VISIBLE);
                        f9.setVisibility(View.VISIBLE);
                        f10.setVisibility(View.VISIBLE);
                        f11.setVisibility(View.VISIBLE);
                        f12.setVisibility(View.VISIBLE);
                        btq.setVisibility(View.VISIBLE);
                        btw.setVisibility(View.VISIBLE);
                        bte.setVisibility(View.VISIBLE);
                        btr.setVisibility(View.VISIBLE);
                        btt.setVisibility(View.VISIBLE);
                        bty.setVisibility(View.VISIBLE);
                        btu.setVisibility(View.VISIBLE);
                        bti.setVisibility(View.VISIBLE);
                        bto.setVisibility(View.VISIBLE);
                        btp.setVisibility(View.VISIBLE);
                        bta.setVisibility(View.VISIBLE);
                        btns.setVisibility(View.VISIBLE);
                        btd.setVisibility(View.VISIBLE);
                        btf.setVisibility(View.VISIBLE);
                        btg.setVisibility(View.VISIBLE);
                        bth.setVisibility(View.VISIBLE);
                        btj.setVisibility(View.VISIBLE);
                        btk.setVisibility(View.VISIBLE);
                        btl.setVisibility(View.VISIBLE);
                        btz.setVisibility(View.VISIBLE);
                        btx.setVisibility(View.VISIBLE);
                        btc.setVisibility(View.VISIBLE);
                        btv.setVisibility(View.VISIBLE);
                        btb.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        btm.setVisibility(View.VISIBLE);
                        space.setVisibility(View.VISIBLE);
                        btpr.setVisibility(View.VISIBLE);
                        btse.setVisibility(View.VISIBLE);
                        shift.setVisibility(View.VISIBLE);

                        全键=true;
                    } else {
                        mioCrossingKeyboard.setVisibility(View.VISIBLE);
                        f1.setVisibility(View.GONE);
                        f2.setVisibility(View.GONE);
                        f3.setVisibility(View.GONE);
                        f4.setVisibility(View.GONE);
                        f5.setVisibility(View.GONE);
                        f6.setVisibility(View.GONE);
                        f7.setVisibility(View.GONE);
                        f8.setVisibility(View.GONE);
                        f9.setVisibility(View.GONE);
                        f10.setVisibility(View.GONE);
                        f11.setVisibility(View.GONE);
                        f12.setVisibility(View.GONE);
                        btq.setVisibility(View.GONE);
                        btw.setVisibility(View.GONE);
                        bte.setVisibility(View.GONE);
                        btr.setVisibility(View.GONE);
                        btt.setVisibility(View.GONE);
                        bty.setVisibility(View.GONE);
                        btu.setVisibility(View.GONE);
                        bti.setVisibility(View.GONE);
                        bto.setVisibility(View.GONE);
                        btp.setVisibility(View.GONE);
                        bta.setVisibility(View.GONE);
                        btns.setVisibility(View.GONE);
                        btd.setVisibility(View.GONE);
                        btf.setVisibility(View.GONE);
                        btg.setVisibility(View.GONE);
                        bth.setVisibility(View.GONE);
                        btj.setVisibility(View.GONE);
                        btk.setVisibility(View.GONE);
                        btl.setVisibility(View.GONE);
                        btz.setVisibility(View.GONE);
                        btx.setVisibility(View.GONE);
                        btc.setVisibility(View.GONE);
                        btv.setVisibility(View.GONE);
                        btb.setVisibility(View.GONE);
                        btn.setVisibility(View.GONE);
                        btm.setVisibility(View.GONE);
                        space.setVisibility(View.GONE);
                        btpr.setVisibility(View.GONE);
                        btse.setVisibility(View.GONE);
                        shift.setVisibility(View.GONE);

                        全键=false;
                    }

                }else if (p1==mouseSwitch){
				    if (mouseTouchMode){
				        mouseTouchMode=false;
                        Toast.makeText(BoatActivity.this, "鼠标指针已切换为点击控制模式", Toast.LENGTH_SHORT).show();
                    }else {
				        mouseTouchMode=true;
                        Toast.makeText(BoatActivity.this, "鼠标指针已切换为滑动控制模式", Toast.LENGTH_SHORT).show();
                    }
                }else if (p1==guesture_disable){
				    if (禁用手势){
                        禁用手势=false;
                        mshe.putBoolean("禁用手势",false);
                        Toast.makeText(BoatActivity.this, "已禁用", Toast.LENGTH_SHORT).show();
                    }else {
                        禁用手势=true;
                        mshe.putBoolean("禁用手势",true);
                        Toast.makeText(BoatActivity.this, "已启用", Toast.LENGTH_SHORT).show();
                    }
				    mshe.commit();
                }
                
				mDrawerLayout.closeDrawer(Gravity.LEFT);
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
		};
		
		custom=base.findViewById(R.id.card_customBtn);
		custom.setOnClickListener(card_listener);
		exit=base.findViewById(R.id.card_exit);
		exit.setOnClickListener(card_listener);
        
        command=base.findViewById(R.id.card_command);
        command.setOnClickListener(card_listener);
        commandSetting=base.findViewById(R.id.card_commandSetting);
        commandSetting.setOnClickListener(card_listener);
        destorySwitch=base.findViewById(R.id.card_destorySwitch);
        destorySwitch.setOnClickListener(card_listener);
        buttonVisible=base.findViewById(R.id.card_buttonVisible);
        buttonVisible.setOnClickListener(card_listener);
        tly=base.findViewById(R.id.card_tly);
        tly.setOnClickListener(card_listener);
        quan=base.findViewById(R.id.card_quan);
        quan.setOnClickListener(card_listener);
        mouseSwitch=findViewById(R.id.card_mouse_switch);
        mouseSwitch.setOnClickListener(card_listener);
        guesture_disable=findViewById(R.id.card_guesture_disable);
        guesture_disable.setOnClickListener(card_listener);
	}
    float x,y;
    private long 点击时间,按下时间;
    private int cx,cy;
    private int ldx,ldy;
    private float index1_dx,index1_dy;
	private float index2_dx,index2_dy;
	private float index1_dt,index2_dt;
    private int customDX,customDY;
    private long customDT;
    OnTouchListener ot=new OnTouchListener(){
        @Override
        public boolean onTouch(View p1, MotionEvent p2)
        {
            // TODO: Implement this method
            if (mMioCustomManager.获取开关状态())
            {
                if(p2.getAction()==MotionEvent.ACTION_DOWN){
                    customDX=(int)p2.getX();
                    customDY=(int)p2.getY();
                    customDT=System.currentTimeMillis();
                }
                if(p2.getAction()==MotionEvent.ACTION_UP){
                    if(Math.abs(customDX-p2.getX())<50&&Math.abs(customDY-p2.getY())<50&&Math.abs(System.currentTimeMillis()-customDT)<250){

                    }
                }
            }else if(!mMioCustomManager.获取开关状态()){
                if (p1 == control1){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, false);

                    }
                    return false;
                }
                if (p1 == control2){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, false);

                    }
                    return false;
                }
                if (p1 == control3){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, false);

                    }
                    return false;
                }
                if (p1 == control4){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, false);

                    }
                    return false;
                }
                if (p1 == control5){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, false);

                    }
                    return false;
                }
                if (p1 == control6){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, false);

                    }
                    return false;
                }
                if (p1 == control7){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, false);

                    }
                    return false;
                }
                if (p1 == control8){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, false);

                    }
                    return false;
                }
                if (p1 == control9){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, false);

                    }
                    return false;
                }
                // TODO: Implement this method
                if (p1==miosurface){
                    if (getResources().getConfiguration().keyboard!=Configuration.KEYBOARD_NOKEYS){
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            miosurface.setFocusable(true);
                            mMioMouseKeyBoard=new MioMouseKeyboard(BoatActivity.this,mouseCursor,miosurface);
                            miosurface.requestPointerCapture();
                        }
                    }
                    if (cursorMode == BoatInput.CursorDisabled){
                        if (p2.getHistorySize()>0){
                            initialX=(int)p2.getHistoricalX(0);
                            initialY=(int)p2.getHistoricalY(0);
                        }else {

                        }
                        if (p2.getAction() == MotionEvent.ACTION_DOWN) {
                            initialX = (int)p2.getX();
                            initialY = (int)p2.getY();
                            handler.postDelayed(我跟你讲这个是长按, 700);
                            dx=(int)p2.getX();
                            dy=(int)p2.getY();
                            按下_时间=System.currentTimeMillis();
                            return true;
                        }else if (p2.getAction() == MotionEvent.ACTION_MOVE){
                            if (Math.abs(p2.getX() - dx) >= 50 || Math.abs(p2.getY() - dy) >= 50) {
                                handler.removeCallbacks(我跟你讲这个是长按);
                            }
                            baseX += ((int)p2.getX() -initialX);
                            baseY += ((int)p2.getY() - initialY);
                            Log.e("initX",initialX+"");
                            Log.e("initY",initialY+"");
                            Log.e("X",(int)p2.getX()+"");
                            Log.e("Y",(int)p2.getY()+"");
                            BoatInput.setPointer(baseX,baseY);
                            mouseCursor.setX(baseX);
                            mouseCursor.setY(baseY);
                            initialX = (int)p2.getX();
                            initialY = (int)p2.getY();
                        }else if (p2.getAction() == MotionEvent.ACTION_UP){
                            if (Math.abs(System.currentTimeMillis() - 按下_时间) <= 700) {
                                handler.removeCallbacks(我跟你讲这个是长按);
                            }
                            if (长按) {
                                if (!禁用手势) {
                                    BoatInput.setMouseButton(BoatInput.Button1, false);
                                }
                                长按 = false;
                            }
                            baseX += ((int)p2.getX() - initialX);
                            baseY += ((int)p2.getY() - initialY);
                            BoatInput.setPointer(baseX, baseY);
                            if(System.currentTimeMillis()-按下_时间<=250&&Math.abs(p2.getX()-dx)<50&&Math.abs(p2.getY()-dy)<50)
                            {
                                if (!禁用手势){
                                    if(点击状态)
                                    {
                                        BoatInput.setMouseButton(BoatInput.Button1, true);
                                        BoatInput.setMouseButton(BoatInput.Button1, false);
                                    }
                                    else
                                    {
                                        BoatInput.setMouseButton(BoatInput.Button3, true);
                                        BoatInput.setMouseButton(BoatInput.Button3, false);
                                    }
                                }

                            }
                        }

                    } else if(cursorMode == BoatInput.CursorEnabled){
                        if (!mouseTouchMode){
                            baseX = (int)p2.getX();
                            baseY = (int)p2.getY();
                            BoatInput.setPointer(baseX, baseY);
                            if (p2.getAction() == MotionEvent.ACTION_DOWN) {
                                handler.postDelayed(我跟你讲这个是长按, 150);
                                cx = (int)p2.getX();
                                cy = (int)p2.getY();
                                点击时间 = System.currentTimeMillis();
                                按下时间 = 点击时间;
                                ldx = cx;
                                ldy = cy;

                            } else if (p2.getAction() == MotionEvent.ACTION_MOVE) {
                                if (Math.abs(p2.getX() - ldx) >= 50 || Math.abs(p2.getY() - ldy) >= 50) {
                                    handler.removeCallbacks(我跟你讲这个是长按);
                                }
                                if (p2.getPointerCount()==2){
                                    BoatInput.setMouseButton(1, true);
                                }
                            } else if (p2.getAction() == MotionEvent.ACTION_UP) {
                                if (p2.getPointerCount()<2){
                                    if (System.currentTimeMillis() - 点击时间 <= 150 && Math.abs(p2.getX() - cx) < 20 && Math.abs(p2.getY() - cy) < 20) {
                                        BoatInput.setMouseButton(1, true);
                                        BoatInput.setMouseButton(1, false);
                                    }
                                }

                                if (Math.abs(System.currentTimeMillis() - 按下时间) <= 150) {
                                    handler.removeCallbacks(我跟你讲这个是长按);
                                }
                                if (长按) {
                                    BoatInput.setMouseButton(1, false);
                                    长按 = false;
                                }
                            }
                            if (p2.getActionMasked()==MotionEvent.ACTION_POINTER_UP&&p2.getPointerCount()==2){
                                BoatInput.setMouseButton(1, false);
                            }
                        }else{
                            if (p2.getAction() == MotionEvent.ACTION_DOWN) {
                                cx = (int)p2.getX();
                                cy = (int)p2.getY();
                                点击时间 = System.currentTimeMillis();
                                按下时间 = 点击时间;
                                ldx = cx;
                                ldy = cy;
                            } else if (p2.getAction() == MotionEvent.ACTION_MOVE) {
                                baseX+=p2.getX()-ldx;
                                baseY+=p2.getY()-ldy;
                                BoatInput.setPointer(baseX, baseY);
                                ldx=(int)p2.getX();
                                ldy=(int)p2.getY();
                            } else if (p2.getAction() == MotionEvent.ACTION_UP) {
                                if (System.currentTimeMillis() - 点击时间 <= 150 && Math.abs(p2.getX() - cx) < 20 && Math.abs(p2.getY() - cy) < 20) {
                                    BoatInput.setMouseButton(1, true);
                                    BoatInput.setMouseButton(1, false);
                                }
                            }
                        }

                    }
                    mouseCursor.setX(baseX);
                    mouseCursor.setY(baseY);
                    return true;
                }
                
            }
            return true;
        }
    };
//﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉
    public static void input(String str) {
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Slash, 0, true);
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Slash, 0, false);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    for (char ch:str.replace("/","").toCharArray()) {
                        BoatInput.setKey(0, ch, true);
                        BoatInput.setKey(0, ch, false);
                        Thread.sleep(50);
                    }
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
                } catch (InterruptedException e) {

                }
            }
        }).start();
    }
//﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		// TODO: Implement this method
		System.out.println("SurfaceTexture is available!");
		if (msh.getBoolean("刘海",false)){
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        }
		screenwidth=width;
		screenheight=height;
		BoatInput.width=(int)(width*msh.getFloat("分辨率缩放",1.0f));
		BoatInput.height=(int)(height*msh.getFloat("分辨率缩放",1.0f));
		BoatInput.缩放=msh.getFloat("分辨率缩放",1.0f);
		surface.setDefaultBufferSize(BoatInput.width,BoatInput.height);

        initMio();
		setMioNativeWindow(new Surface(surface));
		System.out.println("Surface is created!");
		new Thread(){
			@Override
			public void run(){
				LoadMe.exec(MioInfo.config);
//                LoadMe.openInstaller();
				Message msg=new Message();
				msg.what = -1;
				mHandler.sendMessage(msg);

			}
		}.start();
	}
	public static void setViewSize(View view, int width, int height) {  
        ViewGroup.LayoutParams params = view.getLayoutParams();  
        params.width =width;
        params.height =height;
        view.setLayoutParams(params);  
    }  
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3) {
//		Toast.makeText(BoatActivity.this,"changed",Toast.LENGTH_LONG).show();
        p1.setDefaultBufferSize(BoatInput.width,BoatInput.height);
		// TODO: Implement this method
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture p1) {
		// TODO: Implement this method
		return false;
	}
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture p1) {
        p1.setDefaultBufferSize(BoatInput.width,BoatInput.height);
	}
	public int cursorMode = BoatInput.CursorEnabled;
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BoatInput.CursorDisabled:
                    mouseCursor.setVisibility(View.INVISIBLE);
                    itemBar.setVisibility(View.VISIBLE);
                    cursorMode = BoatInput.CursorDisabled;
                    mioCrossingKeyboard.setVisibility(View.VISIBLE);
                    break;
                case BoatInput.CursorEnabled:
                    mouseCursor.setVisibility(View.VISIBLE);
                    itemBar.setVisibility(View.INVISIBLE);
                    cursorMode = BoatInput.CursorEnabled;
                    mouseCursor.setX(screenwidth/2);
                    mouseCursor.setY(screenheight/2);
                    baseX=screenwidth/2;
                    baseY=screenheight/2;
                    BoatInput.setPointer(screenwidth/2,screenheight/2);
                    mioCrossingKeyboard.setVisibility(View.INVISIBLE);
                    break;
                default:
                    finish();
                    break;
            }
        }
    }
	private MyHandler mHandler;
    public void setCursorMode(int mode){
        Message msg=new Message();
        msg.what = mode;
        mHandler.sendMessage(msg);
	}
    private Button findB(int id) {
        Button b = (Button)base.findViewById(id);
        b.setOnTouchListener(全键事件);
		return b;
    }
	@Override
	public void onClick(View p1) {
		// TODO: Implement this method
	}
	private int initialX;
	private int initialY;
	public int baseX;
	public int baseY;
	
	@Override
	public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
	{
		// TODO: Implement this method
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
		hideKeyboard();
        return false;  
	}
    private Runnable 我跟你讲这个是长按 = new Runnable(){
        @Override
        public void run() {
            长按=true;
            if (!禁用手势) {
                BoatInput.setMouseButton((byte) 1, true);
            }
            
        }
    };
    private Runnable 我跟你讲这个是点击 = new Runnable(){
        @Override
        public void run() {
            BoatInput.setMouseButton(1, true);
            BoatInput.setMouseButton(1, false);
        }
    };
	boolean pr_lock=false;
    OnTouchListener 全键事件=new OnTouchListener(){
        @Override
        public boolean onTouch(View p1, MotionEvent p2) {
            if (p1 == btq){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Q, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Q, 0,false);
                }
                return false;
            }
            if (p1 == btw){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                }
                return false;
            }
            if (p1 == bte){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,false);
                }
                return false;
            }
            if (p1 == btr){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_R, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_R, 0,false);
                }
                return false;
            }
            if (p1 == btt){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0,false);
                }
                return false;
            }
            if (p1 == bty){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Y, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Y, 0,false);
                }
                return false;
            }
            if (p1 == btu){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_U, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_U, 0,false);
                }
                return false;
            }
            if (p1 == bti){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_I, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_I, 0,false);
                }
                return false;
            }
            if (p1 == bto){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_O, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_O, 0,false);
                }
                return false;
            }
            if (p1 == btp){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_P, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_P, 0,false);
                }
                return false;
            }
            if (p1 == bta){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                }
                return false;
            }
            if (p1 == btns){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                }
                return false;
            }
            if (p1 == btd){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
                }
                return false;
            }
            if (p1 == btf){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F, 0,false);
                }
                return false;
            }
            if (p1 == btg){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_G, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_G, 0,false);
                }
                return false;
            }
            if (p1 == bth){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_H, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_H, 0,false);
                }
                return false;
            }
            if (p1 == btj){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_J, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_J, 0,false);
                }
                return false;
            }
            if (p1 == btk){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_K, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_K, 0,false);
                }
                return false;
            }
            if (p1 == btl){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_L, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_L, 0,false);
                }
                return false;
            }
            if (p1 == btz){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Z, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Z, 0,false);
                }
                return false;
            }
            if (p1 == btx){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_X, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_X, 0,false);
                }
                return false;
            }
            if (p1 == btc){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_C, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_C, 0,false);
                }
                return false;
            }
            if (p1 == btv){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_V, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_V, 0,false);
                }
                return false;
            }
            if (p1 == btb){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_B, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_B, 0,false);
                }
                return false;
            }
            if (p1 == btn){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_N, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_N, 0,false);
                }
                return false;
            }
            if (p1 == btm){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_M, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_M, 0,false);
                }
                return false;
            }
            if (p1 == f1){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0,false);
                }
                return false;
            }
            if (p1 == f2){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0,false);
                }
                return false;
            }
            if (p1 == f3){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0,false);
                }
                return false;
            }
            if (p1 == f4){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0,false);
                }
                return false;
            }
            if (p1 == f5){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0,false);
                }
                return false;
            }
            if (p1 == f6){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0,false);
                }
                return false;
            }
            if (p1 == f7){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0,false);
                }
                return false;
            }
            if (p1 == f8){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0,false);
                }
                return false;
            }
            if (p1 == f9){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0,false);
                }
                return false;
            }
            if (p1 == f10){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0,false);
                }
                return false;
            }
            if (p1 == f11){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0,false);
                }
                return false;
            }
            if (p1 == f12){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0,false);
                }
                return false;
            }
            if (p1 == space){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Space, 0,true);
                }
                else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Space, 0,false);
                }
                return false;
            }
            if (p1 == btpr){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    if(pr_lock){
                        BoatInput.setMouseButton(1, false);
                        pr_lock=false;
                    }else{
                        BoatInput.setMouseButton(1, true);
                        pr_lock=true;
                    }
                }
                return false;
            }
            if (p1 == btse){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setMouseButton(3, true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setMouseButton(3, false);
                }
                return false;
            }
            return false;
        }
    };
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO: Implement this method
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus){
			MioUtils.hideBottomMenu(this,msh.getBoolean("刘海",false));
		}
	}
    private List<String[]> getCommandAndNames(){
        try {
            String commands_str=msh.getString("命令","");
            if(commands_str.equals("")){
                mshe.putString("命令","{\"创造\":\"/gamemode 1\",\"生存\":\"/gamemode 0\",\"死亡不掉落\":\"/gamerule keepInventory true\",\"自杀\":\"/kill\",\"清空背包\":\"/clear\",\"设置世界重生点\":\"/setworldspawn\",\"白天\":\"/time set day\",\"夜晚\":\"/time set night\",\"午夜\":\"/time set midnight\",\"获取经验\":\"/xp 1000L\"}");
                mshe.commit();
                commands_str=msh.getString("命令","");
            }else{
                JSONObject commandJson=new JSONObject(commands_str);
                JSONArray commandNames=commandJson.names();
                String[] namess=new String[commandNames.length()];
                String[] commands=new String[namess.length];
                for(int i=0;i<commandNames.length();i++){
                    namess[i]=commandNames.getString(i);
                    commands[i]=commandJson.getString(namess[i]);
                }
                List<String[]> listc=new ArrayList<String[]>();
                listc.add(namess);
                listc.add(commands);
                return listc;
            }
            
        } catch (JSONException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
        return null;
    }
    private String getStr(int id){
        return getResources().getString(id);
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);

	}
}



