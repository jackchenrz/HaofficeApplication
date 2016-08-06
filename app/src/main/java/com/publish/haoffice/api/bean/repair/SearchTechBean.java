package com.publish.haoffice.api.bean.repair;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ACER on 2016/6/28.
 */
public class SearchTechBean implements Serializable{
    public List<SearchTech> ds;
    public class SearchTech implements Serializable{

        public String EqptNum;
        public String EqptModel;
        public String SettingAddr;
        public String RepairID;
        public String RepairType;
        public String EqptID;
        public String EqptName;
        public String EqptType;
        public String ProbeStation;
        public String FaultStatus;
        public String Specification;
        public String Manufacturer;
        public String UserID;
        public String UserDeptID;
        public String FaultOccu_Time;
        public String FaultReceiveTime;
        public String FaultAppearance;
        public String CreateDate;
        public String RepairHandleID;
        public String RepairDeptID;
        public String RepairUserName;
        public String ArriveTime;
        public String RepairFinishTime;
        public String FaultHandle;
        public String FaultType;
        public String FaultReason;
        public String FeedbackTime;
        public String FaultUseHours;
        public String FaultLevel;
        public String repair_dept_name;
        public String dept_name;
        public String submit_dept_name;
        public String lrdate;
        public String fkdate;
        public String IsReturnSy;
        public String IsReturnWx;
        public String IsDocument;
        public String IsReport;
        public String ImageUrl;

    }
}
