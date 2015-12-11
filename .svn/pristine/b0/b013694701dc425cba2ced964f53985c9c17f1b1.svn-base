package com.rilintech.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rilintech.manager.MedDescriptionManager;
import com.rilintech.model.MedDescription;
import com.rilintech.smartpillbox_001.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TodayRemindActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "TodayRemindActivity";
	/**
	 * 获取当前日期
	 */
	private String currentDate;
	/**
	 * 所有的药物list集合
	 */
	private List<MedDescription> medd_list;
	/**
	 * 所有重复服用药物集合map
	 */
	private Map<String, Integer> map_repeat = new HashMap<String, Integer>();
	/**
	 * 所有服用药物名字和日期的map集合
	 */
	private Map<String, String> map_date = new HashMap<String, String>();
	/**
	 * 同一天所有服用药物名字和日期的map集合
	 */
	private Map<String, String> map_repeat_new = new HashMap<String, String>();
	/**
	 * 同一天所有服用药物名字和日期的map集合
	 */
	private Map<String, String> map_date_new = new HashMap<String, String>();
	/**
	 * 存放所有时间点map的list集合
	 */
	private List<Map.Entry<String, String>> list_time = new ArrayList<Entry<String, String>>();

	private ListView first_listview;

	private LayoutInflater inflater;

	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_first);
		
		ExitActivity.getExitActivity().addActivity(this);

		inflater = LayoutInflater.from(TodayRemindActivity.this);
		//inflater = this.getLayoutInflater();

		getDate();

		getTimeForDate();

		first_listview = (ListView) findViewById(R.id.first_listview);
		first_listview.setAdapter(new Myadapter());
		first_listview.setOnItemClickListener(this);

		back = (ImageView) findViewById(R.id.iconTodayBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TodayRemindActivity.this,HomeActivity.class);
				startActivity(intent);
				TodayRemindActivity.this.finish();
			}
		});

	}

	public class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_time.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list_time.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.first_listviewitem, null);
				holder = new ViewHolder();
				holder.textview01 = (TextView) convertView.findViewById(R.id.textview01);
				holder.textview02 = (TextView) convertView.findViewById(R.id.textview02);

				convertView.setTag(holder);

			}else{

				holder = (ViewHolder) convertView.getTag();
			}

			holder.textview01.setText(list_time.get(position).getValue());
			holder.textview02.setText(list_time.get(position).getKey());

			return convertView;
		}

	}

	public final class ViewHolder{
		TextView textview01, textview02;
		LinearLayout layout;
	}

	/**
	 * 获得当天所有响铃时间
	 */
	private void getTimeForDate() {
		MedDescriptionManager manager = new MedDescriptionManager(this);
		manager.openDataBase();
		medd_list = manager.getMedDscriptions();
		manager.closeDataBase();

		for(MedDescription medd : medd_list){
			map_date.put(medd.getName(), medd.getDate());
			map_repeat.put(medd.getName(), medd.getRepeat());
		}

		//移除掉不重复响铃的元素
		Iterator<Map.Entry<String, Integer>> iterator1 = map_repeat.entrySet().iterator();
		while(iterator1.hasNext()){
			Map.Entry<String, Integer> entry = iterator1.next();
			if(entry.getValue() == 1){
				iterator1.remove();
			}
		}
		//移除时间与当前日期不相同的元素
		Iterator<Map.Entry<String, String>> iterator = map_date.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, String> entry = iterator.next();
			if(! currentDate.equals(entry.getValue())){
				iterator.remove();
			}
		}
		//再次迭代，取得相应的时间点
		Iterator<Map.Entry<String, String>> iterator_date_new = map_date.entrySet().iterator();
		while(iterator_date_new.hasNext()){
			Map.Entry<String, String> entry = iterator_date_new.next();
			MedDescriptionManager manager2 = new MedDescriptionManager(this);
			manager2.openDataBase();
			MedDescription medd = manager2.getMedDescriptionFromName(entry.getKey());
			manager2.closeDataBase();
			map_date_new.put(entry.getKey(), medd.getTime());
		}

		//再次迭代，取得相应的时间点
		Iterator<Map.Entry<String, Integer>> iterator_repeat_new = map_repeat.entrySet().iterator();
		while(iterator_repeat_new.hasNext()){
			Map.Entry<String, Integer> entry = iterator_repeat_new.next();
			MedDescriptionManager manager2 = new MedDescriptionManager(this);
			manager2.openDataBase();
			MedDescription medd = manager2.getMedDescriptionFromName(entry.getKey());
			manager2.closeDataBase();
			map_repeat_new.put(entry.getKey(), medd.getTime());
		}

		map_date_new.putAll(map_repeat_new);

		//最终的list集合，元素是map
		Iterator<Map.Entry<String, String>> iterator2 = map_date_new.entrySet().iterator();
		while(iterator2.hasNext()){
			Map.Entry<String, String> entry = iterator2.next();
			String[] time = entry.getValue().split(",");
			List<String> list = new ArrayList<String>(Arrays.asList(time));
			for(String t : list){
				Map<String, String> map = new HashMap<String, String>();
				map.put(entry.getKey(), t);
				list_time.addAll(map.entrySet());
			}
		}

		for(int i = 0;i < list_time.size();i++){
			for(int j= i+1;j < list_time.size();j++){

				Map.Entry<String, String> temp;

				DateFormat df = new SimpleDateFormat("HH:mm");
				try {
					Date dt1 = df.parse(list_time.get(i).getValue());
					System.out.println("list_time.get(i).getValue()="+list_time.get(i).getValue());
					Date dt2 = df.parse(list_time.get(j).getValue());
					if(dt1.getTime() > dt2.getTime()){
					/*	Entry<String, String> c = list_time.get(i);
						Entry<String, String> c1 = list_time.get(j);
						list_time.remove(i);
						list_time.add(i, c1);
						list_time.remove(j);
						list_time.add(j, c);*/
						
						temp = (Map.Entry<String, String>) list_time.get(j);	
						list_time.set(j,list_time.get(i));
						list_time.set(i,(Entry<String, String>) temp);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 获取当前日期
	 */
	private String getDate() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = format.format(date);
		System.out.println("当前日期是：  "+currentDate);
		Log.i(TAG, currentDate);

		return currentDate;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MedDescription medd ;
		MedDescriptionManager manager = new MedDescriptionManager(getApplicationContext());
		manager.openDataBase();
		medd=manager.getMedDescriptionFromName(list_time.get(position).getKey());
		manager.closeDataBase();

		Intent intent = new Intent(TodayRemindActivity.this, MedDescriptionDetailActivity2.class);
		intent.putExtra("medd_id", String.valueOf(medd.getMedd_id()));

		intent.putExtra("gg", 1);

		startActivity(intent);
		
		TodayRemindActivity.this.finish();
	}
}
