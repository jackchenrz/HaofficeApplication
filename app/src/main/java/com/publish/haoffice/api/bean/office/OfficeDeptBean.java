package com.publish.haoffice.api.bean.office;

import java.util.List;

public class OfficeDeptBean {
    public List<OfficeDept> ds;
    public class OfficeDept{
        public String Text;
        public String Value;
        public String ImageUrl;
        public String NavigateUrl;
        public String MethodName;
        public String RoleID;
    }
}
