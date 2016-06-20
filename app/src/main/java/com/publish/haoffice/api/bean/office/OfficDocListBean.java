package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class OfficDocListBean implements Serializable{
	public List<OfficDoc> ds;
	public class OfficDoc implements Serializable{
		public String CreateDate;
		public String CurrentStepName;
		public String FileCode;
		public String FileDZ;
		public String FileTitle;
		public String RecID;
		public String SendDept;
	}
}
