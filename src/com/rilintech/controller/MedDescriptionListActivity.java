package com.rilintech.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.receiver.AlarmReceiver;
import com.rilintech.smartpillbox_001.R;

public class MedDescriptionListActivity extends Activity implements
		OnClickListener, OnItemClickListener {
	
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
	private Context context;
	private String AlarmAction = "com.rilintech.controller.AlarmReceiver";

	private ImageView back, add;
	private ListView listView;
	private List<MedDescription> medds;
	private Myadapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.med_description_add);
		
		this.context = getApplicationContext();
		
		ExitActivity.getExitActivity().addActivity(this);
		
		alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

		back = (ImageView) findViewById(R.id.iconSetAlertBack);
		add = (ImageView) findViewById(R.id.second_imageView2);

		back.setOnClickListener(this);
		add.setOnClickListener(this);

		medds = new ArrayList<MedDescription>();
		MedDescriptionManager meddManager = new MedDescriptionManager(this);
		meddManager.openDataBase(); 
		try {
			medds = meddManager.getMedDscriptions();
		} catch (Exception e) {
			// TODO: handle exception
		}
		meddManager.closeDataBase();
		adapter = new Myadapter();
		listView = (ListView) findViewById(R.id.second_listview);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == back) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			MedDescriptionListActivity.this.finish();
		}
		if (v == add) {
			Intent intent = new Intent(this, MedDescriptionAddActivity.class);
			startActivity(intent);
			MedDescriptionListActivity.this.finish();
		}
	}
	
	public class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return medds.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			final MedDescription medd = medds.get(position);

			view = View.inflate(getApplicationContext(),R.layout.second_listviewitem, null);

			final TextView tittle = (TextView) view
					.findViewById(R.id.s_textview01);
			final ImageView image = (ImageView) view.findViewById(R.id.s_click);

			tittle.setText(medd.getName());
			if (medd.getFlag() == 0) {
				image.setImageResource(R.drawable.y_off);
			} else {

				image.setImageResource(R.drawable.y_on);
			}

			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (medd.getFlag() == 0) {
						image.setImageResource(R.drawable.y_on);
						medd.setFlag(1);
						//更新表中flag值
						updateFlag(medd);
						//重新启动服务
						startService(medd);
						
					} else {
						image.setImageResource(R.drawable.y_off);
						medd.setFlag(0);
						//更新表中flag值
						updateFlag(medd);
						//停掉当前药名下的所有铃声
						stopService(medd);
					}
				}


			});

			return view;
		}
	}
	/**
	 * 重新启动当前药名下的服务程序
	 * @param medd
	 */
	private void startService(final MedDescription medd) {
		//重复响铃
		int repeat = medd.getRepeat();
		String medd_name = medd.getName();
		String medd_num = medd.getMensure();
		String medd_unit = medd.getUsage();
		//请求码集合
		String requestCodes = medd.getRequestCode();
		List<String> list1 = null;
		if(requestCodes != null){
			String []requset = requestCodes.split(",");
			list1 = new ArrayList<String>(
					Arrays.asList(requset));
		}
		//重新计算时间差后的时间差集合
		String times = medd.getTime();
		List<String> list = null;
		if(times != null){
			String []time = times.split(",");
			list = new ArrayList<String>(Arrays.asList(time));
			//遍历并启动服务
			for(int i = 0;i<list.size();i++){
				String [] t = list.get(i).split(":");
				long currentTime = getTime(t);
				System.out.println("重新启动服务requestCode="+Integer.parseInt(list1.get(i))+"//"
						+"currentTime="+currentTime+"//"
						+"repeat="+repeat);
				onStartService( Integer.parseInt(list1.get(i)),  currentTime, 
						repeat, medd_name,medd_num, medd_unit, medd);
			}
		}
	}
	
	/**
	 * 重新获得现在时间到设定时间的时间差
x	 */
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
	/**
	 * 开启注册的与参数匹配的闹铃服务
	 * @param /请求码/时间差/重复/名字/服用量/剂型/药实体
	 */
	public void onStartService(int uuid, long time, int repeat, String name, String num, 
			String unit,final MedDescription medd) {

		intent = new Intent(context,AlarmReceiver.class);
		intent.setAction(AlarmAction);
		intent.putExtra("ringStr", medd.getRingName());
		intent.putExtra("medd_name", name);
		intent.putExtra("medd_num", num);
		intent.putExtra("medd_unit", unit);
		System.out.println("当前选择的铃音==="+medd.getRingName());
		pendingIntent = PendingIntent.getBroadcast(
				context, uuid, intent, 0);
		//triggerAtMillis = System.currentTimeMillis() + 15 * 1000;
		int type = AlarmManager.RTC_WAKEUP;
		if(repeat == 1){
			alarmManager.set(type, System.currentTimeMillis()+time, pendingIntent);
			System.out.println("一次提醒服务开启 repeat = "+repeat);
		}else {
			alarmManager.setRepeating(type, System.currentTimeMillis()+time, 24*60*60*1000, pendingIntent);
			System.out.println("每天提醒服务开启 repeat = "+repeat);
		}

	}

	/**
	 * 停掉当前药名下所有的铃音
	 * @param medd
	 */
	private void stopService(final MedDescription medd) {
		String requestCodes = medd.getRequestCode();
		if(requestCodes != null){
			String []requset = requestCodes.split(",");
			List<String> list = new ArrayList<String>(
					Arrays.asList(requset));
			for (String i:list){
				onStopService(Integer.parseInt(i));
				System.out.println("停掉服务requestCode="+i);
			}
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
	 * 更新表中的flag值
	 * @param medd
	 */
	private void updateFlag(final MedDescription medd) {
		int medd_id = medd.getMedd_id();
		MedDescriptionManager descriptionManager = new MedDescriptionManager(getApplicationContext());
		descriptionManager.openDataBase();
		descriptionManager.updateFlag(medd.getFlag(), medd_id);
		descriptionManager.closeDataBase();
	}

	public static class ViewHolder {
		TextView tittle;
		ImageView image;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MedDescription medd = medds.get(position);
		System.out.println("______________________"+medd.getMedd_id());
		Intent intent = new Intent(MedDescriptionListActivity.this, MedDescriptionDetailActivity2.class);
		intent.putExtra("medd_id", String.valueOf(medd.getMedd_id()));
		
		startActivity(intent);
		
		MedDescriptionListActivity.this.finish();
	}

}
