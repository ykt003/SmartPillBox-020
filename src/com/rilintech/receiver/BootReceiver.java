package com.rilintech.receiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 设备关机重启后调用类
 * @author ykt003
 *
 */
public class BootReceiver extends BroadcastReceiver{

	/**
	 * 所有药物的集合
	 */
	private List<MedDescription> meddsList;
	/**
	 * 闹钟管理者
	 */
	private AlarmManager alarmManager ;
	/**
	 * 指定启动AlarmActivity组件
	 */
	private Intent intent;
	/**
	 * 等到服务的对象
	 */
	private PendingIntent pendingIntent;
	/**
	 * Action
	 */
	private String AlarmAction = "com.rilintech.controller.AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			//重新计算闹铃时间，并调前面的方法设置闹铃时间及闹铃间隔时间

			MedDescriptionManager meddManager = new MedDescriptionManager(context);
			meddManager.openDataBase(); 
			try {
				meddsList = meddManager.getMedDscriptions();
			} catch (Exception e) {
				// TODO: handle exception
			}
			meddManager.closeDataBase();
			//循环遍历每个药铃
			for(MedDescription medd : meddsList){
				int flag = medd.getFlag();
				if(flag == 1){
					int repeat = medd.getRepeat();
					String ringName = medd.getRingName();
					String medd_name = medd.getName();
					String medd_num = medd.getMensure();
					String medd_unit = medd.getUsage();

					List<String> Codelist = getRequestCodeList(medd);

					List<String> list = getCurrentTimeList(medd);

					//遍历并启动服务
					for(int i = 0;i<list.size();i++){
						String [] t = list.get(i).split(":");
						long currentTime = getCurrentTime(t);
						System.out.println("重新启动服务requestCode="+Integer.parseInt(Codelist.get(i))+"//"
								+"currentTime="+currentTime+"//"
								+"repeat="+repeat);
						onStartService(context, ringName,medd_name,medd_num,medd_unit,
								Integer.parseInt(Codelist.get(i)),  currentTime, repeat);

					}
				}
			}
		}
	}

	private List<String> getCurrentTimeList(MedDescription medd) {
		List<String> list = null;
		//重新计算时间差后的时间差集合
		String times = medd.getTime();
		if(times != null){
			String []tm = times.split(",");
			list = new ArrayList<String>(Arrays.asList(tm));

		}
		return list;
	}

	/**
	 * 重新获得现在时间到设定时间的时间差
	 */
	private long getCurrentTime(final String[] t) {
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
	 * 获取请求码集合
	 * @param medd
	 * @return
	 */
	private List<String> getRequestCodeList(MedDescription medd) {
		String requestCodes = medd.getRequestCode();
		List<String> list = null;
		if(requestCodes != null){
			String []requset = requestCodes.split(",");
			list = new ArrayList<String>(Arrays.asList(requset));
		}
		return list;
	}

	/**
	 * 开启注册的与参数匹配的闹铃服务
	 */
	public void onStartService(Context context, String ringName,String medd_name,
			String medd_num,String medd_unit,int requestCode, long time, int repeat) {

		intent = new Intent(context,AlarmReceiver.class);
		intent.setAction(AlarmAction);
		intent.putExtra("ringStr", ringName);
		intent.putExtra("medd_name", medd_name);
		intent.putExtra("medd_num", medd_num);
		intent.putExtra("medd_unit", medd_unit);
		System.out.println("当前选择的铃音==="+ringName+"//"
				+"medd_name="+medd_name+"//"
				+"medd_num="+medd_num+"//"
				+"medd_unit="+medd_unit);
		pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
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
}
