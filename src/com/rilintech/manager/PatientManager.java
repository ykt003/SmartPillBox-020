package com.rilintech.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class PatientManager {

	public static final String DB_NAME = "sci";
	public static final String TABLE_NAME = "patients";
	public static final int DB_VERSION = 2;
	private static final String TAG = "patients";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;
	private Context mContext = null;
	private String table = "address";
	public static final String DATABASE_CREATE = "create table if not exists patients (_id Integer primary key autoincrement,"
			+ " name varchar(255), medicines varchar(255), sign varchar(255) ) ";
	private SQLiteDatabase mSQLiteDatabase = null;
	private DataBaseManagementHelper mDatabaseHelper = null;

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
			//db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
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

	public PatientManager(Context context) {
		mContext = context;
		Log.i(TAG, "UserDataManager construction!");

	}

	public void openDataBase() throws SQLException {

		mDatabaseHelper = new DataBaseManagementHelper(mContext);

		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void openDataBase(String bZ) throws SQLException {

		mDatabaseHelper = new DataBaseManagementHelper(mContext,bZ);

		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void closeDataBase() throws SQLException {

		mDatabaseHelper.close();
	}

	// 删除某一行
	public void removeEntry(long _rowIndex) {

		mSQLiteDatabase.delete(TABLE_NAME, KEY_ID + " = " + _rowIndex, null);
	}

	public Cursor getOneAddress(String table, String id) {
		// String sql = " select * from  " + table + " where _id = ? " + id ;
		// return db.query(table, null, "_id=?", new String[] {"16"}, null,
		// null, null);

		Cursor cursor = mSQLiteDatabase.rawQuery(
				"select * from patients where _id = ? ", new String[] { id });
		return cursor;
	}

	// 查询表内所有的

	/**
	 * 向数据库中存入IP地址
	 */
	public void addIpAddress(String address) {
		mSQLiteDatabase.beginTransaction();
		try {
			mSQLiteDatabase.execSQL("delete from address;");
			mSQLiteDatabase.execSQL("insert into address(ip) values(?)",
					new Object[] { address });
			mSQLiteDatabase.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			mSQLiteDatabase.endTransaction();
		}
	}

	/**
	 * 从数据库中取出地址
	 */
	public Cursor queryIpCursor() {
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from address", null);
		return cursor;
	}

	public String getAddress() {
		String ip = "";
		Cursor cursor = queryIpCursor();
		if (cursor.moveToNext()) {
			ip = cursor.getString(cursor.getColumnIndex("ip"));
		}
		cursor.close();
		return ip;
	}

}
