package com.publish.haoffice.api.bean.repair;

import java.io.Serializable;

public class ToRepairSave implements Serializable{
	public String RepairID;
	public String RepairDeptID;
	public String FaultType;
	public String FaultReason;
	public String RepairUserName;
	public String FaultHandle;
	public String RepairFinishTime;
	public String ProbType;
	public String ProbSysName;

	public transient String RepairType;
	public transient String UserID;
	public transient String IsUpload;
	public transient String EqptName;
	public transient String FaultAppearance;
	public transient String ImageUrl;
	public transient String FaultOccu_Time;
	public transient String FaultStatus;
}
