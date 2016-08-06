package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ComworkBean implements Serializable{
	public List<Comwork> ds;
	public class Comwork implements Serializable{
		public String WorkLogID;
		public String Fkr;
		public String FkInfo;
		public String InfoType;
		public String Score;
		public String Jsr;
		public String FkDate;
		public String FkTime;
		public String CreateDate;
		public String ConstructionID;
	}
}
