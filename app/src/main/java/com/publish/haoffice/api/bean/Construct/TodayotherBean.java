package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TodayotherBean implements Serializable{
	public List<Todayother> ds;
	public class Todayother implements Serializable{
		public String OtherJobID;
		public String PlanDate;
		public String PlanNum;
		public String FlowTracking;
		public String WorkLine;
		public String Station;
		public String RegStation;
		public String LineType;
		public String Project;
		public String RepairCount;
		public String ConstContent;
		public String ConstContentName;
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
	}
}
