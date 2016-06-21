package com.publish.haoffice.api.bean.office;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class DocDetailBean  implements Serializable{
	public List<DocDetail> ds;
	public class DocDetail implements Serializable{
		public String AllowDocument;
		public String CreateDate;
		public String CurrentStepNo;
		public String DocumentNo;
		public String FileCode;
//		public String FileRecCbUsers;
//		public String FileRecCopyUsers;
		public String FileRecUsers;
		public String FlowID;
		public String IsDocument;
		public String IsRunning;
		public String RecDate;
		public String RecID;
		public String RecTitle;
		public String RecType;
		public String UserID;
	}
}
