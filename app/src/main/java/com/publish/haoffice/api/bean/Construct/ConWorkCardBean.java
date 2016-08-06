package com.publish.haoffice.api.bean.Construct;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ConWorkCardBean implements Serializable{
	public List<ConWorkCard> ds;
	public class ConWorkCard implements Serializable{
		public String SerialNo;
		public String ConstructionName;
		public String WorkLevel;
		public String WorkDate;
		public String WorkTime;
		public String TianQi;
		public String Sgyj;
		public String Sgnr;
		public String Yxfw;
		public String Phdw;
		public String ADate;
		public String ATime;
		public String AHydd;
		public String ACjrText;
		public String ACjrValue;
		public String Adjsj;
		public String Adjdd;
		public String AYxfw;
		public String AGzl;
		public String BDate;
		public String BTime;
		public String Bdd;
		public String BCjryText;
		public String BCjryValue;
		public String BJtfsHc;
		public String BJtfsQc;
		public String BKkcs;
	}
}
