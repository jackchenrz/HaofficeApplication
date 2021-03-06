package com.publish.haoffice.api.bean.repair;

import java.io.Serializable;

public class RepairInfo implements Serializable{

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
	public String LastUpdateDate;
	public boolean IsStop;
	public String StopTime;
	public int StopHours;
	public int StopMinutes;
	public String ImageUrl;
	public int IsUpload;
	public String RepairDeptID;
	
	@Override
	public String toString() {
		return RepairID + RepairType + EqptID +EqptName +EqptType +ProbeStation +FaultStatus +Specification +
				Manufacturer + UserID + UserDeptID + FaultOccu_Time + FaultReceiveTime + FaultAppearance +
				CreateDate + LastUpdateDate + StopTime + StopHours + StopMinutes + ImageUrl + IsUpload + RepairDeptID;
	}
}
