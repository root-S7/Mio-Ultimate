package com.mio.launcher;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnCapturedPointerListener;
import android.view.MotionEvent;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import cosine.boat.BoatInput;
import cosine.boat.BoatActivity;

public class MioMouseKeyboard {
    private BoatActivity context;
	private View mouseCursor;
	private Runnable mRunnable;
	private OnCapturedPointerListener mPointerListener;
	private AndroidKeyMap mKeyMap;
	private View focusView;
    @RequiresApi(api = Build.VERSION_CODES.O)
	public MioMouseKeyboard(BoatActivity context, View mouse,View focusView){
		this.context=context;
		this.mouseCursor=mouse;
		this.focusView=focusView;
		mKeyMap=new AndroidKeyMap();
		
		mPointerListener=new OnCapturedPointerListener(){
			@Override
			public boolean onCapturedPointer(View view, MotionEvent event) {
				MioMouseKeyboard.this.context.baseX+=event.getX();
				MioMouseKeyboard.this.context.baseY+=event.getY();
				mouseCursor.setX(MioMouseKeyboard.this.context.baseX);
				mouseCursor.setY(MioMouseKeyboard.this.context.baseY);
				BoatInput.setPointer(MioMouseKeyboard.this.context.baseX,MioMouseKeyboard.this.context.baseY);
				if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS)
				{
					if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY)
					{
						BoatInput.setMouseButton(BoatInput.Button1, true);
					}
					if(event.getActionButton() == MotionEvent.BUTTON_SECONDARY||event.getActionButton() == MotionEvent.BUTTON_BACK)
					{
						BoatInput.setMouseButton(BoatInput.Button3, true);
					}
					if(event.getActionButton() == MotionEvent.BUTTON_TERTIARY){
						BoatInput.setMouseButton(BoatInput.Button2, true);
					}
				}
				if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE)
				{
					if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY)
					{
						BoatInput.setMouseButton(BoatInput.Button1, false);
					}
					if(event.getActionButton() == MotionEvent.BUTTON_SECONDARY||event.getActionButton() == MotionEvent.BUTTON_BACK)
					{
						BoatInput.setMouseButton(BoatInput.Button3, false);
					}
					if(event.getActionButton() == MotionEvent.BUTTON_TERTIARY){
						BoatInput.setMouseButton(BoatInput.Button2, false);
					}
				}
				if (event.getActionMasked()==MotionEvent.ACTION_SCROLL){
					if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){
						BoatInput.setMouseButton(5,true);
						BoatInput.setMouseButton(5,false);
					}
					else{
						BoatInput.setMouseButton(4,true);
						BoatInput.setMouseButton(4,false);
					}

				}
				return true;
			}
		};
		mRunnable=new Runnable(){
			@Override
			public void run() {
				focusView.requestPointerCapture();
			}
		};
		focusView.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(View view, int keyCode, KeyEvent event) {
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						if (keyCode==KeyEvent.KEYCODE_ENTER){
							context.showKeyboard();
						}else{
							BoatInput.setKey(mKeyMap.translate(keyCode), 0, true);
						}
					}else if(event.getAction()==MotionEvent.ACTION_UP){
						if (keyCode==KeyEvent.KEYCODE_ENTER){
//							context.showKeyboard();
						}else{
							BoatInput.setKey(mKeyMap.translate(keyCode), 0, false);
						}
					}
					return false;
				}
			});
		focusView.setOnCapturedPointerListener(mPointerListener);
	}
	
	
    public void catchPointer(){
		focusView.requestFocus();
		focusView.postDelayed(mRunnable,300);
	}
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void releasePointer(){
		focusView.releasePointerCapture();
	}
}
