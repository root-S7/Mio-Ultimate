package cosine.boat;


import android.app.*;
import android.view.Surface;
import java.io.File;

public class BoatInput{
	
	public static final int KeyPress              = 2;
	public static final int KeyRelease            = 3;
	public static final int ButtonPress           = 4;
	public static final int ButtonRelease	      = 5;
	public static final int MotionNotify          = 6;

	public static final int Button1               = 1;
	public static final int Button2               = 2;
	public static final int Button3               = 3;
	public static final int Button4               = 4;
	public static final int Button5               = 5;
	public static final int Button6               = 6;
	public static final int Button7               = 7;
	
	public static final int CursorEnabled         = 1;
	public static final int CursorDisabled        = 0;

	public static int width;
	public static int height;
	public static float 缩放;
	
	static {
        System.loadLibrary("boat");
    }
	
	
	public static void setMouseButton(int button, boolean press) {
        send(System.nanoTime(), press ? ButtonPress : ButtonRelease, button, 0);
    }
	public static void setPointer(int x, int y) {
        send(System.nanoTime(), MotionNotify, (int)(x*缩放), (int)(y*缩放));
    }
    
	public static void setKey(int keyCode, int keyChar, boolean press){
		send(System.nanoTime(), press ? KeyPress : KeyRelease, keyCode, keyChar);
	}
	
	
	public static native void send(long time, int type, int p1, int p2);
	
	// To be called by lwjgl/glfw.
	public static void setCursorMode(int mode){
		
		Activity activity = MyApplication.getCurrentActivity();
		if (activity instanceof BoatActivity){
			BoatActivity boatActivity = (BoatActivity)activity;
			boatActivity.setCursorMode(mode);
		}
		
	}
    
//	public static void toast(){
//		File f= new File("/storage/emulated/0/launcher/test");
//		f.mkdirs();
//	}
}
