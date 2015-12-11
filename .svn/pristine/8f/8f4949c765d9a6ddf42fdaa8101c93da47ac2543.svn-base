package com.rilintech.controller;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;

public class ExitActivity {

	private static ExitActivity instance;
	
	private List<Activity> activityList = new LinkedList<Activity>();

	public  ExitActivity(){

	}

	public static ExitActivity getExitActivity(){

		if(instance==null){

			instance=new ExitActivity();

		}

		return instance;

	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		if (activityList != null && activityList.size() > 0) {
			if(!activityList.contains(activity)){
				activityList.add(activity);
			}
		}else{
			activityList.add(activity);
		}
	}
	// 遍历所有Activity 并finish

	public void exit() {
		if (activityList != null && activityList.size() > 0) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
		 System.exit(0);
	}
	
}
