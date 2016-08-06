package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TodayConBean implements Serializable{
	public List<TodayCon> ds;
	public class TodayCon implements Serializable{
		public String ConstructionID;
		public String SerialNo;
		public String WorkLevel;
		public String WorkLine;
		public String LineType;
		public String ConstructionName;
		public String TrueConstructionDate;
		public String ConstructionDate;
		public String ConstructionPlace;
		public String ConstructionTime;
		public String ConstructionContent;
		public String SpeedLimit;
		public String EquipmentChanges;
		public String TransportOrg;
		public String ConstructionDept;
		public String OtherNote;
		public String CreateName;
		public String CreateTime;
		public String ModifyName;
		public String ModifyTime;
		public String SgglID;
		public String ConstructionTypeID;
		public String Monitor;
		public String MonitorIDS;
		public String ProjectTypeID;
		public String WorkSpace;
		public String WorkSpaceID;
		public String CurrentInfoType;
		public String Score;
		public String SkylightStart;
		public String SkylightEnd;
		public String RepairCount;
		public String IsBC;
		public String IsDocument;
		public String StationName;
		public String RegStation;
		public String ConstBasis;
		public String constructionTypeName;
	}
}
