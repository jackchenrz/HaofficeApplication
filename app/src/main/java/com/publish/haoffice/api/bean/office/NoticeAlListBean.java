package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class NoticeAlListBean implements Serializable{
	public List<YBNotice> ds;
	public class YBNotice implements Serializable{
		public String RecID;
		public String FileCode;
		public String RecTitle;
		public String CreateDate;
		public String FileDZ;
		public String SendDept;
		public String CurrentStepName;
	}
}
