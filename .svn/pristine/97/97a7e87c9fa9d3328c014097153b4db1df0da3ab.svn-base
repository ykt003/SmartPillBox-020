package com.rilintech.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rilintech.adapter.CalendarAdapter;
import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.smartpillbox_001.R;
import com.rilintech.unit.mDatePickerDialog;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class HistoryActivity extends Activity implements OnGestureListener,OnClickListener{
	//左右
	private ImageButton leftBtn,rightBtn;
	//返回
	private ImageView back;
	//今天
	private TextView todayBtn=null;
	//显示内容
	private TextView historyText=null;	
	/**
	 * 用来保存从数据库中取出的时间的map集合
	 */
	private Map<String,String>mapTime=new HashMap<String, String>();
	/**
	 * 用来保存药品名称的集合
	 */
	private Map<Integer, String>mapMedicineName=new HashMap<Integer, String>();
	/**
	 * 用来保存数据库中的日期的集合
	 */
	private Map<Integer, String>mapDate=new HashMap<Integer, String>();
	/**
	 * 用来保存到了提醒时间的服药历史记录
	 */
	private List<String>historyTextList=new ArrayList<String>();
	
	/**
	 * 用来保存数据库所有的服药记录
	 */
	private List<String>historyTextLists=new ArrayList<String>();
	/**
	 * 取出的时间map集合的key
	 */
	private int num=0;
	/**
	 * 字符串拼接
	 */
	StringBuffer buffer=new StringBuffer();
	StringBuffer b=new StringBuffer();
	/**
	 * 点击的日历上的位置
	 */
	public static int selectedPosition=0;
	/**
	 * 日历上的位置有没点击的事件判断
	 */
	public static boolean isSelected=false;

	private ListView historyListView=null;
	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	//2015-12-12
	private String currentDate = "";
	//2015012012
	private String nowDate="";
	private Bundle bd=null;//发送参数
	private Bundle bun=null;//接收参数
	private String ruzhuTime;
	private String lidianTime;
	private String state="";

	@SuppressLint("SimpleDateFormat")
	public HistoryActivity(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = sdf.format(date);  //当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		ExitActivity.getExitActivity().addActivity(this);
		
		//初始化
		initView();

		jumpToToday();
		/*bd=new Bundle();//out
		bun=getIntent().getExtras();//in
		if(bun!=null&&bun.getString("state").equals("ruzhu")){
			state=bun.getString("state");
			System.out.println("%%%%%%"+state);
		}else if(bun!=null&&bun.getString("state").equals("lidian")){
			state=bun.getString("state");
			System.out.println("|||||||||||"+state);
		}*/



		/*//手势识别
		gestureDetector = new GestureDetector(this);
		//bd=new Bundle();
		calV = new CalendarAdapter(this,getResources(),year_c,month_c,day_c);
		addGridView();
		gridView.setAdapter(calV);
		addTextToTopTextView(topText);*/


		//获得服药记录
		MedDescription medd = new MedDescription();
		MedDescriptionManager meddManager = new MedDescriptionManager(
				getApplicationContext());
		meddManager.openDataBase();

		List<MedDescription> lists = meddManager.getMedDscriptions();
		if(lists.size()==0){
			historyText.setVisibility(View.VISIBLE);
			historyText.setText("请先添加服药闹铃提醒！！");
		}
		//获得当前时间
		String currentTime=null;
		/*ContentResolver cv = this.getContentResolver();
			String strTimeFormat = Settings.System.getString(cv,
			Settings.System.TIME_12_24);
			if(strTimeFormat.equals("24")){*/
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		currentTime = formatter.format(curDate);
		System.out.println("currentTime="+currentTime);

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
				/*Iterator<Entry<Integer, String>>iterator=mapMedicineName.entrySet().iterator();
				if(iterator.hasNext()){
					Entry<Integer, String>entry=iterator.next();
					Integer key=entry.getKey();
					String name = mapMedicineName.get(key);
					System.out.println("name="+name);*/
				String name=null;
				for(int n=0;n<mapMedicineName.size();n++){
					name=mapMedicineName.get(n);
					medd = meddManager.getMedDescriptionFromName(name);
					String medTime = medd.getTime();
					if(medTime==null){
						historyText.setVisibility(View.VISIBLE);
						historyText.setText("请添加指定的闹铃时间！！");
					}else{
						historyListView.setVisibility(View.VISIBLE);
						String[] timeStr = medTime.split(",");
						for(String str:timeStr){
							mapTime.put(name+num,str);
							num+=1;
							System.out.println("str="+str);
						}
						String time=null;
						int result=0;
						//遍历比较数据库中药品服用时间和当前系统时间
						for(int j=0;j<num;j++){
							time = mapTime.get(name+j);
							System.out.println("time="+time);
							if(time!=null){
								result=time.compareTo(currentTime);
								System.out.println("result="+result);

								if(result<0){
									String str="“"+name+"”"+"在"+time+"已服用";
									buffer.append(str).append(",");
								}
								//全部的服药记录
								String allHistoryRecords="“"+name+"”"+"在"+time+"已服用";
								System.out.println("allHistoryRecords="+allHistoryRecords);
								b.append(allHistoryRecords).append(",");
							}				
						}
						//去掉最后的逗号
						if(buffer.length()>0){
							buffer.deleteCharAt(buffer.length()-1);
						}
						
						if(b.length()>0){
							b.deleteCharAt(b.length()-1);
						}
						historyTextLists.add(b.toString());
						System.out.println("historyTextLists="+historyTextLists);
						b.delete(0, b.toString().length());
						
						//换行
						//buffer.append("\n").append("\n");
						historyTextList.add(buffer.toString());
						buffer.delete(0, buffer.toString().length());
					}	
				}
				//historyText.setText(buffer.toString());
				historyListView.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_expandable_list_item_1, historyTextList));
				meddManager.closeDataBase();

			}

		}

	}

	private void initView() {

		//手势识别
		gestureDetector = new GestureDetector(this);
		leftBtn=(ImageButton)findViewById(R.id.imageButtonpre);
		leftBtn.setOnClickListener(this);
		rightBtn=(ImageButton)findViewById(R.id.imageButtonnext);
		rightBtn.setOnClickListener(this);
		topText = (TextView) findViewById(R.id.Top_Date);
		topText.setOnClickListener(this);
		todayBtn=(TextView)findViewById(R.id.btn_goback_to_today);
		todayBtn.setOnClickListener(this);
		historyText=(TextView)findViewById(R.id.dis);
		historyListView=(ListView)findViewById(R.id.historyListView);		
		back=(ImageView)findViewById(R.id.alertHistoryBack);
		back.setOnClickListener(this);

	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
			isSelected=false;
			//像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			gvFlag++;

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			isSelected=false;
			//向右滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月		
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			gridView.setAdapter(calV);
			gvFlag++;
			addTextToTopTextView(topText);

			return true;
		}
		return false;
	}


	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()){
		case Menu.FIRST:
			//跳转到今天
			int xMonth = jumpMonth;
			int xYear = jumpYear;
			int gvFlag =0;
			jumpMonth = 0;
			jumpYear = 0;
			addGridView();   //添加一个gridView
			year_c = Integer.parseInt(currentDate.split("-")[0]);
			month_c = Integer.parseInt(currentDate.split("-")[1]);
			day_c = Integer.parseInt(currentDate.split("-")[2]);
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			gvFlag++;
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.BLACK);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	//添加gridview
	private void addGridView() {

		gridView =(GridView)findViewById(R.id.dateGridView);
		gridView.setOnTouchListener(new OnTouchListener() {
			//将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return HistoryActivity.this.gestureDetector.onTouchEvent(event);
			}
		});           

		gridView.setOnItemClickListener(new OnItemClickListener() {
			//gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3){	
				isSelected=true;
				selectedPosition=position;
				System.out.println("position======================"+position);
				System.out.println("isSelected="+isSelected);
				//点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if(startPosition <= position+7  && position <= endPosition-7){
					System.out.println("-------------------");
					String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					//String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					System.out.println("scheduleYear="+scheduleYear);
					System.out.println("scheduleMonth="+scheduleMonth);
					//Toast.makeText(HistoryActivity.this, scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, 2000).show();
					ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";	                  
					lidianTime=scheduleMonth+"月"+scheduleDay+"日";  
					System.out.println("ruzhuTime="+ruzhuTime);
					System.out.println(" lidianTime"+lidianTime);

					if(state.equals("ruzhu")){
						bd.putString("ruzhu", ruzhuTime);
						System.out.println("ruzhuuuuuu"+bd.getString("ruzhu"));
					}else if(state.equals("lidian")){

						bd.putString("lidian", lidianTime);
					}

					
					//2015-04-09
					if(Integer.parseInt(scheduleMonth)<10){
						scheduleMonth=0+scheduleMonth;
					}
					if(Integer.parseInt(scheduleDay)<10){
						scheduleDay=0+scheduleDay;
					}

					//获得服药记录
					MedDescription medd = new MedDescription();
					MedDescriptionManager meddManager = new MedDescriptionManager(
							getApplicationContext());
					meddManager.openDataBase();

					List<MedDescription> lists = meddManager.getMedDscriptions();
					if(lists.size()==0){
						historyText.setText("请先添加服药闹铃提醒！！");
					}
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
						int count=0;
						//区间
						int d=0;
						//药品存在，遍历读取每一个药品，获得单个药品的日期
						if(mapMedicineName.size()!=0){
							String name=null;
							for(int n=0;n<mapMedicineName.size();n++){
								name=mapMedicineName.get(n);
								medd = meddManager.getMedDescriptionFromName(name);
								String date = medd.getDate();
								//2015-04-10	
								date=date.replace('-', '0');
								System.out.println("date="+date);
								nowDate = currentDate.replace('-', '0');
								d=Integer.parseInt(date)-Integer.parseInt(nowDate);
								System.out.println("d="+d);
							}
						}
						String selectedDate=scheduleYear+"-"+scheduleMonth+"-"+scheduleDay;
						
						selectedDate=selectedDate.replace('-', '0');	
						int r= Integer.parseInt(selectedDate)-Integer.parseInt(nowDate);
						System.out.println("r="+r);
						if(r==0){			
							historyListView.setVisibility(View.VISIBLE);
							historyText.setVisibility(View.GONE);
							historyListView.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_expandable_list_item_1, historyTextList));
							
						}else if(d<=r&&r<0){
							historyListView.setVisibility(View.VISIBLE);
							historyText.setVisibility(View.GONE);
							historyListView.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_expandable_list_item_1, historyTextLists));
							
						}else{
							historyListView.setVisibility(View.GONE);
							historyText.setVisibility(View.VISIBLE);
							historyText.setText("该日期还没服药记录！！");
							
						}
					}
					meddManager.closeDataBase();
				}
				//加载gridView
				CalendarAdapter adapter = new CalendarAdapter(HistoryActivity.this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
				gridView.setAdapter(adapter); 
				
			}
		});

	}
	@Override
	public void onClick(View v) {
		if(v==leftBtn){
			isSelected=false;
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月		
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);	      
		}
		if(v==rightBtn){
			isSelected=false;
			addGridView();   //添加一个gridView		
			jumpMonth++;     //下一个月
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			gridView.setAdapter(calV); 
			addTextToTopTextView(topText);
		}

		if(v==topText){
			System.out.println("00-00-00-00-00-00-00");
			mDatePickerDialog datePickerDialog=new mDatePickerDialog(HistoryActivity.this,new DatePickerDialog.OnDateSetListener(){

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					year_c = year;
					month_c = monthOfYear;
					day_c = dayOfMonth;
					//更新日历
					addGridView();   //添加一个gridView		
					calV = new CalendarAdapter(HistoryActivity.this, getResources(), year_c, month_c+1, day_c);
					gridView.setAdapter(calV); 
					addTextToTopTextView(topText);
				}
			}, year_c, month_c);
			datePickerDialog.show();	
		}

		if(todayBtn==v){
			jumpToToday();
		}
		if(back==v){
			Intent intent=new Intent(HistoryActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

	}

	/**
	 * 跳转到当前日期
	 */
	private void jumpToToday() {
		//跳转到今天
		//int xMonth = jumpMonth;
		//int xYear = jumpYear;
		//int gvFlag =0;
		jumpMonth = 0;
		jumpYear = 0;
		addGridView();   //添加一个gridView
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		gridView.setAdapter(calV);
		addTextToTopTextView(topText);
		//gvFlag++;
		isSelected=false;
		CalendarAdapter adapter=new CalendarAdapter(HistoryActivity.this, getResources(), year_c, month_c, day_c);
		gridView.setAdapter(adapter);
		if(historyTextList.size()!=0){
			historyListView.setVisibility(View.VISIBLE);
			historyText.setVisibility(View.GONE);
			historyListView.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_expandable_list_item_1, historyTextList));
		}
	}
}
