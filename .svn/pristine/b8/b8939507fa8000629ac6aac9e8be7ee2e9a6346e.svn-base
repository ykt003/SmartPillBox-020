package com.rilintech.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.rilintech.model.MedDescription;
import com.rilintech.smartpillbox_001.R;
import com.rilintech.unit.MedDescriptionDeleteDialogActivity;

public class MedDescriptionDetailActivity extends Activity implements
OnClickListener {

	private ImageView back, delete, clock_on;
	private EditText nameEdit, measureEdit, usageEdit, ringNameEdit,
	intervalEdit;
	private TextView timeText, usageText, intervalText, ringText;
	private Button submit, cancel;
	private Dialog dialog2;
	private LinearLayout addTime, layouts, usageImage, interval_layout,
	ringLayout;
	private int i = 0;
	private int code = 0;
	private boolean flag = false;
	/**
	 * 用来保存开始时间的map集合
	 */
	private Map<Integer,String>mapTimeStr=new HashMap<Integer, String>();
	/**
	 * 开始时间map集合的key
	 */
	private int num=0;
	private String timeStr;
	private String timeStr3;

	private String medd_id;
	
	private int gg = 0 ;

	public static MedDescriptionDetailActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drug_edit);
		instance = this;
		Intent intent = getIntent();
		medd_id = intent.getStringExtra("medd_id");

		init();
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
		measureEdit.setText(medd.getMensure());
		intervalText.setText(medd.getInterval());
		usageText.setText(medd.getUsage());
		ringText.setText(medd.getRingName());
		code=medd.getFlag();
		if (medd.getFlag() == 0) {
			clock_on.setImageResource(R.drawable.y_off);
		} else {
			clock_on.setImageResource(R.drawable.y_on);
		}
		// 开始时间
		timeStr = medd.getTime();
		if (timeStr!= null) {
			String[] timeArr = timeStr.split(",");

			for (String str : timeArr) {
				final View view = this.getLayoutInflater().inflate(
						R.layout.time_add, null);

				final TextView text = (TextView) view.findViewById(R.id.text_time);
				text.setText(str);
				mapTimeStr.put(num,str);
				num+=1;
				
				text.setEnabled(false);		
				layouts.addView(view);

				//开始时间      后面的减号-
				ImageView cancel = (ImageView) view.findViewById(R.id.drug_namese);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						layouts.removeView(view);
						if (mapTimeStr.size() !=0) {
							Iterator<Entry<Integer, String>>iterator=mapTimeStr.entrySet().iterator();
							while(iterator.hasNext()){
								Entry<Integer, String>entry=iterator.next();
								Integer key=entry.getKey();
								if(text.getText().toString().equals(mapTimeStr.get(key))){
									iterator.remove();
								}
							}
						}
					}
				});
			}
		}
	}

	private void init() {
		back = (ImageView) findViewById(R.id.second_imageView1);
		delete = (ImageView) findViewById(R.id.second_imageView2);
		back.setOnClickListener(this);
		delete.setOnClickListener(this);

		submit = (Button) findViewById(R.id.btnlead2backs);
		cancel = (Button) findViewById(R.id.btnlead2nexts);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);

		measureEdit = (EditText) findViewById(R.id.drug_doses);
		layouts = (LinearLayout) findViewById(R.id.time_add);
		nameEdit = (EditText) findViewById(R.id.drug_name);
		addTime = (LinearLayout) findViewById(R.id.addtime);
		addTime.setOnClickListener(this);

		usageImage = (LinearLayout) findViewById(R.id.drug_names);
		usageText = (TextView) findViewById(R.id.y_drugclasss);
		usageImage.setOnClickListener(this);

		interval_layout = (LinearLayout) findViewById(R.id.interval_layout);
		intervalText = (TextView) findViewById(R.id.ys_drugclass);
		interval_layout.setOnClickListener(this);

		// ѡ������
		ringLayout = (LinearLayout) findViewById(R.id.ring_name);
		ringText = (TextView) findViewById(R.id.ringNtext);
		ringLayout.setOnClickListener(this);

		// ���ӵĿ�����ر�
		clock_on = (ImageView) findViewById(R.id.clock_on);
		clock_on.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			Intent intent = new Intent(this, MedDescriptionListActivity.class);
			startActivity(intent);
		}
		if (v == delete) {
			MedDescriptionDeleteDialogActivity.showDialog_Layout(this, medd_id ,gg);

		}
		if (v == clock_on) {
			if (flag == false) {
				code = 0;
				flag = true;
				clock_on.setImageResource(R.drawable.y_off);
			} else {
				code = 1;
				flag = false;
				clock_on.setImageResource(R.drawable.y_on);

				/*startService(new Intent(MedDescriptionDetailActivity.this,
						AlarmService.class));*/
				Toast.makeText(MedDescriptionDetailActivity.this,
						"闹铃已打开", Toast.LENGTH_SHORT).show();

			}

		}

		//  添加时间
		if (v == addTime) {
			i++;
			final View view = this.getLayoutInflater().inflate(R.layout.time_add, null);
			final TextView text = (TextView) view.findViewById(R.id.text_time);
			text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(i<1){
						text.setEnabled(false);
						System.out.println("000-000-000-000-000");
					}

					if(i==1){
						text.setEnabled(true);
						showDialog_Layout2(MedDescriptionDetailActivity.this,text,view);
						System.out.println("i="+i);
						i--;
					}
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
					if (mapTimeStr.size() !=0) {
						System.out.println("+++++++++++++++++++"+ mapTimeStr.size());
						Iterator<Entry<Integer, String>>iterator=mapTimeStr.entrySet().iterator();
						while(iterator.hasNext()){
							Entry<Integer, String>entry=iterator.next();
							Integer key=entry.getKey();
							if(text.getText().toString().equals(mapTimeStr.get(key))){
								iterator.remove();
							}
						}
					}

				}
			});

		}
		//药品类型
		if (v == usageImage) {
			showDialog_Layout3(MedDescriptionDetailActivity.this, usageText);
		}
		//重复间隔
		if (v == interval_layout) {
			showDialog_Layout4(MedDescriptionDetailActivity.this, intervalText);
		}
		//铃声
		if (v == ringLayout) {
			Intent intent = new Intent(this, RingSelectedActivity.class);
			startActivityForResult(intent, 0);
		}

		if (v == submit) {
			MedDescription medd = new MedDescription();

			medd.setName(nameEdit.getText().toString());
			medd.setInterval(intervalText.getText().toString());
			medd.setMensure(measureEdit.getText().toString());
			medd.setRingName(ringText.getText().toString());
			medd.setUsage(usageText.getText().toString());
			medd.setFlag(code);


			System.out.println("code="+code);
			if (mapTimeStr.size() != 0) {
					for(Integer key:mapTimeStr.keySet()){
						String timeStr = mapTimeStr.get(key);
						timeStr3=timeStr3+","+timeStr;
					}
					if(timeStr3.length()>4){
						timeStr3=timeStr3.substring(5);
					}

				medd.setTime(timeStr3);		
				}



			MedDescriptionManager meddManager = new MedDescriptionManager(
					getApplicationContext());
			meddManager.openDataBase();
			meddManager.updateMedDescription(medd,medd_id);
			meddManager.closeDataBase();
			Intent intent = new Intent(getApplicationContext(),
					MedDescriptionListActivity.class);
			startActivity(intent);

		}
		if (v == cancel) {
			Intent intent = new Intent(this, MedDescriptionListActivity.class);
			startActivity(intent);
		}

	}

	//铃声
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {// ������

		case 0:

			if (resultCode == Activity.RESULT_OK) {

				ringText.setText(data.getStringExtra("ringName"));

			}

			break;

		}
	}

	/*
	 * 时间对话框
	 */
	public void showDialog_Layout2(Context context, final TextView text,
			final View view) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View textEntryView = inflater.inflate(R.layout.activity_select_time,null);
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

				if (mapTimeStr.size() != 0) {
					//String[] strArr = timeStr.split(",");

					//List<Map<Integer, String>> timeList = Arrays.asList(mapTimeStr);
					for(Integer key:mapTimeStr.keySet()){
						if (mapTimeStr.get(key).equals(text.getText().toString())) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MedDescriptionDetailActivity.this);
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
							dialog2.dismiss();
							return;
						} 
					}
				}
				dialog2.dismiss();
				
				mapTimeStr.put(num,text.getText().toString());
				num+=1;
				for(Integer key:mapTimeStr.keySet()){
					System.out.println("time="+mapTimeStr.get(key));
					System.out.println("num="+num);
				}

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

	/*
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

		Button cancel = (Button) textEntryView.findViewById(R.id.usage_cancel);
		// ��ѡ
		final RadioGroup radioG = (RadioGroup) textEntryView
				.findViewById(R.id.groups);
		dialog2 = builder.show();

		// ȡ��
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
				dialog2.dismiss();
			}
		});

	}

	/*
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
		// ��ѡ
		final RadioGroup radioG = (RadioGroup) textEntryView
				.findViewById(R.id.groups);
		dialog2 = builder.show();

		// ȡ��
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
				dialog2.dismiss();
			}
		});

	}

}
