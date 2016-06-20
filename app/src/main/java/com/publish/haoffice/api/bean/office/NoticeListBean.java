package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class NoticeListBean implements Serializable{
	public List<Notice> ds;
	public class Notice implements Serializable{
		public String CreateDate;
		public String CurrentStepName;
		public String FileCode;
		public String FileDZ;
		public String FileTitle;
		public String DocID;
		public String SendDept;
	}
}
