package com.mio.mclauncher.customcontrol;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
public class MioCustomUtils
{
	public static void setViewSize(Context context,View view, String height, String width) {  
        ViewGroup.LayoutParams params = view.getLayoutParams();  
        params.width = dip2px(context, Integer.parseInt(width));
        params.height = dip2px(context, Integer.parseInt(height));
        view.setLayoutParams(params);  
    } 
	public static void setViewSize(Context context,View view, int height, int width) {  
        ViewGroup.LayoutParams params = view.getLayoutParams();  
        params.width = dip2px(context, (width));
        params.height = dip2px(context, (height));
        view.setLayoutParams(params);  
    }  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
	public static int percentTopx(double percent, int screen) {
        return (int) (((double) screen) * percent);
    }

    public static double pxToPercent(double px, int screen) {
        return px / screen;
    }
	
	
}
