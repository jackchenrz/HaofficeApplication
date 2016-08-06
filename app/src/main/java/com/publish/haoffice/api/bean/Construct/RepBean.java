package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RepBean implements Serializable{
	public List<Rep> ds;
	public class Rep implements Serializable{
		public String PlanDate;
		public String WorkSpaceID;
		public String PlanNum;
		public String FlowTracking;
		public String WorkLine;
		public String Station;
		public String RegStation;
		public String LineType;
		public String Project;
		public String RepairType;
		public String RoofType;
		public String ConstMileage;
		public String LockMileage;
		public String RepairCount;
		public String RoadCarInfo;
		public String ConstContent;
		public String PowerSupplyArm;
		public String ConstTime;
		public String FitUnit;
		public String DeptAndMan;
		public String OtherNote;
		public String TrueConstructionDate;
		public String ProjectTypeID;
		public String ProjectTypeName;
		public String WorkSpace;
		public String MonitorText;
		public String MonitorValue;
		public String MonitorIDSValue;
		public String SkylightStart;
		public String SkylightEnd;
	}
}
