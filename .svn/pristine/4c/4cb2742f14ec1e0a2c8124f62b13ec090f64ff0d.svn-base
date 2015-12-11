package com.rilintech.unit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rilintech.controller.AlarmActivity;
import com.rilintech.controller.MedDescriptionAddActivity;
import com.rilintech.controller.MedDescriptionDetailActivity2;
import com.rilintech.controller.MedDescriptionListActivity;
import com.rilintech.controller.TodayRemindActivity;
import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.smartpillbox_001.R;

public class MedDescriptionDeleteDialogActivity extends Activity {
	private static Button sure;
	private static Button cancel;
	private static Dialog dialog1;
	/**
	 * 请求码数组
	 */
	private static String [] request = null;
	/**
	 * 闹钟管理者
	 */
	private static AlarmManager alarmManager;
	/**
	 * 指定启动AlarmActivity组件
	 */
	private static Intent intent;
	/**
	 * 等到服务的对象
	 */
	private static PendingIntent pendingIntent;

	public static void showDialog_Layout(final Context context,
			final String medd_id , final int gg) {

		alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

		LayoutInflater inflater = LayoutInflater.from(context);
		View textEntryView = inflater.inflate(R.layout.activity_alert_delete,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		sure = (Button) textEntryView.findViewById(R.id.del_ok);
		cancel = (Button) textEntryView.findViewById(R.id.del_cancel);
		builder.setCancelable(false);
		//builder.setView(textEntryView);
		dialog1 = builder.show();
		dialog1.getWindow().setContentView(textEntryView);

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!medd_id.equals("s")) {
					MedDescriptionManager meddManager = new MedDescriptionManager(context);
					MedDescription medDescription = new MedDescription();
					meddManager.openDataBase();
					medDescription = meddManager.medDescription(medd_id);
					meddManager.removeEntry(medd_id);
					meddManager.closeDataBase();

					String code = medDescription.getRequestCode();
					System.out.println("code======="+code);
					if(code != null){
						if(code.length()<=1){
							onStopService(Integer.parseInt(code));
							onStopService(0);//停止5分钟重复响铃
							System.out.println("停止服务code1="+code);
						}else{

							request = code.split(",");

							//停掉之前的服务
							try {
								if(request.length !=0){
									for(String code1 : request){
										onStopService(Integer.parseInt(code1));
										onStopService(0);//停止5分钟重复响铃
										System.out.println("停止服务code1="+code1);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}
				dialog1.dismiss();

				if( gg == 1){
					Intent intent = new Intent(context,
							TodayRemindActivity.class);
					context.startActivity(intent);
					MedDescriptionDetailActivity2.instance.finish();
				}else{

					Intent intent = new Intent(context,
							MedDescriptionListActivity.class);
					context.startActivity(intent);
					MedDescriptionDetailActivity2.instance.finish();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog1.dismiss();
			}
		});
	}
	/**
	 * 取消已经注册的与参数匹配的闹铃
	 */
	public static void onStopService(int uuid){
		try {
			intent = new Intent(MedDescriptionAddActivity.instance,AlarmActivity.class);
			pendingIntent = PendingIntent.getActivity(
					MedDescriptionAddActivity.instance, uuid, intent, 0);
			alarmManager.cancel(pendingIntent);
		} catch (Exception e) {
			System.out.println("uuid=========="+uuid);
		}

	}

}
