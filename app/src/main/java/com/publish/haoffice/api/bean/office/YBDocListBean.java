package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class YBDocListBean implements Serializable{
	public List<YBDoc> ds;
	public class YBDoc implements Serializable{
		public String CreateDate;
		public String CurrentStepName;
		public String FileCode;
		public String FileDZ;
		public String RecID;
		public String RecTitle;
		public String SendDept;
		public String DocID;
		public String FileTitle;
		public String DocType;
	}
}
