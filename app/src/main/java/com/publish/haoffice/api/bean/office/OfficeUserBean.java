package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

public class OfficeUserBean implements Serializable {
    public List<OfficeUser> ds;
    public class OfficeUser implements Serializable{
        public String user_id;
        public String real_name;
        public String user_name;
        public String password;
        public String mobile;
        public String home_tel;
        public String office_tel;
        public String create_time;
        public String modi_pwd_time;
        public String recent_access;
        public String is_admin;
        public String is_used;
        public String is_lock;
        public String remark;
        public String sex;
        public String error_login_times;
        public String email;
        public String sort_no;
        public String dept_id;
        public String dept_name;
        public String role_name;

        @Override
        public boolean equals (Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OfficeUser that = (OfficeUser) o;

            if (user_id != null ? !user_id.equals(that.user_id) : that.user_id != null)
                return false;
            if (real_name != null ? !real_name.equals(that.real_name) : that.real_name != null)
                return false;
            if (user_name != null ? !user_name.equals(that.user_name) : that.user_name != null)
                return false;
            if (password != null ? !password.equals(that.password) : that.password != null)
                return false;
            if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
            if (home_tel != null ? !home_tel.equals(that.home_tel) : that.home_tel != null)
                return false;
            if (office_tel != null ? !office_tel.equals(that.office_tel) : that.office_tel != null)
                return false;
            if (create_time != null ? !create_time.equals(that.create_time) : that.create_time != null)
                return false;
            if (modi_pwd_time != null ? !modi_pwd_time.equals(that.modi_pwd_time) : that.modi_pwd_time != null)
                return false;
            if (recent_access != null ? !recent_access.equals(that.recent_access) : that.recent_access != null)
                return false;
            if (is_admin != null ? !is_admin.equals(that.is_admin) : that.is_admin != null)
                return false;
            if (is_used != null ? !is_used.equals(that.is_used) : that.is_used != null)
                return false;
            if (is_lock != null ? !is_lock.equals(that.is_lock) : that.is_lock != null)
                return false;
            if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
            if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
            if (error_login_times != null ? !error_login_times.equals(that.error_login_times) : that.error_login_times != null)
                return false;
            if (email != null ? !email.equals(that.email) : that.email != null) return false;
            if (sort_no != null ? !sort_no.equals(that.sort_no) : that.sort_no != null)
                return false;
            if (dept_id != null ? !dept_id.equals(that.dept_id) : that.dept_id != null)
                return false;
            if (dept_name != null ? !dept_name.equals(that.dept_name) : that.dept_name != null)
                return false;
            return role_name != null ? role_name.equals(that.role_name) : that.role_name == null;

        }

        @Override
        public int hashCode () {
            int result = user_id != null ? user_id.hashCode() : 0;
            result = 31 * result + (real_name != null ? real_name.hashCode() : 0);
            result = 31 * result + (user_name != null ? user_name.hashCode() : 0);
            result = 31 * result + (password != null ? password.hashCode() : 0);
            result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
            result = 31 * result + (home_tel != null ? home_tel.hashCode() : 0);
            result = 31 * result + (office_tel != null ? office_tel.hashCode() : 0);
            result = 31 * result + (create_time != null ? create_time.hashCode() : 0);
            result = 31 * result + (modi_pwd_time != null ? modi_pwd_time.hashCode() : 0);
            result = 31 * result + (recent_access != null ? recent_access.hashCode() : 0);
            result = 31 * result + (is_admin != null ? is_admin.hashCode() : 0);
            result = 31 * result + (is_used != null ? is_used.hashCode() : 0);
            result = 31 * result + (is_lock != null ? is_lock.hashCode() : 0);
            result = 31 * result + (remark != null ? remark.hashCode() : 0);
            result = 31 * result + (sex != null ? sex.hashCode() : 0);
            result = 31 * result + (error_login_times != null ? error_login_times.hashCode() : 0);
            result = 31 * result + (email != null ? email.hashCode() : 0);
            result = 31 * result + (sort_no != null ? sort_no.hashCode() : 0);
            result = 31 * result + (dept_id != null ? dept_id.hashCode() : 0);
            result = 31 * result + (dept_name != null ? dept_name.hashCode() : 0);
            result = 31 * result + (role_name != null ? role_name.hashCode() : 0);
            return result;
        }
    }
}
