package com.ols.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ProgressView extends ImageView {
	private int[] ImagesId;
	private Timer timer;
	private int timeDelay = 300;
	private int count = 0;
	private int animModel = 1;
	private RotateAnimation rotateAnimation;
	private Paint textPaint;
	private Bitmap bitMap;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setImageResource(ImagesId[msg.what]);
			super.handleMessage(msg);
		}
	};
	private TimerTask switchProgressImage = new TimerTask() {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(count);
			count++;
			if (count > ImagesId.length)
				count = 0;
		}
	};

	public ProgressView(Context context) {
		super(context);
		Log.d("ProgressView", "1");
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("ProgressView", "2");
		textPaint=new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(15);
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d("ProgressView", "3");
	}

	public void setImagesId(int[] imagesId) {
		ImagesId = imagesId;
	}

	public int getTimeDelay() {
		return timeDelay;
	}

	public void setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility == View.VISIBLE) {
			Log.d("setVisibility", "visibility=" + visibility);
			Log.d("setVisibility", "View.VISIBLE=" + View.VISIBLE);
			startAnimation();
		} else {
			Log.d("setVisibility", "visibility=" + visibility);
			Log.d("setVisibility", "View.INVISIBLE=" + View.INVISIBLE);
			stopAnimation();
		}
		super.setVisibility(visibility);
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
	}

	public void setAnimModel(int model) {
		this.animModel = model;
	}

	public void initRotateAnimation() {
		rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setRepeatMode(Animation.RESTART);
		rotateAnimation.setDuration(1000);
	}

	public void startAnimation() {
		Log.d("ProgressView", "startAnimation");
		switch (animModel) {
		case 1:
			initRotateAnimation();
			this.startAnimation(rotateAnimation);
			break;
		case 2:
			timer = new Timer();
			timer.schedule(switchProgressImage, 0, timeDelay);
			break;
		default:
			break;
		}
	}

	public void stopAnimation() {
		Log.d("ProgressView", "stopAnimation");
		switch (animModel) {
		case 1:
			this.clearAnimation();
			break;
		case 2:
			if(timer!=null)
				timer.cancel();
			break;
		default:
			break;
		}
	}
}
