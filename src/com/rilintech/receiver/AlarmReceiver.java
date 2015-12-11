package com.rilintech.receiver;

import com.rilintech.controller.AlarmActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	private String AlarmAction = "com.rilintech.controller.AlarmReceiver"; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("BroadcastReceiver启动");
		if(intent.getAction().equals(AlarmAction)){
			
			String ringStr = intent.getStringExtra("ringStr");
			String medd_name = intent.getStringExtra("medd_name");
			String medd_num = intent.getStringExtra("medd_num");
			String medd_unit = intent.getStringExtra("medd_unit");
			System.out.println("ringStr="+ringStr+"//"
								+"medd_name="+medd_name+"//"
								+"medd_num="+medd_num+"//"
								+"medd_unit="+medd_unit);
			
			Intent it = new Intent(context,AlarmActivity.class);
			it.putExtra("ringStr", ringStr);
			it.putExtra("medd_name", medd_name);
			it.putExtra("medd_num", medd_num);
			it.putExtra("medd_unit", medd_unit);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(it);
		}
		
	}

}
