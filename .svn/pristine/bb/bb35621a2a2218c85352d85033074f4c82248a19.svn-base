package com.rilintech.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.manager.RequsetCodeManager;
import com.rilintech.model.MedDescription;
import com.rilintech.model.RequestCode;
import com.rilintech.receiver.AlarmReceiver;
import com.rilintech.smartpillbox_001.R;
import com.rilintech.unit.MedDescriptionDeleteDialogActivity;

@SuppressLint("ResourceAsColor")
public class MedDescriptionDetailActivity2 extends Activity implements OnClickListener, OnLongClickListener {

	private String AlarmAction = "com.rilintech.controller.AlarmReceiver"; 
	private RequsetCodeManager requsetCodeManager;
	private RequestCode requestCode ;
	private List<RequestCode> listCode;
	/**
	 * 用来保存闹铃响应吗的map
	 */
	private Map<String, Integer> mapCode = new HashMap<String, Integer>();
	/**
	 * 每次动态添加时间时，都会生成对应的响应码
	 */
	private int part= 0 ;
	/**
	 * 用来保存现在时间到设定时间的时间差
	 */
	private Map<String, Long> mapTime = new HashMap<String, Long>();
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
	 * 间隔时间
	 */
	private Long triggerAtMillis;
	/**
	 * 闹钟类型
	 */
	private int type;
	/**
	 * 响铃重复类型,默认0开启重复
	 */
	private int repeat = 0;
	/**
	 * 药名
	 */
	private EditText nameEdit;
	/**
	 * 服用量
	 */
	private EditText  measureEdit;
	/**
	 * 药品剂型
	 */
	private TextView usageText;
	/**
	 * 重复
	 */
	private TextView  intervalText;
	/**
	 * 铃声类型
	 */
	private TextView  ringText;
	/**
	 * 服务开启状态码，默认1是开启
	 */
	private int code = 1;
	/**
	 * 保存
	 */
	private Button submit;
	/**
	 * 取消
	 */
	private Button  cancel;

	private ImageView back, delete, clock_on;
	private Dialog dialog2;
	private LinearLayout addTime, layouts, usageImage, interval_layout,ringLayout;
	private int i = 0;

	//闹铃开关的标记
	private boolean flag = false;

	//开始时间
	private String timeStr;
	//中间变量
	private String timeStr3;
	/**
	 * 药的ID
	 */
	private String medd_id;
	/**
	 * 请求码数组
	 */
	private String [] request = null;
	/**
	 * 当前修改的药名
	 */
	private String med_name;
	/**
	 * 备注图片
	 */
	private ImageView image;
	/**
	 * 点击添加
	 */
	private TextView text;
	/**
	 * 备注图片的URI
	 */
	private String imageUri = "";

	private Context context;

	/**
	 * 当日提醒传的值
	 */
	private int gg;

	public static MedDescriptionDetailActivity2 instance;
	//月
	private String monthStr=null;
	//日
	private String dayStr=null;
	//年月日
	private String date=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drug_edit);
		this.context = getApplicationContext();

		ExitActivity.getExitActivity().addActivity(this);

		alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		instance = this;

		Intent intent = getIntent();
		medd_id = intent.getStringExtra("medd_id");
		gg = intent.getIntExtra("gg", 0);
		System.out.println("gggggggggggggg====="+gg);

		init();
		setDate();
		/*
		 * 初始赋值
		 */
		MedDescription medd = new MedDescription();
		MedDescriptionManager meddManager = new MedDescriptionManager(
				getApplicationContext());
		meddManager.openDataBase();
		medd = meddManager.medDescription(medd_id);
		meddManager.closeDataBase();
		nameEdit.setText(medd.getName());
		med_name = medd.getName();
		measureEdit.setText(medd.getMensure());
		measureEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		intervalText.setText(medd.getInterval());
		usageText.setText(medd.getUsage());
		ringText.setText(medd.getRingName());
		imageUri = medd.getImageUri();

		if(!"".equals(imageUri)){
			text.setVisibility(View.GONE);
			image.setVisibility(View.VISIBLE);
			if(null != setImage(imageUri)){
				image.setImageBitmap(setImage(imageUri));
			}else{
				image.setImageResource(R.drawable.imgbg);
			}
		}

		code=medd.getFlag();
		if (medd.getFlag() == 0) {
			clock_on.setImageResource(R.drawable.y_off);
		} else {
			clock_on.setImageResource(R.drawable.y_on);
		}
		//请求码
		String code = medd.getRequestCode();
		if(code != null){
			request = code.split(",");
		}

		// 开始时间
		if (medd.getTime() != null) {
			String[] timeArr = medd.getTime().split(",");
			for(int i = 0;i<timeArr.length;i++){
				final View view = this.getLayoutInflater().inflate(
						R.layout.time_add, null);
				//将时间通过冒号分割成小时和分钟
				String []t = timeArr[i].split(":");

				timeView(timeArr[i]);
				//final TextView text = (TextView) view.findViewById(R.id.text_time);
				//text.setText(timeArr[i]);

				//layouts.addView(view);

				try {
					//初始化map,并放入相应的值
					mapCode.put(timeArr[i],Integer.parseInt(request[i]));
					mapTime.put(timeArr[i], getTime(t));
				} catch (Exception e) {
					System.out.println("request是空null");
				}
			}
		}
	}

	/**
	 * 获得图片
	 * @param imageUri
	 * @return
	 */
	private Bitmap setImage(String imageUri){

		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(imageUri, options); 
		 
		return bitmap;
	}

	/*
	 * 设置日期
	 */
	private  void setDate(){

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);



		if(month<10){
			monthStr = String.valueOf("0"+month);
		}else{
			monthStr=String.valueOf(month);
		}
		if(day<10){
			dayStr = String.valueOf("0"+day);
		}else{
			dayStr =String.valueOf(day);
		}	

		date=year+"-"+monthStr+"-"+dayStr;
	}

	/**
	 * 重新获得现在时间到设定时间的时间差
	 */
	private long getTime(final String[] t) {
		long systemTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MINUTE,Integer.parseInt(t[1]));
		calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(t[0]));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		// 选择的每天定时时间
		long selectTime = calendar.getTimeInMillis();	
		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
		if(systemTime > selectTime) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			selectTime = calendar.getTimeInMillis();
		}
		// 计算现在时间到设定时间的时间差
		long time = selectTime - systemTime;

		return time;
	}

	private void init() {

		image= (ImageView) findViewById(R.id.image);
		text = (TextView) findViewById(R.id.text);
		text.setOnClickListener(this);
		image.setOnClickListener(this);
		image.setOnLongClickListener(this);

		back = (ImageView) findViewById(R.id.second_imageView1);
		delete = (ImageView) findViewById(R.id.second_imageView2);
		back.setOnClickListener(this);
		delete.setOnClickListener(this);

		submit = (Button) findViewById(R.id.btnlead2backs);
		cancel = (Button) findViewById(R.id.btnlead2nexts);
		measureEdit = (EditText) findViewById(R.id.drug_doses);
		layouts = (LinearLayout) findViewById(R.id.time_add);
		nameEdit = (EditText) findViewById(R.id.drug_name);
		addTime = (LinearLayout) findViewById(R.id.addtime);
		addTime.setOnClickListener(this);
		//药品类型
		usageImage = (LinearLayout) findViewById(R.id.drug_names);
		usageText = (TextView) findViewById(R.id.y_drugclasss);
		usageImage.setOnClickListener(this);
		//重复间隔
		interval_layout = (LinearLayout) findViewById(R.id.interval_layout);
		intervalText = (TextView) findViewById(R.id.ys_drugclass);
		interval_layout.setOnClickListener(this);
		//铃声
		ringLayout = (LinearLayout) findViewById(R.id.ring_name);
		ringText = (TextView) findViewById(R.id.ringNtext);
		ringLayout.setOnClickListener(this);

		//闹铃开关
		clock_on = (ImageView) findViewById(R.id.clock_on);
		clock_on.setOnClickListener(this);

		// 保存
		submit.setOnClickListener(this);
		// 取消
		cancel.setOnClickListener(this);
	}

	/**
	 * 开启注册的与参数匹配的闹铃服务
	 */
	public void onStartService(int uuid, long time, int repeat) {

		intent = new Intent(context,AlarmReceiver.class);
		intent.setAction(AlarmAction);
		intent.putExtra("ringStr", ringText.getText().toString());
		intent.putExtra("medd_name", nameEdit.getText().toString());
		intent.putExtra("medd_num", measureEdit.getText().toString());
		intent.putExtra("medd_unit", usageText.getText().toString());
		System.out.println("当前选择的铃音==="+ringText.getText().toString());
		System.out.println("medd_name="+nameEdit.getText().toString()+"//"
				+"medd_num="+measureEdit.getText().toString()+"//"
				+"medd_unit="+usageText.getText().toString());
		pendingIntent = PendingIntent.getBroadcast(
				context, uuid, intent, 0);
		//triggerAtMillis = System.currentTimeMillis() + 15 * 1000;
		triggerAtMillis = time;
		type = AlarmManager.RTC_WAKEUP;
		if(repeat == 1){
			alarmManager.set(type, System.currentTimeMillis()+triggerAtMillis, pendingIntent);
			System.out.println("一次提醒服务开启 repeat = "+repeat);
		}else {
			alarmManager.setRepeating(type, System.currentTimeMillis()+triggerAtMillis, 24*60*60*1000, pendingIntent);
			System.out.println("每天提醒服务开启 repeat = "+repeat);
		}

	}
	/**
	 * 取消已经注册的与参数匹配的闹铃
	 */
	public void onStopService(int uuid){

		intent = new Intent(context,AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(
				context, uuid, intent, 0);
		alarmManager.cancel(pendingIntent);
	}
	/**
	 * 点击保存时，判断是否开启服务
	 */
	private void setService() {
		if(code==0 ){
			List<Integer> list = new ArrayList<Integer>();
			for(String key : mapCode.keySet()){
				list.add(mapCode.get(key));
			}
			for (int i:list){
				onStopService(i);
			}
		}else {
			List<Integer> list = new ArrayList<Integer>();
			for(String key : mapCode.keySet()){
				list.add(mapCode.get(key));
			}
			List<Long> list1 = new ArrayList<Long>();
			for(String key : mapTime.keySet()){
				list1.add(mapTime.get(key));
			}
			for(int i=0;i< list.size();i++){

				onStartService(list.get(i),list1.get(i),repeat);

				System.out.println("请求码requestCode="+list.get(i));
				System.out.println("距离响铃时间listTime="+list1.get(i));
			}
			System.out.println("服务开启状态code="+code+"//"+"设置界面闹钟开关flag="+flag+"//"+"重复响铃repeat="+repeat);
		}
	}

	@Override
	public void onClick(View v) {

		//点击添加备注
		if( v == text) {
			//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent,1);
		}
		if(v == image){
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent,1);
		}


		//返回
		if (v == back) {
			if( gg == 1){
				Intent intent = new Intent(this, TodayRemindActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}else{
				Intent intent = new Intent(this, MedDescriptionListActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}
		}
		//添加界面的删除
		if (v == delete) {
			MedDescriptionDeleteDialogActivity.showDialog_Layout(this, medd_id,gg);
		}

		if (v == clock_on) {
			//闹铃关
			if (code == 1) {
				code = 0;
				flag = true;
				clock_on.setImageResource(R.drawable.y_off);
				if(repeat == 0){
					System.out.println("repeat="+repeat+"//"+"code="+code);
					Toast.makeText(MedDescriptionDetailActivity2.this, "每天服务已关闭",Toast.LENGTH_SHORT).show();
				}else{
					System.out.println("repeat="+repeat+"//"+"code="+code);
					Toast.makeText(MedDescriptionDetailActivity2.this, "一次服务已关闭",Toast.LENGTH_SHORT).show();
				}
				//闹铃关
			}else{
				code = 1;
				flag = false;
				clock_on.setImageResource(R.drawable.y_on);
				//startService(new Intent(MedDescriptionAddActivity.this, AlarmService.class));
				if(repeat == 0){
					System.out.println("repeat="+repeat+"//"+"code="+code);
					Toast.makeText(MedDescriptionDetailActivity2.this, "每天服务已启动",Toast.LENGTH_SHORT).show();
				}else{
					System.out.println("repeat="+repeat+"//"+"code="+code);
					Toast.makeText(MedDescriptionDetailActivity2.this, "一次服务已启动",Toast.LENGTH_SHORT).show();
				}
			}
		}

		// 添加时间
		if (v == addTime) {
			i++;
			for (int j = 1; j <= i; j++) {
				if (j == i) {

					final View view = this.getLayoutInflater().inflate(
							R.layout.time_add, null);
					final TextView text = (TextView) view.findViewById(R.id.text_time);
					text.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//点击textview时先判断是新添加还是更改
							String s = text.getText().toString();
							System.out.println("sssss="+s);
							if(s.length()==0){
								System.out.println("textview空，新添加时间");
							}else{
								System.out.println("textview="+s+"修改时间");
								deleteMap(text);
							}
							showDialog_Layout2(MedDescriptionDetailActivity2.this,text,view);
						}
					});
					layouts.addView(view);

					//开始时间      后面的减号-
					ImageView cancel = (ImageView) view.findViewById(R.id.drug_namese);
					cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							layouts.removeView(view);
							/*if (timeStr != null) {
								System.out.println("+++++++++++++++++++"+ timeStr);
								String[] strArr = timeStr.split(",");

								List<String> list = new ArrayList<String>(
										Arrays.asList(strArr));
								//迭代器遍历list集合
								Iterator<String> iterator = list.iterator();
								while(iterator.hasNext()){
									String str = (String) iterator.next();
									if (str.equals(text.getText().toString())) {
										iterator.remove();
									}
								}
								for (String s : list) {
									if (timeStr3 == null) {
										timeStr3 = s;
									} else {
										timeStr3 = timeStr3 + "," + s;
									}
								}
								timeStr = timeStr3;
								System.out.println("000000000000000000000"+ timeStr);
							}*/
							if(mapCode.size() != 0){
								//迭代map，将要删除的时间对应的part也删除
								Iterator<Map.Entry<String, Integer>> iterator = mapCode.entrySet().iterator();
								while(iterator.hasNext()){
									Map.Entry<String, Integer> entry = iterator.next();
									String key = entry.getKey();
									if(text.getText().toString().equals(key)){
										iterator.remove();
										System.out.println("delete this: "+key+" = "+key);
									}
								}
								for(String key:mapCode.keySet()){
									System.out.println("value="+mapCode.get(key));
								}
							}
							if(mapTime.size() != 0){
								//迭代map，将要删除的时间对应的时间差也删除
								Iterator<Map.Entry<String, Long>> iterator = mapTime.entrySet().iterator();
								while(iterator.hasNext()){
									Map.Entry<String, Long> entry = iterator.next();
									String key = entry.getKey();
									if(text.getText().toString().equals(key)){
										iterator.remove();
										System.out.println("delete this: "+key+" = "+key);
									}
								}
								for(String key:mapTime.keySet()){
									System.out.println("value="+mapTime.get(key));
								}
							}
						}
					});

				}
			}

		}
		//药品类型
		if (v == usageImage) {
			showDialog_Layout3(MedDescriptionDetailActivity2.this, usageText);
		}
		//重复间隔
		if (v == interval_layout) {
			showDialog_Layout4(MedDescriptionDetailActivity2.this, intervalText);
		}
		//铃声
		if (v == ringLayout) {

			Intent intent = new Intent(this, RingSelectedActivity.class);
			startActivityForResult(intent, 0);
		}

		if (v == submit) {
			MedDescriptionManager meddManager = new MedDescriptionManager(
					getApplicationContext());
			meddManager.openDataBase();
			List<MedDescription> medds = meddManager.getMedDscriptions();
			meddManager.closeDataBase();
			List<String> nameList = new ArrayList<String>();
			for(MedDescription medd : medds){
				nameList.add(medd.getName());
			}
			Iterator<String> it = nameList.iterator();
			while(it.hasNext()){
				if(it.next().equals(med_name)){
					it.remove();
				}
			}

			if(nameEdit.getText().toString().equals("")){
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.tone_button_alert_dialog, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				//builder.setView(view);
				builder.setIcon(R.drawable.ic_launcher);
				final Dialog dialog = builder.show();
				dialog.getWindow().setContentView(view);
				Button button = (Button) view.findViewById(R.id.alertbtn1);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();	
					}
				});
			}else if(nameList.contains(nameEdit.getText().toString())){
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.tone_button_alert_dialog2, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				//builder.setView(view);
				builder.setIcon(R.drawable.ic_launcher);
				final Dialog dialog = builder.show();
				dialog.getWindow().setContentView(view);
				Button button = (Button) view.findViewById(R.id.alertbtn1);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();	
					}
				});
			}else if(mapTime.size() == 0){
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.tone_button_alert_dialog3, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				//builder.setView(view);
				builder.setIcon(R.drawable.ic_launcher);
				final Dialog dialog = builder.show();
				dialog.getWindow().setContentView(view);
				Button button = (Button) view.findViewById(R.id.alertbtn1);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();	
					}
				});
			}else{

				MedDescription medd = new MedDescription();

				medd.setName(nameEdit.getText().toString());
				medd.setInterval(intervalText.getText().toString());
				medd.setMensure(measureEdit.getText().toString());
				medd.setRingName(ringText.getText().toString());
				medd.setUsage(usageText.getText().toString());
				medd.setRepeat(repeat);
				medd.setFlag(code);
				medd.setDate(date);
				medd.setImageUri(imageUri);
				saveTime(medd);
				saveRequestCode(medd);

				/*if (timeStr != null) {
				// 获取到用药时间的时间集合
				//去掉重复
				String[] strArr = timeStr.split(",");
				Set<String> set = new HashSet<String>();
				set.addAll(Arrays.asList(strArr));
				String[] storeStr = set.toArray(new String[set.size()]);
				String timeStr2 = null;

				for (String s : storeStr) {
					timeStr2 = timeStr2 + "," + s;
				}
				//去掉null, 
				if (timeStr2.length() > 4) {
					timeStr2 = timeStr2.substring(5);
				}
				timeStr = timeStr2;
				System.out.println("------------"+ timeStr);
			}
			medd.setTime(timeStr);*/

				MedDescriptionManager meddManager1 = new MedDescriptionManager(
						getApplicationContext());
				meddManager1.openDataBase();
				meddManager1.updateMedDescription(medd, medd_id);
				meddManager1.closeDataBase();

				//停掉之前的服务
				try {
					if(request.length !=0){
						for(String code : request){
							onStopService(Integer.parseInt(code));
							onStopService(0);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}


				setService();

				if( gg == 1){
					Intent intent = new Intent(getApplicationContext(),
							TodayRemindActivity.class);
					startActivity(intent);
					MedDescriptionDetailActivity2.this.finish();
				}else{
					Intent intent = new Intent(getApplicationContext(),
							MedDescriptionListActivity.class);
					startActivity(intent);
					MedDescriptionDetailActivity2.this.finish();
				}

			}

		}
		if (v == cancel) {
			if( gg ==1){
				Intent intent = new Intent(this, TodayRemindActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}else{
				Intent intent = new Intent(this, MedDescriptionListActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}
		}

	}
	private void timeView(String t) {
		final View view = this.getLayoutInflater().inflate(
				R.layout.time_add, null);
		final TextView text = (TextView) view.findViewById(R.id.text_time);
		text.setText(t);
		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击textview时先判断是新添加还是更改
				String s = text.getText().toString();
				System.out.println("sssss="+s);
				if(s.length()==0){
					System.out.println("textview空，新添加时间");
				}else{
					System.out.println("textview="+s+"修改时间");
					deleteMap(text);
				}
				showDialog_Layout2(MedDescriptionDetailActivity2.this,text,view);
			}
		});
		layouts.addView(view);

		//开始时间      后面的减号-
		ImageView cancel = (ImageView) view.findViewById(R.id.drug_namese);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layouts.removeView(view);
				/*if (timeStr != null) {
					System.out.println("+++++++++++++++++++"+ timeStr);
					String[] strArr = timeStr.split(",");

					List<String> list = new ArrayList<String>(
							Arrays.asList(strArr));
					//迭代器遍历list集合
					Iterator<String> iterator = list.iterator();
					while(iterator.hasNext()){
						String str = (String) iterator.next();
						if (str.equals(text.getText().toString())) {
							iterator.remove();
						}
					}
					for (String s : list) {
						if (timeStr3 == null) {
							timeStr3 = s;
						} else {
							timeStr3 = timeStr3 + "," + s;
						}
					}
					timeStr = timeStr3;
					System.out.println("000000000000000000000"+ timeStr);
				}*/
				if(mapCode.size() != 0){
					//迭代map，将要删除的时间对应的part也删除
					Iterator<Map.Entry<String, Integer>> iterator = mapCode.entrySet().iterator();
					while(iterator.hasNext()){
						Map.Entry<String, Integer> entry = iterator.next();
						String key = entry.getKey();
						if(text.getText().toString().equals(key)){
							iterator.remove();
							System.out.println("delete this: "+key+" = "+key);
						}
					}
					for(String key:mapCode.keySet()){
						System.out.println("value="+mapCode.get(key));
					}
				}
				if(mapTime.size() != 0){
					//迭代map，将要删除的时间对应的时间差也删除
					Iterator<Map.Entry<String, Long>> iterator = mapTime.entrySet().iterator();
					while(iterator.hasNext()){
						Map.Entry<String, Long> entry = iterator.next();
						String key = entry.getKey();
						if(text.getText().toString().equals(key)){
							iterator.remove();
							System.out.println("delete this: "+key+" = "+key);
						}
					}
					for(String key:mapTime.keySet()){
						System.out.println("value="+mapTime.get(key));
					}
				}
			}
		});
	}

	/**
	 * 保存RequestCode
	 * @param medd
	 */
	private void saveRequestCode(MedDescription medd) {
		List<Integer> list2 = new ArrayList<Integer>();
		for(String key : mapCode.keySet()){
			list2.add(mapCode.get(key));
		}
		if(list2.size() != 0){
			String requestCode = null;
			for(int i = 0;i<list2.size();i++){
				if(i == 0){
					requestCode = list2.get(0).toString();
				}else{
					requestCode = requestCode+ "," + list2.get(i);
				}
			}
			System.out.println("响应吗requestCode=============="+ requestCode);
			medd.setRequestCode(requestCode);
		}
	}
	/**
	 * 保存时间点
	 * @param medd
	 */
	private void saveTime(MedDescription medd) {
		List<String> list = new ArrayList<String>();
		for(String key : mapCode.keySet()){
			list.add(key);
		}
		if(list.size() != 0){
			String timeStr0 = null;
			for(int i = 0;i<list.size();i++){
				if(i == 0){
					timeStr0 = list.get(0);
				}else{
					timeStr0 = timeStr0+ "," + list.get(i);
				}
			}
			System.out.println("时间点timeStr0=============="+ timeStr0);
			medd.setTime(timeStr0);
		}
	}
	/**
	 * 铃声
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {// 
		case 0:
			if (resultCode == Activity.RESULT_OK) {

				ringText.setText(data.getStringExtra("ringName"));
			}
			break;
			//图库
		case 1:
			if(resultCode == Activity.RESULT_OK){
				image.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);

				Uri uri = data.getData();
				Cursor cursor = this.getContentResolver().query(uri, null,null, null, null);
				cursor.moveToFirst();
				String imgNo = cursor.getString(0); // 图片编号
				String imgPath = cursor.getString(1); // 图片文件路径
				String imgSize = cursor.getString(2); // 图片大小
				String imgName = cursor.getString(3); // 图片文件名
				cursor.close();
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 30;
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options); 

				image.setImageBitmap(bitmap);

				imageUri = imgPath;

				System.out.println(imgPath+"//"+imgName);

			}else if(resultCode == Activity.RESULT_CANCELED){

			}


			break;
		}
	}

	/**
	 * 时间对话框
	 */
	public void showDialog_Layout2(Context context, final TextView text,
			final View view) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View textEntryView = inflater.inflate(R.layout.activity_select_time,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final Button sure = (Button) textEntryView
				.findViewById(R.id.uploadHistory);
		final Button cancel = (Button) textEntryView
				.findViewById(R.id.uploadHistorys);
		final TimePicker timePicker = (TimePicker) textEntryView
				.findViewById(R.id.timePicker1);

		builder.setCancelable(false);
		builder.setView(textEntryView);
		dialog2 = builder.show();

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hour=null;
				if(timePicker.getCurrentHour()<10){
					hour="0"+timePicker.getCurrentHour();			
				}else{
					hour=String.valueOf(timePicker.getCurrentHour());
				}
				String minute=null;
				if(timePicker.getCurrentMinute()<10){
					minute="0"+timePicker.getCurrentMinute();
				}else{
					minute=String.valueOf(timePicker.getCurrentMinute());
				}		
				text.setText(new StringBuilder().append(hour).append(":").append(minute));

				//if (timeStr != null) {
				//String[] strArr = timeStr.split(",");
				//List<String> timeList = Arrays.asList(strArr);
				List<String> timeList = new ArrayList<String>();
				for(String key : mapCode.keySet()){
					timeList.add(key);
					System.out.println("listTime="+timeList);
				}
				if (timeList.contains(text.getText().toString())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MedDescriptionDetailActivity2.this);
					builder.setTitle("注意");
					builder.setCancelable(false)
					.setMessage("不能添加相同时间")
					.setPositiveButton(
							"确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {

									layouts.removeView(view);
									dialog.dismiss();
								}
							}).create();
					builder.show();
				} else {
					//timeStr = timeStr + "," + text.getText().toString();
					setCode();
				}
				//}else {
				//	timeStr = text.getText().toString();
				//	setCode();
				//}

				dialog2.dismiss();
				mapCode.put(text.getText().toString(), part);
				for(String key:mapCode.keySet()){
					System.out.println("value="+mapCode.get(key));
				}
				//listInt.add(part);

				mapTime.put(text.getText().toString(), getTime(timePicker));
				for(String key:mapTime.keySet()){
					System.out.println("value="+mapTime.get(key));
				}
				//getTime(timePicker);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.dismiss();
			}
		});
	}

	/**
	 * 药品类型
	 */
	public void showDialog_Layout3(Context context, final TextView textView) {

		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater
				.inflate(R.layout.alpha_alert1, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		textEntryView.setLayoutParams(new LayoutParams(300, 400));
		builder.setCancelable(false);
		builder.setView(textEntryView);
		dialog2 = builder.show();

		Button cancel = (Button) textEntryView.findViewById(R.id.usage_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.dismiss();
			}
		});
		final RadioGroup radioG = (RadioGroup) textEntryView
				.findViewById(R.id.groups);
		radioG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				String str = ((RadioButton) textEntryView.findViewById(radioG
						.getCheckedRadioButtonId())).getText().toString();
				textView.setText(str);
				dialog2.dismiss();
			}
		});
	}

	/**
	 * 重复间隔
	 */
	public void showDialog_Layout4(Context context, final TextView textView) {

		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater
				.inflate(R.layout.alpha_alert2, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// textEntryView.setLayoutParams(new LayoutParams(300, 400));
		builder.setCancelable(false);
		builder.setView(textEntryView);

		Button cancel = (Button) textEntryView.findViewById(R.id.usage_cancel);
		// 单选
		final RadioGroup radioG = (RadioGroup) textEntryView
				.findViewById(R.id.groups);
		dialog2 = builder.show();

		// 取消
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.dismiss();
			}
		});

		radioG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				String str = ((RadioButton) textEntryView.findViewById(radioG
						.getCheckedRadioButtonId())).getText().toString();
				textView.setText(str);
				if(str.equals("每天")){
					repeat = 0;
					System.out.println("repeat="+str+repeat);
				}else{
					repeat = 1;
					System.out.println("repeat="+str+repeat);
				}
				dialog2.dismiss();
			}
		});

	}
	/**
	 * 每次添加新时间都要更新request_code表里part值
	 */
	private void setCode() {
		requsetCodeManager = new RequsetCodeManager(getApplicationContext());
		requsetCodeManager.openDataBase();
		requestCode = new RequestCode();
		listCode = requsetCodeManager.getParts();
		if(listCode.size() == 0){

		}else{
			requestCode = listCode.get(0);
			part = requestCode.getPart();
		}
		part += 1;
		System.out.println("part="+part);
		requestCode.setPart(part);
		requsetCodeManager.remove();
		requsetCodeManager.addRequsetCodeManager(requestCode);
		requsetCodeManager.closeDataBase();
	}
	/**
	 * 获得现在时间到设定时间的时间差
	 * @param timePicker
	 */
	private long getTime(final TimePicker timePicker) {
		long systemTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		// 选择的每天定时时间
		long selectTime = calendar.getTimeInMillis();	
		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
		if(systemTime > selectTime) {
			Toast.makeText(MedDescriptionDetailActivity2.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			selectTime = calendar.getTimeInMillis();
		}
		// 计算现在时间到设定时间的时间差
		long time = selectTime - systemTime;
		//listTime.add(time);

		return time;
	}
	/**
	 * 同步删除mapCode，mapTime中相应的元素
	 * @param text
	 */
	private void deleteMap(final TextView text) {
		if(mapCode.size() != 0){
			//迭代map，将要删除的时间对应的part也删除
			Iterator<Map.Entry<String, Integer>> iterator = mapCode.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Integer> entry = iterator.next();
				String key = entry.getKey();
				if(text.getText().toString().equals(key)){
					iterator.remove();
					System.out.println("delete this: "+key+" = "+key);
				}
			}
			for(String key:mapCode.keySet()){
				System.out.println("value="+mapCode.get(key));
			}
		}
		if(mapTime.size() != 0){
			//迭代map，将要删除的时间对应的时间差也删除
			Iterator<Map.Entry<String, Long>> iterator = mapTime.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Long> entry = iterator.next();
				String key = entry.getKey();
				if(text.getText().toString().equals(key)){
					iterator.remove();
					System.out.println("delete this: "+key+" = "+key);
				}
			}
			for(String key:mapTime.keySet()){
				System.out.println("value="+mapTime.get(key));
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(gg==1){
				Intent intent = new Intent(MedDescriptionDetailActivity2.this,TodayRemindActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}else{
				Intent intent = new Intent(MedDescriptionDetailActivity2.this,MedDescriptionListActivity.class);
				startActivity(intent);
				MedDescriptionDetailActivity2.this.finish();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onLongClick(View v) {
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
			
			builder.setCancelable(true);
			Dialog dialog =builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			dialog.setContentView(view);
			
		}
		
		return false;
	}

}
