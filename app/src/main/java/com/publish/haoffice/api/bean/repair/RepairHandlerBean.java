package com.publish.haoffice.api.bean.repair;

import java.io.Serializable;
import java.util.List;

public class RepairHandlerBean implements Serializable {

	public List<RepairHandler> ds;
	public class RepairHandler implements Serializable{
		public String ArriveTime;
		public String CreateDate;
		public String FaultHandle;
		public String FaultLevel;
		public String FaultReason;
		public String FaultType;
		public String FaultUseHours;
		public String FeedbackTime;
		public String IsDocument;
		public String IsReturnSy;
		public String IsReturnWx;
		public String LastUpdateDate;
		public String ProbSysName;
		public String ProbType;
		public String RepairDeptBzID;
		public String RepairDeptID;
		public String RepairFinishTime;
		public String RepairHandleID;
		public String RepairID;
		public String ImageUrlPath;
		public String EqptName;
		public String FaultOccu_Time;
		public String FaultAppearance;
		public String RepairUserName;
		public String RepairType;
	}
}
