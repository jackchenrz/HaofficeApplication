package com.publish.haoffice.api.bean.office;

import java.util.List;

/**
 * Created by ACER on 2016/6/21.
 */
public class WordBean {
    public List<Word> ds;
    public class Word{
        public String AttachID;
        public String DocID;
        public String FileName;
        public String FileSize;
        public String FilePath;
        public String CreateDate;
        public String DownUrl;
    }
}
