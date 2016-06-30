package com.publish.haoffice.api.bean.office;

import java.util.List;

/**
 * Created by ACER on 2016/6/24.
 */
public class AddUserBean {

    public List<AddUser> ds;
    public class AddUser{
        public String txtTitle;
        public String txtCurrentStepNo;
        public String txtSelectUsers;
        public String HFSelectUsers;
    }
}
