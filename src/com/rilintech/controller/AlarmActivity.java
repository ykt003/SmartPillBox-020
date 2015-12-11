package com.rilintech.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.receiver.AlarmReceiver;
import com.rilintech.smartpillbox_001.R;
import com.rilintech.unit.SliderRelativeLayout1;
import com.rilintech.unit.SliderRelativeLayout2;
import com.rilintech.unit.SliderRelativeLayout3;

public class AlarmActivity extends Activity implements OnClickListener {

	private String AlarmAction = "com.rilintech.controller.AlarmReceiver";
	/**
	 * 保存音乐的map
	 */
	private Map<String, Integer> map ;
	/**
	 * 音乐播放器
	 */
	private MediaPlayer alarmMusic;
	/**
	 * 系统震动
	 */
	private Vibrator mVibrator;
	/**
	 * 现在服用
	 */
	private TextView nowText;
	/**
	 * 稍后服用
	 */
	private Button shaoHou;
	/**
	 * 忽略
	 */
	private Button hulue;
	/**
	 * AlarmActivity对象
	 */
	public static AlarmActivity instance = null;
	/**
	 * 屏幕锁
	 */
	private WakeLock mWakelock;
	/**
	 * 铃音名
	 */
	private String ringStr;
	/**
	 * 获得窗体类实体
	 */
	private Window win;
	/**
	 * 药名/服用量/剂型 的控件
	 */
	private TextView medd_name, medd_num, medd_unit;
	/**
	 * 药名/服用量/剂型
	 */
	private String name, num, unit;
	/**
	 * 闹钟管理者
	 */
	private AlarmManager alarmManager;
	/**
	 * 指定启动AlarmActivity组件
	 */
	private Intent intent;
	/**
	 * 等到服务的对象
	 */
	private PendingIntent pendingIntent;
	/**
	 * 上下文管理对象
	 */
	private Context context;
	/**
	 * 响铃时获取时间点
	 */
	private long currentTime;
	/**
	 * 记录是否操作了
	 */
	private int hit = 0;
	/**
	 * 当前系统音量
	 */
	private int current;
	/**
	 * 系统最大音量
	 */
	private int max;
	/**
	 * 再次获得系统当前的音量
	 */
	private int min=-1;
	/**
	 * 音频管理器
	 */
	private AudioManager mAudioManager;
	/**
	 * 图片
	 */
	private ImageView image;
	/**
	 * 路径
	 */
	private String imageUri = "";
	
	
	public static final int MSG_LOCK_SUCESS1 = 1;
	public static final int MSG_LOCK_SUCESS2 = 2;
	public static final int MSG_LOCK_SUCESS3 = 3;
	
	private SliderRelativeLayout1 srl1;
	private SliderRelativeLayout2 srl2;
	private SliderRelativeLayout3 srl3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tree_button_alert_dialog);
		
		instance = this;
		this.context = getApplicationContext();
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

		currentTime = System.currentTimeMillis();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM )-2;
		current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
		//设置成最小音量
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
		
		getIntentData();

		setScreenWake();

		setMapData();

		setAlarm();
		
		initView();

		srl1 = (SliderRelativeLayout1) findViewById(R.id.sliderLayout1);
		srl2 = (SliderRelativeLayout2) findViewById(R.id.sliderLayout2);
		srl3 = (SliderRelativeLayout3) findViewById(R.id.sliderLayout3);
		
		srl1.setMainHandler(handler2);
		srl2.setMainHandler(handler2);
		srl3.setMainHandler(handler2);

		new Thread(new MyThread1()).start();
		new Thread(new MyThread()).start();
	}
	
	private Handler handler2 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case MSG_LOCK_SUCESS1:
				try {
					if(alarmMusic.isPlaying()){
						alarmMusic.stop();
						alarmMusic.reset();
						alarmMusic.release();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//恢复系统之前的音量
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
				mVibrator.cancel();
				hit = 1;
				AlarmActivity.this.finish();
				break;
			case MSG_LOCK_SUCESS2:
				
				try {
					if(alarmMusic.isPlaying()){
						alarmMusic.stop();
						alarmMusic.reset();
						alarmMusic.release();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//恢复系统之前的音量
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
				mVibrator.cancel();
				onStartService();
				hit = 1;
				AlarmActivity.this.finish();
				
				break;
			case MSG_LOCK_SUCESS3:
				try {
					if(alarmMusic.isPlaying()){
						alarmMusic.stop();
						alarmMusic.reset();
						alarmMusic.release();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				//恢复系统之前的音量
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
				mVibrator.cancel();
				hit = 1;
				AlarmActivity.this.finish();
				break;

			default:
				break;
			}
			
		}
	};
	
	

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				if( hit == 0){
					try {
						if(alarmMusic.isPlaying()){
							alarmMusic.stop();
							alarmMusic.reset();
							alarmMusic.release();
						}
					} catch (Exception e) {
						alarmMusic = null;
					}
					mVibrator.cancel();
					//恢复系统之前的音量
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
					AlarmActivity.this.finish();
					onStartService();
				}else{
					System.out.println("用户已经操作了");
				}
				break;
			case 0:
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, min+1, AudioManager.FLAG_PLAY_SOUND);  
				min += 1;
				System.out.println("min="+min);
				break;

			default:
				break;
			}
		};
	};
	/**
	 * 响铃计时线程（一分钟）停止响铃，并启动5分钟后再响
	 * @author ykt003
	 *
	 */
	public class MyThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(60*1000);
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MyThread1 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while (min < max) {
				
				try {
					Thread.sleep((1*1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		}
	}
	
	/**
	 * 拿到传过来的数据
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		ringStr = intent.getStringExtra("ringStr");
		name = intent.getStringExtra("medd_name");
		num = intent.getStringExtra("medd_num");
		unit = intent.getStringExtra("medd_unit");
		System.out.println("ringStr="+ringStr+"//"
				+"medd_name="+name+"//"
				+"medd_num="+num+"//"
				+"medd_unit="+unit);
		
		MedDescriptionManager descriptionManager = new MedDescriptionManager(this);
		descriptionManager.openDataBase();
		MedDescription description = new MedDescription();
		description = descriptionManager.getMedDescriptionFromName(name);
		imageUri = description.getImageUri();
		
		System.out.println("imageUri===="+imageUri);
		
		descriptionManager.closeDataBase();
		
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		
		image = (ImageView) findViewById(R.id.image);
		image.setOnClickListener(this);
		
		if(!"".equals(imageUri)){
			Options op = new Options();
			op.inJustDecodeBounds = false;
			op.inSampleSize = 30;
			Bitmap bitmap = BitmapFactory.decodeFile(imageUri, op);
			if(null != bitmap){
				image.setVisibility(View.VISIBLE);
				image.setImageBitmap(bitmap);
			}else{
				
			}
			
		}

		medd_name = (TextView) findViewById(R.id.medd_name);
		medd_num = (TextView) findViewById(R.id.medd_num);
		medd_unit = (TextView) findViewById(R.id.medd_unit);

		medd_name.setText(name);
		medd_num.setText(num);
		medd_unit.setText(unit);

		//现在服用
		nowText = (TextView)findViewById(R.id.tixinga);
		nowText.setOnClickListener(this);

		//稍后服用
		shaoHou = (Button)findViewById(R.id.shaohous);
		shaoHou.setOnClickListener(this);

		//忽略
		hulue=(Button)findViewById(R.id.hulue);
		hulue.setOnClickListener(this);
	}
	/**
	 * 设置闹铃
	 */
	private void setAlarm() {

		Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> entry = iterator.next();
			String key = entry.getKey();
			try {
				if(ringStr.equals(key)){
					alarmMusic = MediaPlayer.create(this, map.get(key));
					System.out.println("value"+map.get(key)+"//"+"key="+key);
				}
			} catch (Exception e) {
				// TODO: handle exception
				alarmMusic = MediaPlayer.create(this, R.raw.a);
			}
		}
		// 设置为循环播放
		alarmMusic.setLooping(true);
		// 获取震动
		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		//震动时长与音乐播放时长一样,放在MediaPlayer.create()之后，不然报错null object reference
		//mVibrator.vibrate(alarmMusic.getDuration());
		mVibrator.vibrate( new long[]{1000,2000,1000,2000,1000},0);
		//调节到最大音量
		//mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, AudioManager.FLAG_PLAY_SOUND);
		
		// 闹铃响
		alarmMusic.start();
	}
	
	
	/**
	 * 存入音乐数据
	 */
	private void setMapData() {

		map = new HashMap<String, Integer>();

		map.put("请服用早餐前药物", R.raw.afterbreakfaft);
		map.put("请服用早餐后药物", R.raw.beforebreakfast);
		map.put("请服用中餐前药物", R.raw.afterlunch);
		map.put("请服用中餐后药物", R.raw.beforelunch);
		map.put("请服用晚餐前药物", R.raw.aftersupper);
		map.put("请服用晚餐后药物", R.raw.beforesupper);
		map.put("请服用睡前药物", R.raw.beforesleep);
		map.put("雨的秘密轻轻告诉你", R.raw.a);
		map.put("请选择铃声", R.raw.a);
		map.put("蓝调口琴", R.raw.b);
		map.put("天空之城", R.raw.c);
		map.put("令你慢慢睁开疏松双眼", R.raw.d);
		map.put("爵士短信铃", R.raw.e);
		map.put("清脆铃声", R.raw.f);
		map.put("棉花糖版钢琴曲", R.raw.g);
		map.put("殇心吉他优美闹钟", R.raw.h);
		map.put("鸟之诗(八音盒版)", R.raw.i);
		map.put("特效音效", R.raw.j);
	}

	/**
	 * 锁屏时，可以唤醒屏幕
	 */
	private void setScreenWake() {

		win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v == image){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View view = inflater.inflate(R.layout.image_dialog, null);
			ImageView dialog_image = (ImageView) view.findViewById(R.id.dialog_iamge);
			
			if(!"".equals(imageUri)){
				Options op = new Options();
				op.inJustDecodeBounds = false;
				op.inSampleSize = 1;
				Bitmap bitmap = BitmapFactory.decodeFile(imageUri, op);
				if(null != bitmap){
					//dialog_image.setVisibility(View.VISIBLE);
					dialog_image.setImageBitmap(bitmap);
				}else{
					
				}
				
			}
			
			Dialog dialog =builder.create();
			dialog.show();
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(true);
			
		}
		
		if(v == nowText){
			if(alarmMusic.isPlaying()){
				alarmMusic.stop();
				alarmMusic.reset();
				alarmMusic.release();
			}
			//恢复系统之前的音量
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
			mVibrator.cancel();
			hit = 1;
			//startActivity(new Intent(AlarmActivity.this, HomeActivity.class));
			this.finish();
			
		}
		if(v == shaoHou){
			if(alarmMusic.isPlaying()){
				alarmMusic.stop();
				alarmMusic.reset();
				alarmMusic.release();
			}
			//恢复系统之前的音量
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
			mVibrator.cancel();
			onStartService();
			hit = 1;
			//startActivity(new Intent(AlarmActivity.this, HomeActivity.class));
			this.finish();
		}
		if(v==hulue){
			if(alarmMusic.isPlaying()){
				alarmMusic.stop();
				alarmMusic.reset();
				alarmMusic.release();
			}
			//恢复系统之前的音量
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);
			mVibrator.cancel();
			hit = 1;
			//startActivity(new Intent(AlarmActivity.this, HomeActivity.class));
			this.finish();
		}
	}

	/**
	 * (non-Javadoc) 屏蔽返回键
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(AlarmActivity.this, HomeActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//唤醒锁定屏幕
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//该方法的使用一定要伴随mWakelock.release();否则会报异常；
		//推荐使用方法：在唤醒屏幕显示的activity的onResume方法中唤醒，在onPause方法中release；
		mWakelock.release();
	}

	/**
	 * 5分钟后开启注册的与参数匹配的闹铃服务
	 */
	public void onStartService() {

		intent = new Intent(context,AlarmReceiver.class);
		intent.setAction(AlarmAction);
		intent.putExtra("ringStr", ringStr);
		intent.putExtra("medd_name", name);
		intent.putExtra("medd_num", num);
		intent.putExtra("medd_unit", unit);
		System.out.println("ringStr="+ringStr+"//"
				+"medd_name="+name+"//"
				+"medd_num="+num+"//"
				+"medd_unit="+unit);
		pendingIntent = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		int type = AlarmManager.RTC_WAKEUP;
		alarmManager.set(type, currentTime + 5*60*1000, pendingIntent);
		System.out.println("5分钟服务开启  ");
	}

}