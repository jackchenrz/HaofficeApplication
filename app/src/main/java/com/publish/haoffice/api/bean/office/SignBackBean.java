package com.publish.haoffice.api.bean.office;

import java.util.List;

/**
 * Created by ACER on 2016/6/24.
 */
public class SignBackBean  {

    public List<SignBack> ds;

    public class SignBack{

        public String StepID;
        public String StepNo;
        public String StepName;
        public String NextStepNo;
        public String DefaultStepNo;
        public String Status;
        public String SelectType;
        public String SelectUsers;
        public String CreateDate;
        public String FlowID;
        public String IsRadio;
        public String IsAddUser;
        public String IsEditForm;
        public String IsEditDoc;
    }
}
