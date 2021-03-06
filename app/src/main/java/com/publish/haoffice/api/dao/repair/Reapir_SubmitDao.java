package com.publish.haoffice.api.dao.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.publish.haoffice.api.DBHelper.DBHelper;
import com.publish.haoffice.api.bean.repair.RepairInfo;
import com.publish.haoffice.api.utils.BooleanAndintUtils;


public class Reapir_SubmitDao{

	private Context context;
	private DBHelper helper;

	private Reapir_SubmitDao(Context context) {
		this.context = context;
		helper = new DBHelper(context);
	}

	public static Reapir_SubmitDao instance;

	public synchronized static Reapir_SubmitDao getInstance(Context context) {
		if (instance == null) {
			instance = new Reapir_SubmitDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "Repair_Submit";

	public boolean addRepairSubmit(RepairInfo repairInfo) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("RepairID", repairInfo.RepairID);
			values.put("RepairType", repairInfo.RepairType);
			values.put("EqptID", repairInfo.EqptID);
			values.put("EqptName", repairInfo.EqptName);
			values.put("EqptType", repairInfo.EqptType);
			values.put("ProbeStation", repairInfo.ProbeStation);
			values.put("FaultStatus", repairInfo.FaultStatus);
			values.put("Specification", repairInfo.Specification);
			values.put("Manufacturer", repairInfo.Manufacturer);
			values.put("UserID", repairInfo.UserID);
			values.put("UserDeptID", repairInfo.UserDeptID);
			values.put("FaultOccu_Time", repairInfo.FaultOccu_Time);
			values.put("FaultReceiveTime", repairInfo.FaultReceiveTime);
			values.put("FaultAppearance", repairInfo.FaultAppearance);
			values.put("CreateDate", repairInfo.CreateDate);
			values.put("LastUpdateDate", repairInfo.LastUpdateDate);
			values.put("IsStop", repairInfo.IsStop);
			values.put("StopTime", repairInfo.StopTime);
			values.put("StopHours", repairInfo.StopHours);
			values.put("StopMinutes", repairInfo.StopMinutes);
			values.put("ImageUrl", repairInfo.ImageUrl);
			values.put("IsUpload", repairInfo.IsUpload);
			values.put("RepairDeptID", repairInfo.RepairDeptID);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addRepairSubmit-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public boolean deleteAllRepairSubmit() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllRepairSubmit-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public List<RepairInfo> getAllRepairSubmit() {
		List<RepairInfo> list = new ArrayList<RepairInfo>();
		RepairInfo repairInfo;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				repairInfo = new RepairInfo();
				repairInfo.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				repairInfo.RepairType = cursor.getString(cursor
						.getColumnIndex("RepairType"));
				repairInfo.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				repairInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				repairInfo.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				repairInfo.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				repairInfo.FaultStatus = cursor.getString(cursor
						.getColumnIndex("FaultStatus"));
				repairInfo.Specification = cursor.getString(cursor
						.getColumnIndex("Specification"));
				repairInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				repairInfo.UserID = cursor.getString(cursor
						.getColumnIndex("UserID"));
				repairInfo.UserDeptID = cursor.getString(cursor
						.getColumnIndex("UserDeptID"));
				repairInfo.FaultOccu_Time = cursor.getString(cursor
						.getColumnIndex("FaultOccu_Time"));
				repairInfo.FaultReceiveTime = cursor.getString(cursor
						.getColumnIndex("FaultReceiveTime"));
				repairInfo.FaultAppearance = cursor.getString(cursor
						.getColumnIndex("FaultAppearance"));
				repairInfo.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				repairInfo.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				repairInfo.IsStop = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("IsStop")));
				repairInfo.StopTime = cursor.getString(cursor
						.getColumnIndex("StopTime"));
				repairInfo.StopHours = cursor.getInt(cursor
						.getColumnIndex("StopHours"));
				repairInfo.StopMinutes = cursor.getInt(cursor
						.getColumnIndex("StopMinutes"));
				repairInfo.ImageUrl = cursor.getString(cursor
						.getColumnIndex("ImageUrl"));
				repairInfo.IsUpload = cursor.getInt(cursor
						.getColumnIndex("IsUpload"));
				repairInfo.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				list.add(repairInfo);
				repairInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllRepairSubmit-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public List<RepairInfo> getRepairInfo(String userID,int IsUpload,String RepairType) {
		List<RepairInfo> list = new ArrayList<RepairInfo>();
		RepairInfo repairInfo = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "UserID = ? and IsUpload = ? and RepairType = ?",
					new String[] { userID,IsUpload + "" ,RepairType}, null, null, "FaultOccu_Time desc");
			while (cursor.moveToNext()) {
				repairInfo = new RepairInfo();
				repairInfo.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				repairInfo.RepairType = cursor.getString(cursor
						.getColumnIndex("RepairType"));
				repairInfo.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				repairInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				repairInfo.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				repairInfo.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				repairInfo.FaultStatus = cursor.getString(cursor
						.getColumnIndex("FaultStatus"));
				repairInfo.Specification = cursor.getString(cursor
						.getColumnIndex("Specification"));
				repairInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				repairInfo.UserID = cursor.getString(cursor
						.getColumnIndex("UserID"));
				repairInfo.UserDeptID = cursor.getString(cursor
						.getColumnIndex("UserDeptID"));
				repairInfo.FaultOccu_Time = cursor.getString(cursor
						.getColumnIndex("FaultOccu_Time"));
				repairInfo.FaultReceiveTime = cursor.getString(cursor
						.getColumnIndex("FaultReceiveTime"));
				repairInfo.FaultAppearance = cursor.getString(cursor
						.getColumnIndex("FaultAppearance"));
				repairInfo.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				repairInfo.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				repairInfo.IsStop = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("IsStop")));
				repairInfo.StopTime = cursor.getString(cursor
						.getColumnIndex("StopTime"));
				repairInfo.StopHours = cursor.getInt(cursor
						.getColumnIndex("StopHours"));
				repairInfo.StopMinutes = cursor.getInt(cursor
						.getColumnIndex("StopMinutes"));
				repairInfo.ImageUrl = cursor.getString(cursor
						.getColumnIndex("ImageUrl"));
				repairInfo.IsUpload = cursor.getInt(cursor
						.getColumnIndex("IsUpload"));
				repairInfo.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				list.add(repairInfo);
				repairInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getRepairInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public RepairInfo getRepairInfobyRepairID(String RepairID,String RepairType) {
		RepairInfo repairInfo = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "RepairID like ? and EqptType = ?",
					new String[] { "%"+RepairID+"%",RepairType}, null, null, null);
			if (cursor.moveToNext()) {
				repairInfo = new RepairInfo();
				repairInfo.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				repairInfo.RepairType = cursor.getString(cursor
						.getColumnIndex("RepairType"));
				repairInfo.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				repairInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				repairInfo.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				repairInfo.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				repairInfo.FaultStatus = cursor.getString(cursor
						.getColumnIndex("FaultStatus"));
				repairInfo.Specification = cursor.getString(cursor
						.getColumnIndex("Specification"));
				repairInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				repairInfo.UserID = cursor.getString(cursor
						.getColumnIndex("UserID"));
				repairInfo.UserDeptID = cursor.getString(cursor
						.getColumnIndex("UserDeptID"));
				repairInfo.FaultOccu_Time = cursor.getString(cursor
						.getColumnIndex("FaultOccu_Time"));
				repairInfo.FaultReceiveTime = cursor.getString(cursor
						.getColumnIndex("FaultReceiveTime"));
				repairInfo.FaultAppearance = cursor.getString(cursor
						.getColumnIndex("FaultAppearance"));
				repairInfo.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				repairInfo.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				repairInfo.IsStop = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("IsStop")));
				repairInfo.StopTime = cursor.getString(cursor
						.getColumnIndex("StopTime"));
				repairInfo.StopHours = cursor.getInt(cursor
						.getColumnIndex("StopHours"));
				repairInfo.StopMinutes = cursor.getInt(cursor
						.getColumnIndex("StopMinutes"));
				repairInfo.ImageUrl = cursor.getString(cursor
						.getColumnIndex("ImageUrl"));
				repairInfo.IsUpload = cursor.getInt(cursor
						.getColumnIndex("IsUpload"));
				repairInfo.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
			}
		} catch (Exception e) {
			System.out.println("----getRepairInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return repairInfo;
	}

	public void delRepairInfo(String RepairID) {
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			database.delete(TABLE_NAME, "RepairID = ?",
					new String[] { RepairID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editRepairInfo(int isUpload, String FaultReceiveTime, String RepairID) {
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("IsUpload", isUpload);
			values.put("FaultReceiveTime", FaultReceiveTime);
			database.update(TABLE_NAME, values, "RepairID = ?",
					new String[] { RepairID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
