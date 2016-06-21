package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class NoticeDetailBean  implements Serializable{
	public List<NoticeDetail> ds;
	public class NoticeDetail implements Serializable{
		public String NoticeID;
		public String NoticeType;
		public String NoticeCode;
		public String NoticeTitle;
		public String NoticeContent;
		public String FileRecUsers;
		public String FileRecCopyUsers;
		public String UserID;
		public String IsDocument;
		public String FlowID;
		public String CreateDate;
		public String CurrentStepNo;
		public String IsRunning;
		public String AllowDocument;
//		public String FileRecUsersText;
//		public String FileRecCopyUsersText;
	}
}
