package com.publish.haoffice.api.bean.Construct;

import java.util.List;

public class UserLogBean {
	
	public List<UserLog> ds;
	public class UserLog{
		public String user_id;
		public String real_name;
		public String user_name;
		public String password;
		public String mobile;
		public String home_tel;
		public String office_tel;
		public String create_time;
		public boolean modi_pwd_time;
		public boolean recent_access;
		public boolean is_admin;
		public boolean is_used;
		public String is_lock;
		public String sex;
		public String email;
		public String remark;
		public String error_login_times;
		public String sort_no;
		public String dept_id;
		public String is_fuke;
		public String is_jxkh;
		public String role_name;
	}
}
