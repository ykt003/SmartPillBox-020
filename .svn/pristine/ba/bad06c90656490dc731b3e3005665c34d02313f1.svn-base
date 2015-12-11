package com.rilintech.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class AlarmService extends Service {
	private AlarmManager alarmManager;
	public static AlarmService instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
		
		alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 2 * 1000, PendingIntent
						.getActivity(MedDescriptionAddActivity.instance, 0,
								new Intent(MedDescriptionAddActivity.instance,
										AlarmActivity.class), 0));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 2 * 1000, PendingIntent
						.getActivity(MedDescriptionAddActivity.instance, 0,
								new Intent(MedDescriptionAddActivity.instance,
										AlarmActivity.class), 0));
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
