package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class OfficDocDetailBean  implements Serializable{
	public List<OfficDocDetail> ds;
	public class OfficDocDetail implements Serializable{
		public String AllowDocument;
		public String CreateDate;
		public String CurrentStepNo;
		public String CurrentStepName;
		public String DocID;
		public String FileCode;
		public String FileContent;
		public String FileDZ;
		public String FileFF;
		public String FileHJCD;
		public String FileJMDJ;
		public String FileKeyWord;
		public String FileRecCopyUsers;
		public String FileRecCopyUsersTrue;
		public String FileRecUsers;
		public String FileRecUsersTrue;
		public String FileTitle;
		public String FileWZ;
		public String FlowID;
		public String IsDocument;
		public String IsRunning;
		public String LXDH;
		public String LXR;
		public String SendDept;
		public String Tel;
		public String UserID;
		public String FileRecUsersText;
		public String FileRecCopyUsersText;
		public String OwerUserName;
	}
}
