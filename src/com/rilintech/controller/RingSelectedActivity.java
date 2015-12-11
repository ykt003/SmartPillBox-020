package com.rilintech.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rilintech.smartpillbox_001.R;

public class RingSelectedActivity extends Activity implements OnClickListener {

	private ImageView back, save;
	private ListView listView,musicListview;
	private MyAdapter adapter;
	//播放的曲目 集合
	private List<String> lists = new ArrayList<String>();
	// 播放器集合
	private List<MediaPlayer> playerLists = new ArrayList<MediaPlayer>();
	private List<ImageView> imageViewLists = new ArrayList<ImageView>();
	//音乐名称
	private String ringName;
	//是否已经加载布局
	private boolean isFirstLoadView=false;
	private View convertview=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_sound);
		
		ExitActivity.getExitActivity().addActivity(this);

		//返回
		back = (ImageView) findViewById(R.id.iconSelectMusicBacks);
		back.setOnClickListener(this);
		//保存
		save = (ImageView) findViewById(R.id.save_song);
		save.setOnClickListener(this);

		lists.add("请服用早餐前药物");
		lists.add("请服用早餐后药物");
		lists.add("请服用中餐前药物");
		lists.add("请服用中餐后药物");
		lists.add("请服用晚餐前药物");
		lists.add("请服用晚餐后药物");
		lists.add("请服用睡前药物");		
		lists.add("更多");

		listView = (ListView) findViewById(R.id.selected_musiclist);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);

		 

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView ringText = (TextView)view.findViewById(R.id.select_musicnames);
				ringName = ringText.getText().toString();
				
				ImageView imageBtn = (ImageView) view.findViewById(R.id.select_btnmusics);
				imageBtn.setImageResource(R.drawable.select_song);
				imageViewLists.add(imageBtn);
				//选中的图标的 (显示与隐藏)
				for (ImageView imageView : imageViewLists) {
					if (imageView == imageBtn) {
						imageView.setVisibility(View.VISIBLE);
					}else{
						imageView.setVisibility(View.GONE);
					}
				}

				/*
				 * 根据位置，播放当前曲目
				 */
				switch (position) {
				case 0:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player1 = MediaPlayer.create(
							getApplicationContext(), R.raw.beforebreakfast);
					player1.start();
					playerLists.add(player1);

					break;
				case 1:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player2 = MediaPlayer.create(
							getApplicationContext(), R.raw.afterbreakfaft);
					player2.start();
					playerLists.add(player2);
					break;
				case 2:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player3 = MediaPlayer.create(
							getApplicationContext(), R.raw.beforelunch);
					player3.start();
					playerLists.add(player3);
					break;
				case 3:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player4 = MediaPlayer.create(
							getApplicationContext(), R.raw.afterlunch);
					player4.start();
					playerLists.add(player4);
					break;
				case 4:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player5 = MediaPlayer.create(
							getApplicationContext(), R.raw.beforesupper);
					player5.start();
					playerLists.add(player5);
					break;
				case 5:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player6 = MediaPlayer.create(
							getApplicationContext(), R.raw.aftersupper);
					player6.start();
					playerLists.add(player6);
					break;
				case 6:
					for (MediaPlayer player : playerLists) {
						player.stop();
						player.release();
						playerLists.remove(player);
					}
					MediaPlayer player7 = MediaPlayer.create(
							getApplicationContext(), R.raw.beforesleep);
					player7.start();
					playerLists.add(player7);
					break;
				case 7:
					
					for (ImageView imageView : imageViewLists) {
							imageView.setVisibility(View.GONE);
					}
					
					if(playerLists.size()!=0){
					MediaPlayer p = playerLists.get(0);	
					p.stop();
					p.release();
					playerLists.remove(p);
					}
					
					if(!isFirstLoadView){
						isFirstLoadView=true;			
						convertview = LayoutInflater.from(RingSelectedActivity.this).inflate(R.layout.activity_choose_music, null);
						musicListview = (ListView)convertview.findViewById(R.id.choose_musiclist);
				
						lists.add("雨的秘密轻轻告诉你");
						lists.add("蓝调口琴");
						lists.add("天空之城");
						lists.add("令你慢慢睁开疏松双眼");
						lists.add("爵士短信铃");
						lists.add("清脆铃声");
						lists.add("棉花糖版钢琴曲");
						lists.add("殇心吉他优美闹钟");
						lists.add("鸟之诗(八音盒版)");
						lists.add("特效音效");
						adapter = new MyAdapter();
						musicListview.setAdapter(adapter);
						
						musicListview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								TextView ringText = (TextView)view.findViewById(R.id.select_musicnames);
								ringName = ringText.getText().toString();
								
								ImageView imageBtn = (ImageView) view.findViewById(R.id.select_btnmusics);
								imageBtn.setImageResource(R.drawable.select_song);
								imageViewLists.add(imageBtn);
								for (ImageView imageView : imageViewLists) {
									if (imageView == imageBtn) {
										imageView.setVisibility(View.VISIBLE);
									}else{
										imageView.setVisibility(View.GONE);
									}
								}
								
							}
						});
					}else{
						isFirstLoadView=false;
						lists.clear();
						if(lists.isEmpty()){
						lists.add("请服用早餐前药物");
						lists.add("请服用早餐后药物");
						lists.add("请服用中餐前药物");
						lists.add("请服用中餐后药物");
						lists.add("请服用晚餐前药物");
						lists.add("请服用晚餐后药物");
						lists.add("请服用睡前药物");		
						lists.add("更多");
						adapter.notifyDataSetChanged();
						}	
					}
					break;
					
					case 8:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player8 = MediaPlayer.create(
								getApplicationContext(), R.raw.a);
						player8.start();
						playerLists.add(player8);
						break;
					case 9:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player9 = MediaPlayer.create(
								getApplicationContext(), R.raw.b);
						player9.start();
						playerLists.add(player9);
						break;
					case 10:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player10 = MediaPlayer.create(
								getApplicationContext(), R.raw.c);
						player10.start();
						playerLists.add(player10);
						break;
					case 11:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player11 = MediaPlayer.create(
								getApplicationContext(), R.raw.d);
						player11.start();
						playerLists.add(player11);
						break;
					case 12:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player12 = MediaPlayer.create(
								getApplicationContext(), R.raw.e);
						player12.start();
						playerLists.add(player12);
						break;
					case 13:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player13 = MediaPlayer.create(
								getApplicationContext(), R.raw.f);
						player13.start();
						playerLists.add(player13);
						break;
					case 14:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player14 = MediaPlayer.create(
								getApplicationContext(), R.raw.g);
						player14.start();
						playerLists.add(player14);
						break;
					case 15:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player15 = MediaPlayer.create(
								getApplicationContext(), R.raw.h);
						player15.start();
						playerLists.add(player15);
						break;
					case 16:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player16 = MediaPlayer.create(
								getApplicationContext(), R.raw.i);
						player16.start();
						playerLists.add(player16);
						break;
					case 17:
						for (MediaPlayer player : playerLists) {
							player.stop();
							player.release();
							playerLists.remove(player);
						}
						MediaPlayer player17 = MediaPlayer.create(
								getApplicationContext(), R.raw.j);
						player17.start();
						playerLists.add(player17);
						break;
				default:
					break;
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == back) {
			for (MediaPlayer player : playerLists) {
				player.stop();
			}
			
			Intent intent = new Intent(this, MedDescriptionAddActivity.class);
			startActivity(intent);
		}
		if (v == save) {
			if(playerLists.size()==0){
				Toast.makeText(RingSelectedActivity.this, "请先选择一首铃声", Toast.LENGTH_LONG).show();
			}else{
				for (MediaPlayer player : playerLists) {
					player.stop();
				}
				Intent intent = new Intent(RingSelectedActivity.this,
						MedDescriptionAddActivity.class);
				intent.putExtra("ringName", ringName);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			
		}
	}
	
	
	

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
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

			view = View.inflate(getApplicationContext(),
					R.layout.activity_select_sounditem, null);
			TextView ringName = (TextView) view
					.findViewById(R.id.select_musicnames);
			ringName.setText(lists.get(position).toString());

			return view;
		}

	}

}
