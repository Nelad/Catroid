package com.tugraz.android.app.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tugraz.android.app.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

/**
 * 
 * The StageViewThread which executes the drawing of the stage.
 * 
 * @author Thomas Holzmann
 *
 */
public class StageViewThread extends Thread {
	public boolean mIsDraw = false;
	
	private boolean mRun = false;
	private SurfaceHolder mSurfaceHolder;
	private Context context;
	private Bitmap mBackground = null;
	private Map<String, Pair<Bitmap,Pair<Float,Float>>> mBitmapToPositionMap;
	

	public StageViewThread(SurfaceHolder holder, Context context,
			Handler handler) {
		mSurfaceHolder = holder;
		this.context = context;
		this.setName("StageViewThread");

		mBitmapToPositionMap =  Collections.synchronizedMap(new HashMap<String, Pair<Bitmap,Pair<Float,Float>>>());

	}

	public synchronized void setRunning(boolean b) {
		mRun = b;
	}
	
	public void setBackground(String path) {
		mIsDraw = false;
		//synchronized (mBackground){ //TODO synchronisieren!!
			mBackground = BitmapFactory.decodeFile(path);
		//}
		mIsDraw = true;
	}
	
	public void addBitmapToDraw(String spriteName, String path, float x, float y) {
		Pair<Float,Float> coordinates = new Pair<Float,Float>(x,y);
		Pair<Bitmap,Pair<Float,Float>> bitmapPair = new Pair<Bitmap,Pair<Float,Float>>(BitmapFactory.decodeFile(path), coordinates);
		mIsDraw = false;  //TODO brauchen wir das ueberall??
		mBitmapToPositionMap.put(spriteName, bitmapPair);
		mIsDraw = true;
	}
	
	public void removeBitmapToDraw(String spriteName) { 
		mIsDraw = false;
		mBitmapToPositionMap.remove(spriteName);
		mIsDraw = true;
	}
	
	public void changeBitmapPosition(String spriteName, float x, float y) {
		if (!mBitmapToPositionMap.containsKey(spriteName))
			return;
		Pair<Float,Float> newCoordinates = new Pair<Float,Float>(x,y);
		Bitmap bitmap = mBitmapToPositionMap.get(spriteName).first;
		Pair <Bitmap,Pair<Float,Float>> bitmapAndPosition = new Pair <Bitmap,Pair<Float,Float>>(bitmap,newCoordinates);
		mIsDraw = false;
		mBitmapToPositionMap.remove(spriteName);
		mBitmapToPositionMap.put(spriteName, bitmapAndPosition);
		mIsDraw = true;
		
	}
	
	public synchronized boolean isRunning(){
		return mRun;
	}

	public void run() {
		while (mRun) {
			Canvas c = null;
			if (mIsDraw) {
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {

						doDraw(c);
					}
				} finally {
					if (c != null)
						mSurfaceHolder.unlockCanvasAndPost(c);
					//throw new ThreadDeath();
				}
			}

		}
	}

	
	/**
	 * Draws the stage.
	 */
	protected synchronized void doDraw(Canvas canvas) {
		Paint paint = new Paint();

//		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//				   R.drawable.icon);
			
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		// canvas.drawRect(new Rect(mX + 0, mY + 0, mX + 40, mY + 40), paint);
		canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
				paint);

		if (mBackground != null)
			//synchronized (mBackground){ //TODO synchronisieren!!
				canvas.drawBitmap(mBackground, 0, 0, null);
			//}
		
		Iterator<String> keyIterator = mBitmapToPositionMap.keySet().iterator();
		for (int i=0; i<mBitmapToPositionMap.size(); i++) {
			Pair<Bitmap, Pair<Float, Float>> bitmapPair = mBitmapToPositionMap.get(keyIterator.next()); 
			canvas.drawBitmap(bitmapPair.first, bitmapPair.second.first, bitmapPair.second.second, null);
		}
		mIsDraw = false;

	}

}
