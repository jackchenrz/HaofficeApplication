package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ConBean implements Serializable{
	public List<Con> ds;
	public class Con implements Serializable{
		public String SerialNo;
		public String WorkSpaceID;
		public String WorkLevel;
		public String WorkLine;
		public String LineType;
		public String ConstructionName;
		public String ConstructionDate;
		public String TrueConstructionDate;
		public String ConstructionTime;
		public String ConstructionPlace;
		public String ConstructionContent;
		public String SpeedLimit;
		public String EquipmentChanges;
		public String TransportOrg;
		public String ConstructionDept;
		public String OtherNote;
		public String ConstructionTypeID;
		public String ProjectTypeID;
		public String WorkSpace;
		public String MonitorText;
		public String MonitorValue;
		public String MonitorIDS;
		public String SkylightStart;
		public String SkylightEnd;
		public String RegStation;
		public String StationName;
		public String IsBC;
		public String ConstBasis;
		public String ConstructionTypeName;
		public String ProjectTypeName;
	}
}
