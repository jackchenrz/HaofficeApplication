package com.publish.haoffice.api.dao.repair;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.publish.haoffice.api.DBHelper.DBHelper;
import com.publish.haoffice.api.bean.repair.ToRepairSave;


public class ToRepairDao {

	private DBHelper helper;

	private ToRepairDao(Context context) {
		helper = new DBHelper(context);
	}

	public static ToRepairDao instance;

	public synchronized static ToRepairDao getInstance(Context context) {
		if (instance == null) {
			instance = new ToRepairDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "torepair_handler";

	public boolean addTorepair(ToRepairSave toRepair) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("RepairID", toRepair.RepairID);
			values.put("RepairDeptID", toRepair.RepairDeptID);
			values.put("FaultType", toRepair.FaultType);
			values.put("FaultReason", toRepair.FaultReason);
			values.put("RepairUserName", toRepair.RepairUserName);
			values.put("FaultHandle", toRepair.FaultHandle);
			values.put("RepairFinishTime", toRepair.RepairFinishTime);
			values.put("ProbType", toRepair.ProbType);
			values.put("ProbSysName", toRepair.ProbSysName);
			values.put("RepairType", toRepair.RepairType);
			values.put("UserID", toRepair.UserID);
			values.put("IsUpload", toRepair.IsUpload);
			values.put("EqptName", toRepair.EqptName);
			values.put("FaultAppearance", toRepair.FaultAppearance);
			values.put("ImageUrl", toRepair.ImageUrl);
			values.put("FaultOccu_Time", toRepair.FaultOccu_Time);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addEqptInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public boolean deleteAllToRepairSave() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllEqptInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public List<ToRepairSave> getAllToRepairSave(String userID,int IsUpload,String RepairType) {
		List<ToRepairSave> list = new ArrayList<ToRepairSave>();
		ToRepairSave toRepairSave = null;;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "UserID = ? and IsUpload = ? and RepairType = ?",
					new String[] { userID,IsUpload + "" ,RepairType}, null, null, "RepairFinishTime desc");
			while (cursor.moveToNext()) {
				toRepairSave = new ToRepairSave();
				toRepairSave.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				toRepairSave.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				toRepairSave.FaultType = cursor.getString(cursor
						.getColumnIndex("FaultType"));
				toRepairSave.FaultReason = cursor.getString(cursor
						.getColumnIndex("FaultReason"));
				toRepairSave.RepairUserName = cursor.getString(cursor
						.getColumnIndex("RepairUserName"));
				toRepairSave.FaultHandle = cursor.getString(cursor
						.getColumnIndex("FaultHandle"));
				toRepairSave.RepairFinishTime = cursor.getString(cursor
						.getColumnIndex("RepairFinishTime"));
				toRepairSave.ProbType = cursor.getString(cursor
						.getColumnIndex("ProbType"));
				toRepairSave.ProbSysName = cursor.getString(cursor
						.getColumnIndex("ProbSysName"));
				toRepairSave.RepairType = cursor.getString(cursor
						.getColumnIndex("RepairType"));
				toRepairSave.UserID = cursor.getString(cursor
						.getColumnIndex("UserID"));
				toRepairSave.IsUpload = cursor.getString(cursor
						.getColumnIndex("IsUpload"));
				toRepairSave.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				toRepairSave.FaultAppearance = cursor.getString(cursor
						.getColumnIndex("FaultAppearance"));
				toRepairSave.ImageUrl = cursor.getString(cursor
						.getColumnIndex("ImageUrl"));
				toRepairSave.FaultOccu_Time = cursor.getString(cursor
						.getColumnIndex("FaultOccu_Time"));
				list.add(toRepairSave);
				toRepairSave = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllEqptInfoList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public ToRepairSave getToRepairSave(String RepairID) {
		ToRepairSave toRepairSave = null;;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "RepairID = ?" ,
					new String[] { RepairID}, null, null, "RepairFinishTime desc");
			if (cursor.moveToNext()) {
				toRepairSave = new ToRepairSave();
				toRepairSave.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				toRepairSave.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				toRepairSave.FaultType = cursor.getString(cursor
						.getColumnIndex("FaultType"));
				toRepairSave.FaultReason = cursor.getString(cursor
						.getColumnIndex("FaultReason"));
				toRepairSave.RepairUserName = cursor.getString(cursor
						.getColumnIndex("RepairUserName"));
				toRepairSave.FaultHandle = cursor.getString(cursor
						.getColumnIndex("FaultHandle"));
				toRepairSave.RepairFinishTime = cursor.getString(cursor
						.getColumnIndex("RepairFinishTime"));
				toRepairSave.ProbType = cursor.getString(cursor
						.getColumnIndex("ProbType"));
				toRepairSave.ProbSysName = cursor.getString(cursor
						.getColumnIndex("ProbSysName"));
				toRepairSave.RepairType = cursor.getString(cursor
						.getColumnIndex("RepairType"));
				toRepairSave.UserID = cursor.getString(cursor
						.getColumnIndex("UserID"));
				toRepairSave.IsUpload = cursor.getString(cursor
						.getColumnIndex("IsUpload"));
				toRepairSave.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				toRepairSave.FaultAppearance = cursor.getString(cursor
						.getColumnIndex("FaultAppearance"));
				toRepairSave.ImageUrl = cursor.getString(cursor
						.getColumnIndex("ImageUrl"));
				toRepairSave.FaultOccu_Time = cursor.getString(cursor
						.getColumnIndex("FaultOccu_Time"));
			}
		} catch (Exception e) {
			System.out.println("----getAllEqptInfoList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return toRepairSave;
	}
	
	public void delToRepair(String RepairID) {
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			database.delete(TABLE_NAME, "RepairID = ?",
					new String[] { RepairID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void editToRepair(int isUpload, String FaultReceiveTime, String RepairID) {
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("IsUpload", isUpload);
			values.put("RepairFinishTime", FaultReceiveTime);
			database.update(TABLE_NAME, values, "RepairID = ?",
					new String[] { RepairID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
