package com.publish.haoffice.api.dao.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.publish.haoffice.api.DBHelper.DBHelper;
import com.publish.haoffice.api.bean.repair.DeptInfoBean;
import com.publish.haoffice.api.bean.repair.DeptInfoBean.DeptInfo;
import com.publish.haoffice.api.utils.BooleanAndintUtils;

public class Sys_deptDao{

	private DBHelper helper;

	private Sys_deptDao(Context context) {
		helper = new DBHelper(context);
	}

	public static Sys_deptDao instance;

	public synchronized static Sys_deptDao getInstance(Context context) {
		if (instance == null) {
			instance = new Sys_deptDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "sys_dept";

	public boolean addDept(DeptInfo deptInfo) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("dept_id", deptInfo.dept_id);
			values.put("dept_name", deptInfo.dept_name);
			values.put("p_dept_id", deptInfo.p_dept_id);
			values.put("is_used",
					BooleanAndintUtils.Boolean2int(deptInfo.is_used));
			values.put("sort_no", deptInfo.sort_no);
			values.put("is_usedept",
					BooleanAndintUtils.Boolean2int(deptInfo.is_usedept));
			values.put("is_repairdept",
					BooleanAndintUtils.Boolean2int(deptInfo.is_repairdept));
			values.put("deptlevel", deptInfo.deptlevel);
			values.put("is_repairgroup",
					BooleanAndintUtils.Boolean2int(deptInfo.is_repairgroup));
			values.put("is_workarea",
					BooleanAndintUtils.Boolean2int(deptInfo.is_workarea));
			values.put("short_dept_name", deptInfo.short_dept_name);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addUserLog-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public boolean deleteAllDept() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();// ʵ�ֶ���ݿ��д�Ĳ���
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllUserLog-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	public List<DeptInfo> getAllDeptList(int is_repairdept) {
		List<DeptInfo> list = new ArrayList<DeptInfo>();
		DeptInfoBean bean = new DeptInfoBean();
		DeptInfo deptInfo;
		String sql = "select * from " + TABLE_NAME + "where is_repairdept = " + is_repairdept;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "is_repairdept = ?", new String[]{is_repairdept+""}, null, null, null);
			while (cursor.moveToNext()) {
				deptInfo = bean.new DeptInfo();
				deptInfo.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				deptInfo.dept_name = cursor.getString(cursor
						.getColumnIndex("dept_name"));
				deptInfo.p_dept_id = cursor.getString(cursor
						.getColumnIndex("p_dept_id"));
				deptInfo.is_used = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_used")));
				deptInfo.sort_no = cursor.getString(cursor
						.getColumnIndex("sort_no"));
				deptInfo.is_usedept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_usedept")));
				deptInfo.is_repairdept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairdept")));
				deptInfo.deptlevel = cursor.getInt(cursor
						.getColumnIndex("deptlevel"));
				deptInfo.is_repairgroup = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairgroup")));
				deptInfo.is_workarea = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_workarea")));
				deptInfo.short_dept_name = cursor.getString(cursor
						.getColumnIndex("short_dept_name"));
				list.add(deptInfo);
				deptInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllUserLogList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public DeptInfo getDeptInfo(String deptNmae) {
		DeptInfoBean bean = new DeptInfoBean();
		DeptInfo deptInfo = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "dept_name = ?",
					new String[] { deptNmae }, null, null, null);
			while (cursor.moveToNext()) {
				deptInfo = bean.new DeptInfo();
				deptInfo.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				deptInfo.dept_name = cursor.getString(cursor
						.getColumnIndex("dept_name"));
				deptInfo.p_dept_id = cursor.getString(cursor
						.getColumnIndex("p_dept_id"));
				deptInfo.is_used = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_used")));
				deptInfo.sort_no = cursor.getString(cursor
						.getColumnIndex("sort_no"));
				deptInfo.is_usedept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_usedept")));
				deptInfo.is_repairdept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairdept")));
				deptInfo.deptlevel = cursor.getInt(cursor
						.getColumnIndex("deptlevel"));
				deptInfo.is_repairgroup = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairgroup")));
				deptInfo.is_workarea = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_workarea")));
				deptInfo.short_dept_name = cursor.getString(cursor
						.getColumnIndex("short_dept_name"));
			}
		} catch (Exception e) {
			System.out.println("----getAllUserLogList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return deptInfo;
	}

	public DeptInfo getDeptInfo1(String deptID) {
		DeptInfoBean bean = new DeptInfoBean();
		DeptInfo deptInfo = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "dept_id = ?",
					new String[] { deptID }, null, null, null);
			while (cursor.moveToNext()) {
				deptInfo = bean.new DeptInfo();
				deptInfo.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				deptInfo.dept_name = cursor.getString(cursor
						.getColumnIndex("dept_name"));
				deptInfo.p_dept_id = cursor.getString(cursor
						.getColumnIndex("p_dept_id"));
				deptInfo.is_used = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_used")));
				deptInfo.sort_no = cursor.getString(cursor
						.getColumnIndex("sort_no"));
				deptInfo.is_usedept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_usedept")));
				deptInfo.is_repairdept = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairdept")));
				deptInfo.deptlevel = cursor.getInt(cursor
						.getColumnIndex("deptlevel"));
				deptInfo.is_repairgroup = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_repairgroup")));
				deptInfo.is_workarea = BooleanAndintUtils.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_workarea")));
				deptInfo.short_dept_name = cursor.getString(cursor
						.getColumnIndex("short_dept_name"));
			}
		} catch (Exception e) {
			System.out.println("----getAllUserLogList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return deptInfo;
	}

}
