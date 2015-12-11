package com.rilintech.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.rilintech.smartpillbox_001.R;

public class CompanyActivity extends Activity implements OnClickListener{
	private WebView webView;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ruilinsaer);
		
		ExitActivity.getExitActivity().addActivity(this);
		
		//webView = (WebView)findViewById(R.id.web);
		//webView.loadUrl("http://www.rilintech.com/lianxiwomen/2013-06-22/1.html");
		initView();
	}
	private void initView() {
		back = (ImageView) findViewById(R.id.iconSetAlertBack);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iconSetAlertBack:
			this.finish();
			
			break;

		default:
			break;
		}
	}
}
