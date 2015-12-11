package com.rilintech.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.smartpillbox_001.R;

@SuppressLint("CutPasteId")
public class HomeActivity extends Activity implements OnClickListener {
	//日历的年月日，用药时间，药品
	private TextView monthText,dayText,yearText,userDrugTime,drug;
	private LinearLayout webText;
	private LinearLayout addMedDescription;
	private LinearLayout historyDescription;
	private TextView todayRemind;
	/**
	 * 用来保存药品名称的集合
	 */
	private Map<Integer, String>mapMedicineName=new HashMap<Integer, String>();
	/**
	 * 用来保存闹铃没启动的时间集合
	 */
	private Map<String, String>mapRemindTime=new HashMap<String, String>();
	/**
	 * 用来保存从数据库中取出的时间的map集合
	 */
	private Map<String,String>mapTime=new HashMap<String, String>();
	/**
	 * 取出的时间map集合的key
	 */
	private int num=0;
	//拼接的数字开头
	private int k=0;
	private int m=0;
	private String lowDrugName=null;
	private String lowTime=null;

	private String nowDrugName=null;
	//数据库中的药品使用的时间
	private String drugUserTime=null;
	/**
	 * 保存timeAndName这个map的list集合
	 */
	List<Map<String, String>>listsForTime=new ArrayList<Map<String, String>>();

	List<Map<String, String>>listsForLowTime=new ArrayList<Map<String, String>>();
	/**
	 * 保存未响铃的时间和药品名称，key是时间，value是药品名称
	 */
	Map<String, String>timeAndName=new HashMap<String, String>();

	Map<String, String>lowTimeAndName=new HashMap<String, String>();
	//最接近要闹铃的时间
	String timeForRecently=null;
	String timeForRemote=null;

	Map<String, String>lowTimes=new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		ExitActivity.getExitActivity().addActivity(this);

		initView();
		//设置日期
		setDate();

		//获取当前时间
		String currentTime = getCurrentTime();
		System.out.println("currentTime="+currentTime);
		//获得服药记录
		MedDescription medd = new MedDescription();
		MedDescriptionManager meddManager = new MedDescriptionManager(
				getApplicationContext());
		meddManager.openDataBase();

		List<MedDescription> lists = meddManager.getMedDscriptions();
		if(lists.size()==0){
			userDrugTime.setText("无");
			drug.setText("无");
		}
		//有添加的闹铃	
		if(lists.size()!=0){

			String medicineName=null;
			int medicineNameKey=0;
			//获取药品名称的集合
			for(int i=0;i<=lists.size()-1;i++){
				MedDescription historyList = lists.get(i);
				medicineName= historyList.getName();
				mapMedicineName.put(medicineNameKey, medicineName);
				medicineNameKey++;
			}
			//药品存在，遍历读取每一个药品，获得单个药品的时间集合mapTime
			if(mapMedicineName.size()!=0){
				String name=null;
				for(int n=0;n<mapMedicineName.size();n++){
					name=mapMedicineName.get(n);
					medd = meddManager.getMedDescriptionFromName(name);
					String medTime = medd.getTime();
					if(medTime==null){			
						userDrugTime.setText("无");
						drug.setText("无");
					}else{
						String[] timeStr = medTime.split(",");
						for(String str:timeStr){

							str=str.replace(':', '0');
							mapTime.put(name+num,str);
							num+=1;
							System.out.println("str="+str);
						}

						int result=0;
						//遍历比较数据库中药品服用时间和当前系统时间
						for(int j=0;j<num;j++){
							drugUserTime = mapTime.get(name+j);
							System.out.println("time="+drugUserTime);
							String nowTime = currentTime.replace(':', '0');
							if(drugUserTime!=null){
								if(drugUserTime.startsWith("0")){
									drugUserTime=drugUserTime.substring(1);
									System.out.println("drugUserTime="+drugUserTime);
								}
								if(nowTime.startsWith("0")){
									nowTime=nowTime.substring(1);
								}
								result=Integer.parseInt(drugUserTime)-Integer.parseInt(nowTime);
								System.out.println("result="+result);	
							}
							if(result>0){
								mapRemindTime.put(k+name, drugUserTime);
								System.out.println("k+name="+(k+name));	
								k+=1;
							}else{
								lowTimes.put(m+name, drugUserTime);
								m+=1;
							}
						}
					}

					//读取到的所有的大于当前时间的时间，存放在集合中
					for(int i=0;i<=k;i++){
						timeForRecently = mapRemindTime.get(i+name);
						System.out.println("timeForRecently="+timeForRecently);
						if(timeForRecently!=null){
							timeAndName.put(timeForRecently, name);			
						}
					}

					for(int i=0;i<=m;i++){
						timeForRemote=lowTimes.get(i+name);
						if(timeForRemote!=null){
							lowTimeAndName.put(timeForRemote, name);
						}
					}	
				}
				if(timeAndName.size()!=0){
					listsForTime.add(timeAndName);
				}
				if(lowTimeAndName.size()!=0){
					listsForLowTime.add(lowTimeAndName);
				}

				if(listsForLowTime.size()!=0){
					//遍历集合，取出最偏离当前时间的时刻
					Map<String, String> time=listsForLowTime.get(0);
					int minValue=Integer.MAX_VALUE;
					Set<String> timeValue = time.keySet();
					System.out.println("111111111111111111111111111--timeValue"+timeValue);
					for(String value:timeValue){
						System.out.println("-----------------------------------------------"+value+","+time.get(value));
						if(Integer.parseInt(value)< minValue){
							minValue=Integer.parseInt(value);
							lowDrugName=time.get(value);
						}		
					}
					String minTime =null;
					System.out.println("minValue===="+minValue);
					if( minValue>=10000){
						minTime = String.valueOf(minValue);
						System.out.println("minTime1==="+minTime);
					}else{
						minTime=0+String.valueOf(minValue);

						System.out.println("minTime2==="+minTime);
					}
					String hour=null;
					String minute=null;
					if(minTime.length()==5){
						hour = minTime.substring(0, 2);
						System.out.println("hour ==="+hour);
						minute = minTime.substring(3, 5);
						lowTime = hour+":"+minute;
						System.out.println("minute  ==="+minute );	
						if(minTime.startsWith("0")){
							minTime=minTime.substring(1);
						}
					}else if(minTime.length()==2){
						hour="00";
						minute=minTime;
						lowTime = hour+":"+minute;

						minTime="00"+minute;
						System.out.println("0000000000002"+minTime);

					}else if(minTime.length()==4){
						hour="00";
						minute=minTime.substring(2, 4);
						lowTime = hour+":"+minute;
						minTime="00"+minute;
						System.out.println("0000000000004"+minTime);
					}else if(minTime.length()==3){
						hour="00";
						minute=minTime.substring(1, 3);
						lowTime = hour+":"+minute;
						minTime="00"+minute;
						System.out.println("0000000000003"+minTime);
					}

					System.out.println("lowTime========"+lowTime);

					/*if(minTime.startsWith("0")){
						minTime=minTime.substring(1);
					}*/
					//lowDrugName = time.get(minTime);
					System.out.println("lowDrugName======="+lowDrugName);
				}



				if(listsForTime.size()!=0){
					//遍历集合，取出最接近当前时间的时刻
					Map<String, String> time = listsForTime.get(0);			
					int minValue=Integer.MAX_VALUE;
					Set<String> timeValue = time.keySet();
					for(String value:timeValue){
						System.out.println("--------------------"+value+","+time.get(value));
						if(Integer.parseInt(value)<minValue){
							minValue=Integer.parseInt(value);
							nowDrugName=time.get(value);
						}		
					}

					String minTime =null;
					System.out.println("minValue===="+minValue);
					if( minValue>=10000){
						minTime = String.valueOf(minValue);
						System.out.println("minTime1==="+minTime);
					}else{
						minTime=0+String.valueOf(minValue);

						System.out.println("minTime2==="+minTime);
					}
					String hour=null;
					String minute=null;
					if(minTime.length()==5){
						hour = minTime.substring(0, 2);
						System.out.println("hour ==="+hour);
						minute = minTime.substring(3, 5);
						lowTime = hour+":"+minute;
						System.out.println("minute  ==="+minute );	
						minTime=minute;
					}else if(minTime.length()==2){
						hour="00";
						minute=minTime;
						lowTime = hour+":"+minute;

						minTime=00+minute;

					}else if(minTime.length()==4){
						hour="00";
						minute=minTime.substring(2, 4);
						lowTime = hour+":"+minute;
						minTime=00+minute;
					}else if(minTime.length()==3){
						hour="00";
						minute=minTime.substring(1, 3);
						lowTime = hour+":"+minute;
						minTime=00+minute;
					}
					String t = hour+":"+minute;		
					System.out.println(" t="+ t);

					//String nowDrugName = time.get(minTime);
					System.out.println("nowDrugName="+nowDrugName);
					userDrugTime.setText(t);
					drug.setText(nowDrugName);
				}else{
					userDrugTime.setText(lowTime);
					drug.setText(lowDrugName);
				}
			}}

		meddManager.closeDataBase();
	}
	/**
	 * 获取当前时间
	 * @return currentTime
	 */
	private String getCurrentTime() {

		String currentTime=null;
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		currentTime = formatter.format(curDate);
		System.out.println("currentTime="+currentTime);
		return currentTime;
	}

	private void initView() {
		//设置提醒
		addMedDescription = (LinearLayout)findViewById(R.id.y_nowremind);
		addMedDescription.setOnClickListener(this);
		//联系我们
		webText = (LinearLayout)findViewById(R.id.y_setClocks);
		webText.setOnClickListener(this);
		//服药历史
		historyDescription=(LinearLayout)findViewById(R.id.historylayoutroot);
		historyDescription.setOnClickListener(this);
		//用药提醒时间
		userDrugTime=(TextView)findViewById(R.id.hometime);
		drug=(TextView)findViewById(R.id.y_drugName);		
		//当天提醒
		todayRemind = (TextView) findViewById(R.id.todayAlert);
		todayRemind.setOnClickListener(this);


	}

	/*
	 * 设置日期
	 */
	private  void setDate(){
		yearText = (TextView) findViewById(R.id.y_year);
		monthText = (TextView) findViewById(R.id.y_monthtv);
		dayText = (TextView) findViewById(R.id.y_day);

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		yearText.setText(String.valueOf(year)+"年");
		if(month<10){
			monthText.setText(String.valueOf("0"+month)+"/");
		}else{
			monthText.setText(String.valueOf(month)+"/");
		}
		if(day<10){
			dayText.setText(String.valueOf("0"+day));
		}else{
			dayText.setText(String.valueOf(day));
		}	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == addMedDescription){

			Intent intent = new Intent(this, MedDescriptionListActivity.class);
			startActivity(intent);

		}
		if(v == webText){
			Intent intent = new Intent(this, CompanyActivity.class);
			startActivity(intent);
		}
		if(v==historyDescription){
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
		}
		if(v==todayRemind){
			Intent intent = new Intent(this, TodayRemindActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//获取当前时间
		String currentTime = getCurrentTime();
		System.out.println("currentTime="+currentTime);
		//获得服药记录
		MedDescription medd = new MedDescription();
		MedDescriptionManager meddManager = new MedDescriptionManager(
				getApplicationContext());
		meddManager.openDataBase();

		List<MedDescription> lists = meddManager.getMedDscriptions();
		if(lists.size()==0){
			userDrugTime.setText("无");
			drug.setText("无");
		}
		//有添加的闹铃	
		if(lists.size()!=0){

			String medicineName=null;
			int medicineNameKey=0;
			//获取药品名称的集合
			for(int i=0;i<=lists.size()-1;i++){
				MedDescription historyList = lists.get(i);
				medicineName= historyList.getName();
				mapMedicineName.put(medicineNameKey, medicineName);
				medicineNameKey++;
			}
			//药品存在，遍历读取每一个药品，获得单个药品的时间集合mapTime
			if(mapMedicineName.size()!=0){
				String name=null;
				for(int n=0;n<mapMedicineName.size();n++){
					name=mapMedicineName.get(n);
					medd = meddManager.getMedDescriptionFromName(name);
					String medTime = medd.getTime();
					if(medTime==null){			
						userDrugTime.setText("无");
						drug.setText("无");
					}else{
						String[] timeStr = medTime.split(",");
						for(String str:timeStr){

							str=str.replace(':', '0');
							mapTime.put(name+num,str);
							num+=1;
							System.out.println("str="+str);
						}

						int result=0;
						//遍历比较数据库中药品服用时间和当前系统时间
						for(int j=0;j<num;j++){
							drugUserTime = mapTime.get(name+j);
							System.out.println("time="+drugUserTime);
							String nowTime = currentTime.replace(':', '0');
							if(drugUserTime!=null){
								if(drugUserTime.startsWith("0")){
									drugUserTime=drugUserTime.substring(1);
									System.out.println("drugUserTime="+drugUserTime);
								}
								if(nowTime.startsWith("0")){
									nowTime=nowTime.substring(1);
								}
								result=Integer.parseInt(drugUserTime)-Integer.parseInt(nowTime);
								System.out.println("result="+result);	
							}
							if(result>0){
								mapRemindTime.put(k+name, drugUserTime);
								System.out.println("k+name="+(k+name));	
								k+=1;
							}else{
								lowTimes.put(m+name, drugUserTime);
								m+=1;
							}
						}
					}

					//读取到的所有的大于当前时间的时间，存放在集合中
					for(int i=0;i<=k;i++){
						timeForRecently = mapRemindTime.get(i+name);
						System.out.println("timeForRecently="+timeForRecently);
						if(timeForRecently!=null){
							timeAndName.put(timeForRecently, name);			
						}
					}

					for(int i=0;i<=m;i++){
						timeForRemote=lowTimes.get(i+name);
						if(timeForRemote!=null){
							lowTimeAndName.put(timeForRemote, name);
						}
					}	
				}
				if(timeAndName.size()!=0){
					listsForTime.add(timeAndName);
				}
				if(lowTimeAndName.size()!=0){
					listsForLowTime.add(lowTimeAndName);
				}

				if(listsForLowTime.size()!=0){
					//遍历集合，取出最偏离当前时间的时刻
					Map<String, String> time=listsForLowTime.get(0);
					int minValue=Integer.MAX_VALUE;
					Set<String> timeValue = time.keySet();
					System.out.println("111111111111111111111111111--timeValue"+timeValue);
					for(String value:timeValue){
						System.out.println("-----------------------------------------------"+value+","+time.get(value));
						if(Integer.parseInt(value)< minValue){
							minValue=Integer.parseInt(value);
							lowDrugName=time.get(value);
						}		
					}
					String minTime =null;
					System.out.println("minValue===="+minValue);
					if( minValue>=10000){
						minTime = String.valueOf(minValue);
						System.out.println("minTime1==="+minTime);
					}else{
						minTime=0+String.valueOf(minValue);

						System.out.println("minTime2==="+minTime);
					}
					String hour=null;
					String minute=null;
					if(minTime.length()==5){
						hour = minTime.substring(0, 2);
						System.out.println("hour ==="+hour);
						minute = minTime.substring(3, 5);
						lowTime = hour+":"+minute;
						System.out.println("minute  ==="+minute );	
						if(minTime.startsWith("0")){
							minTime=minTime.substring(1);
						}
					}else if(minTime.length()==2){
						hour="00";
						minute=minTime;
						lowTime = hour+":"+minute;

						minTime="00"+minute;
						System.out.println("0000000000002"+minTime);

					}else if(minTime.length()==4){
						hour="00";
						minute=minTime.substring(2, 4);
						lowTime = hour+":"+minute;
						minTime="00"+minute;
						System.out.println("0000000000004"+minTime);
					}else if(minTime.length()==3){
						hour="00";
						minute=minTime.substring(1, 3);
						lowTime = hour+":"+minute;
						minTime="00"+minute;
						System.out.println("0000000000003"+minTime);
					}

					System.out.println("lowTime========"+lowTime);

					/*if(minTime.startsWith("0")){
								minTime=minTime.substring(1);
							}*/
					//lowDrugName = time.get(minTime);
					System.out.println("lowDrugName======="+lowDrugName);
				}



				if(listsForTime.size()!=0){
					//遍历集合，取出最接近当前时间的时刻
					Map<String, String> time = listsForTime.get(0);			
					int minValue=Integer.MAX_VALUE;
					Set<String> timeValue = time.keySet();
					for(String value:timeValue){
						System.out.println("--------------------"+value+","+time.get(value));
						if(Integer.parseInt(value)<minValue){
							minValue=Integer.parseInt(value);
							nowDrugName=time.get(value);
						}		
					}

					String minTime =null;
					System.out.println("minValue===="+minValue);
					if( minValue>=10000){
						minTime = String.valueOf(minValue);
						System.out.println("minTime1==="+minTime);
					}else{
						minTime=0+String.valueOf(minValue);

						System.out.println("minTime2==="+minTime);
					}
					String hour=null;
					String minute=null;
					if(minTime.length()==5){
						hour = minTime.substring(0, 2);
						System.out.println("hour ==="+hour);
						minute = minTime.substring(3, 5);
						lowTime = hour+":"+minute;
						System.out.println("minute  ==="+minute );	
						minTime=minute;
					}else if(minTime.length()==2){
						hour="00";
						minute=minTime;
						lowTime = hour+":"+minute;

						minTime=00+minute;

					}else if(minTime.length()==4){
						hour="00";
						minute=minTime.substring(2, 4);
						lowTime = hour+":"+minute;
						minTime=00+minute;
					}else if(minTime.length()==3){
						hour="00";
						minute=minTime.substring(1, 3);
						lowTime = hour+":"+minute;
						minTime=00+minute;
					}
					String t = hour+":"+minute;		
					System.out.println(" t="+ t);

					//String nowDrugName = time.get(minTime);
					System.out.println("nowDrugName="+nowDrugName);
					userDrugTime.setText(t);
					drug.setText(nowDrugName);
				}else{
					userDrugTime.setText(lowTime);
					drug.setText(lowDrugName);
				}
			}}

		meddManager.closeDataBase();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if(keyCode == KeyEvent.KEYCODE_BACK){

			ExitActivity.getExitActivity().exit();
			//System.exit(0);

		}
		return super.onKeyDown(keyCode, event);

	}
}
