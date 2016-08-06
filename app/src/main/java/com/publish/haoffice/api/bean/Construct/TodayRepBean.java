package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TodayRepBean implements Serializable{
	public List<TodayRep> ds;
	public class TodayRep implements Serializable{
		public String RepairID;
		public String PlanDate;
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
		public String RoadCarInfo;
		public String RepairCount;
		public String ConstContent;
		public String ConstContentName;
		public String PowerSupplyArm;
		public String ConstTime;
		public String FitUnit;
		public String DeptAndMan;
		public String OtherNote;
		public String CreateName;
		public String CreateDate;
		public String ProjectTypeID;
		public String ConstructionTypeID;
		public String WorkSpace;
		public String WorkSpaceID;
		public String Monitor;
		public String MonitorIDS;
		public String TrueConstructionDate;
		public String SkylightStart;
		public String SkylightEnd;
		public String SgglID;
		public String CurrentInfoType;
		public String Score;
		public String IsSign;
		public String sign_real_name;
	}
}
