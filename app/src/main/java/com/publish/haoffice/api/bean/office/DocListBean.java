package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DocListBean implements Serializable{
	public List<Doc> ds;
	public class Doc implements Serializable{
		public String CreateDate;
		public String CurrentStepName;
		public String FileCode;
		public String FileDZ;
		public String FileTitle;
		public String RecID;
		public String SendDept;
	}
}
