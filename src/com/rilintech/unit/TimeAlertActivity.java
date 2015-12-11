package com.rilintech.unit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rilintech.smartpillbox_001.R;

public class TimeAlertActivity extends Activity {

	private static Button sure;
	private static Button cancel;
	private static Dialog dialog1;

	public static void showDialog_Layout(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View textEntryView = inflater.inflate(R.layout.activity_select_time,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		sure = (Button) textEntryView.findViewById(R.id.uploadHistory);
		cancel = (Button) textEntryView.findViewById(R.id.uploadHistorys);
		builder.setCancelable(false);
		builder.setView(textEntryView);
		dialog1 = builder.show();

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
}
