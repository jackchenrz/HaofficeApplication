package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class OtherBean implements Serializable{
	public List<Other> ds;
	public class Other implements Serializable{
		public String PlanDate;
		public String WorkSpaceID;
		public String PlanNum;
		public String FlowTracking;
		public String WorkLine;
		public String Station;
		public String RegStation;
		public String LineType;
		public String OtherJobTypeID;
		public String RepairCount;
		public String RoadCarInfo;
		public String ConstContent;
		public String FitUnit;
		public String DeptAndMan;
		public String OtherNote;
		public String TrueConstructionDate;
		public String WorkSpace;
		public String MonitorText;
		public String MonitorValue;
		public String MonitorIDSValue;
		public String SkylightStart;
		public String SkylightEnd;
	}
}
