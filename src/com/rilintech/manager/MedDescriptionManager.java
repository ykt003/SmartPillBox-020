package com.rilintech.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rilintech.model.MedDescription;

public class MedDescriptionManager {

	public static final String DB_NAME = "sci";
	public static final String TABLE_NAME = "med_descriptions";
	public static final int DB_VERSION = 2;
	private static final String TAG = "med_descriptions";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;
	private String table = "med_descriptions";
	private static Context mContext = null;
	public static final String DATABASE_CREATE = "create table if not exists med_descriptions ("
			+ "_id Integer primary key autoincrement,"
			+ " name varchar(255), time varchar(255), "
			+ "mensure varchar(255), usage varchar(255), "
			+ "ringName varchar(255), interval varchar(255), "
			+ "requestCode varchar(255), "
			+ "imageUri varchar(255), "
			+ "repeat Integer, flag Integer,date varchae(255)) ";
	private static SQLiteDatabase mSQLiteDatabase = null;
	private static DataBaseManagementHelper mDatabaseHelper = null;

	private static class DataBaseManagementHelper extends SQLiteOpenHelper {

		DataBaseManagementHelper(Context context) {

			super(context, DB_NAME, null, DB_VERSION);
			onCreate(getWritableDatabase());
		}

		DataBaseManagementHelper(Context context, String bZ) {

			super(context, DB_NAME, null, DB_VERSION);
			onCreate(getWritableDatabase());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "db.getVersion()=" + db.getVersion());
			db.execSQL(DATABASE_CREATE);
			Log.i(TAG, "db.execSQL(DB_CREATE)");
			Log.e(TAG, DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(TAG, "DataBaseManagementHelper onUpgrade");
			onCreate(db);
		}
	}

	public MedDescriptionManager(Context context) {
		mContext = context;
		Log.i(TAG, "UserDataManager construction!");

	}

	public void openDataBase() throws SQLException {

		mDatabaseHelper = new DataBaseManagementHelper(mContext);

		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void openDataBase(String bZ) throws SQLException {

		mDatabaseHelper = new DataBaseManagementHelper(mContext, bZ);

		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void closeDataBase() throws SQLException {

		mDatabaseHelper.close();
	}

	// ɾ��ĳһ��
	public void removeEntry(String _rowIndex) {

		mSQLiteDatabase.delete(TABLE_NAME, KEY_ID + " = " + _rowIndex, null);
	}

	// 查找所有的药物记录

	public Cursor queryIpCursor() {
		Cursor cursor = mSQLiteDatabase.rawQuery(
				"select * from med_descriptions", null);
		return cursor;
	}

	/**
	 * 查找所有的药物记录
	 * @return medds
	 */
	public List<MedDescription> getMedDscriptions() {

		List<MedDescription> medds = new ArrayList<MedDescription>();
		Cursor c = queryIpCursor();
		while (c.moveToNext()) {
			MedDescription medd = new MedDescription();
			medd.setInterval(c.getString(c.getColumnIndex("interval")));
			medd.setMensure(c.getString(c.getColumnIndex("mensure")));
			medd.setName(c.getString(c.getColumnIndex("name")));
			medd.setRingName(c.getString(c.getColumnIndex("ringName")));
			medd.setTime(c.getString(c.getColumnIndex("time")));
			medd.setUsage(c.getString(c.getColumnIndex("usage")));
			medd.setFlag(c.getInt(c.getColumnIndex("flag")));
			medd.setMedd_id(c.getInt(c.getColumnIndex("_id")));
			medd.setRepeat(c.getInt(c.getColumnIndex("repeat")));
			medd.setRequestCode(c.getString(c.getColumnIndex("requestCode")));
			medd.setDate(c.getString(c.getColumnIndex("date")));
			medd.setImageUri(c.getString(c.getColumnIndex("imageUri")));
			medds.add(medd);
		}
		c.close();
		return medds;
	}

	/*
	 * 查找某一个序列号的记录实体
	 */
	public MedDescription medDescription(String medd_id) {

		MedDescription medd = new MedDescription();

		Cursor c = queryOneMedDescription(medd_id);

		while (c.moveToNext()) {
			medd.setFlag(c.getInt(c.getColumnIndex("flag")));
			medd.setInterval(c.getString(c.getColumnIndex("interval")));
			medd.setMensure(c.getString(c.getColumnIndex("mensure")));
			medd.setName(c.getString(c.getColumnIndex("name")));
			medd.setRingName(c.getString(c.getColumnIndex("ringName")));
			medd.setTime(c.getString(c.getColumnIndex("time")));
			medd.setUsage(c.getString(c.getColumnIndex("usage")));
			medd.setRepeat(c.getInt(c.getColumnIndex("repeat")));
			medd.setRequestCode(c.getString(c.getColumnIndex("requestCode")));
			medd.setDate(c.getString(c.getColumnIndex("date")));
			medd.setImageUri(c.getString(c.getColumnIndex("imageUri")));
		}

		return medd;
	}
	public MedDescription getMedDescriptionFromName(String name){
		MedDescription medd = new MedDescription();
		Cursor c = queryMedDescriptionFromName(name);
		while(c.moveToNext()){
			medd.setTime(c.getString(c.getColumnIndex("time")));
			medd.setDate(c.getString(c.getColumnIndex("date")));
			medd.setMedd_id(c.getInt(c.getColumnIndex("_id")));
			medd.setImageUri(c.getString(c.getColumnIndex("imageUri")));
		}
		return medd;
}
	
	public Cursor queryMedDescriptionFromName(String name){
		Cursor cursor=mSQLiteDatabase.rawQuery("select * from med_descriptions where name=?", 
				new String[]{name});
		return cursor;
	}
	
	
	
	/*
	 * 查找某一个序列号的记录
	 */
	public Cursor queryOneMedDescription(String medd_id) {
		Cursor cursor = mSQLiteDatabase.rawQuery(
				"select * from med_descriptions where _id = ? ",
				new String[] { medd_id });
		return cursor;
	}

	/*
	 *
	 */
	public void addMedDescriptions(List<MedDescription> medds) {

		for (MedDescription med : medds) {
			addMedDescription(med);
		}

	}

	/*
	 * 添加
	 */
	public long addMedDescription(MedDescription med) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("name", med.getName());
		cv.put("flag", med.getFlag());
		cv.put("interval", med.getInterval());
		cv.put("mensure", med.getMensure());
		cv.put("ringName", med.getRingName());
		cv.put("time", med.getTime());
		cv.put("usage", med.getUsage());
		cv.put("repeat", med.getRepeat());
		cv.put("requestCode", med.getRequestCode());
		cv.put("date", med.getDate());
		cv.put("imageUri", med.getImageUri());
		return mSQLiteDatabase.insert(table, null, cv);
	}	
	//更新
	public void updateMedDescription(MedDescription value,String medd_id){
		ContentValues values=new ContentValues();
		values.put("name", value.getName());
		values.put("interval", value.getInterval());
		values.put("mensure", value.getMensure());
		values.put("ringName", value.getRingName());
		values.put("time", value.getTime());
		values.put("usage", value.getUsage());
		values.put("flag", value.getFlag());
		values.put("repeat", value.getRepeat());
		values.put("requestCode", value.getRequestCode());
		values.put("date", value.getDate());
		values.put("imageUri", value.getImageUri());
		//values.put("medd_id", value.getMedd_id());
		String whereClause="_id=?";
		String[] whereArgs={String.valueOf(medd_id)};
		
		mSQLiteDatabase.update(TABLE_NAME, values, whereClause, whereArgs);
	}
	/**
	 * 更新flag（服务开关）值
	 */
	public void updateFlag(int flag,int medd_id){
		ContentValues c=new ContentValues();
		c.put("flag", flag);
		String whereClause="_id=?";
		String[] whereArgs={String.valueOf(medd_id)};
		
		mSQLiteDatabase.update(TABLE_NAME, c, whereClause, whereArgs);
	}
	
}
