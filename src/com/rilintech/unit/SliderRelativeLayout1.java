package com.rilintech.unit;

import com.rilintech.controller.AlarmActivity;
import com.rilintech.smartpillbox_001.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SliderRelativeLayout1 extends RelativeLayout {
	private final static String TAG = "SliderRelativeLayout";
	private Context context;
	private Bitmap dragBitmap = null; //拖拽图片
	private int locationX = 0; //bitmap初始绘图位置，足够大，可以认为看不见
	private ImageView heartView = null; //主要是获取相对布局的高度
	private ImageView leftRingView = null;
	private ImageView rightRingView = null;
	private Handler handler = null; //信息传递
	private static int BACK_DURATION = 10 ;   // 回退动画时间间隔值  20ms
	private static float VE_HORIZONTAL = 0.9f ;  // 水平方向前进速率 0.1dip/ms
	
	public SliderRelativeLayout1(Context context) {
		super(context); 
		SliderRelativeLayout1.this.context = context;
		intiDragBitmap();
	}
	
	public SliderRelativeLayout1(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		SliderRelativeLayout1.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout1(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		SliderRelativeLayout1.this.context = context;
		intiDragBitmap();
	}

	/**
	 * 得到拖拽图片
	 */
	private void intiDragBitmap() {
		if(dragBitmap == null){
			dragBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.love);
		}
	}
	
	/**
	 * 这个方法里可以得到一个其他资源
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		heartView = (ImageView) findViewById(R.id.loveView1);
		leftRingView = (ImageView) findViewById(R.id.leftRing1);
		rightRingView = (ImageView) findViewById(R.id.rightRing1);
	}

	/**
	 * 对拖拽图片不同的点击事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X = (int) event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			locationX = (int) event.getX();
			Log.i(TAG, "是否点击到位=" + isActionDown(event));
			return isActionDown(event);//判断是否点击了滑动区域
			
		case MotionEvent.ACTION_MOVE: //保存x轴方向，绘制图画
			locationX = X;
			invalidate(); //重新绘图
			return true;
			
		case MotionEvent.ACTION_UP: //判断是否解锁成功
			if(!isLocked()){ //没有解锁成功,动画应该回退
				handleActionUpEvent(event); //动画回退
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * 回退动画
	 * @param event
	 */
	private void handleActionUpEvent(MotionEvent event) {
		int x = (int) event.getX();
		int toLeft = leftRingView.getWidth();
		
		locationX = x - toLeft;
		if(locationX >= 0){
			handler.postDelayed(ImageBack, BACK_DURATION); //回退
		}
	}

	/**
	 * 未解锁时，图片回退
	 */
	private Runnable ImageBack = new Runnable() {
		@Override
		public void run() {
			locationX = locationX - (int) (VE_HORIZONTAL*BACK_DURATION);
			if(locationX >= 0){
				handler.postDelayed(ImageBack, BACK_DURATION); //回退
				invalidate();
			}
		}
	};
	
	/**
	 * 判断是否点击到了滑动区域
	 * @param event
	 * @return
	 */
	private boolean isActionDown(MotionEvent event) {
		Rect rect = new Rect();
		heartView.getHitRect(rect);
		//boolean isIn = rect.contains((int)event.getX()-heartView.getWidth(), (int)event.getY());
		boolean isIn = rect.contains((int)event.getX(), (int)event.getY());
		if(isIn){
			heartView.setVisibility(View.GONE);
			return true;
		}
		return false;
	}
	
	/**
	 * 绘制拖动时的图片
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		invalidateDragImg(canvas);
	}
	
	/**
	 * 图片随手势移动
	 * @param canvas
	 */
	private void invalidateDragImg(Canvas canvas) { 
		int drawX = locationX - dragBitmap.getWidth();
		//int drawY = heartView.getTop();
		int drawY = heartView.getHeight()/4;
		
		if(drawX < leftRingView.getWidth()){ //划到最左边
			heartView.setVisibility(View.VISIBLE);
			return;
		} else {
			if(isLocked()){ //判断是否成功
				return;
			}
			heartView.setVisibility(View.GONE);
			canvas.drawBitmap(dragBitmap, drawX < 0 ? 5 : drawX,drawY,null);
		}
	}
	
	/**
	 * 判断是否解锁
	 */
	private boolean isLocked(){
		if(locationX > (getScreenWidth() - rightRingView.getWidth()-60)){
			handler.obtainMessage(AlarmActivity.MSG_LOCK_SUCESS1).sendToTarget();
			return true;
		}
		return false;
	}
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	private int getScreenWidth(){
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		return width;
	}
	
	/**
	 * 与主activity通信
	 * @param handler
	 */
	public void setMainHandler(Handler handler){
		this.handler = handler;
	}
}
