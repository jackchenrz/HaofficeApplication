package com.msystemlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
	private  SharedPreferences sp;

	public  void setSpName(Context context, String key, String value,String SP_NAME) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 保存字符串
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public  void saveString(Context context, String key, String value,String SP_NAME) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}
	
	public  void clear(Context context,String SP_NAME){
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().clear().commit();
	}
	

	/**
	 * 获取字符串
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public  String getString(Context context, String key, String defValue,String SP_NAME) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}

}
