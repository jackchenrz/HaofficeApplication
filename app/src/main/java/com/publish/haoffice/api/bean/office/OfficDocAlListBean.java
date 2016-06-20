package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class OfficDocAlListBean implements Serializable{
	public List<YBOffcDoc> ds;
	public class YBOffcDoc implements Serializable{
		public String CreateDate;
		public String CurrentStepName;
		public String FileCode;
		public String FileDZ;
		public String RecID;
		public String RecTitle;
		public String SendDept;
	}
}
