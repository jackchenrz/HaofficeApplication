package com.publish.haoffice.api;

import android.os.Environment;

public class Const {
    //公用
    public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
    public static final String LOCAL = "local";
    public static final String LOCALPWD = "111111";
    public static final String SERVICE_IP = "ip";
    public static final String SERVICE_PORT = "port";
    public static final String SYSTEM_FLAG = "system_falg";
    public static final String USERNAME = "userName";
    public static final String PWD = "pwd";
    public static final String DB_NAME = "HaOffice";
    public static final String FILEPATH = Environment.getExternalStorageDirectory() + "/.problems/";//图片的存放地址

    //报修子系统
    public static String SP_REPAIR = "repair";
    public static final int DEVICENAME = 1;
    public static final int REPAIRDEPARTMENTLIST = 2;
    public static final String SERVICE_PAGE = "/dm/DmApp.asmx";
    public static final String REPAIR_GETSYSUSER = "Getsys_user";// 得到用户
    public static final String REPAIR_GETSYS_DEPT = "Getsys_dept";// 得到维修部门
    public static final String REPAIR_GETTECH_EQPT = "GetTech_Eqpt";// 得到机械设备
    public static final String REPAIR_GETREPAIR_SUBMIT = "GetRepair_Submit";// 得到上传
    public static final String REPAIR_GETREPAIR_HANDLE = "GetRepair_Handle";// 得到报修处理
    public static final String REPAIR_GETTECHREPAIRINGLIST = "GetTechRepairingList";// 得到机械设备维修列表
    public static final String REPAIR_GETTECHREPAIREDLIST = "GetTechRepairedList";// 得到机械设备已维修列表
    public static final String REPAIR_GET5TREPAIRINGLIST = "Get5TRepairingList";// 得到行安设备维修列表
    public static final String REPAIR_GET5TREPAIREDLIST = "Get5TRepairedList";// 得到行安设备已维修列表
    public static final String REPAIR_GETFIVET_EQPT_PROB_TYPE = "GetFiveT_Eqpt_Prob_Type";// 得到行安设备故障类型
    public static final String REPAIR_RETURNREPAIR_HANDLE = "ReturnRepair_Handle";// 提交维修处理
    public static final String REPAIR_GETFIVET_EQPT = "GetFiveT_Eqpt";// 得到行安设备
    public static final String REPAIR_GETEQPT_INFO = "GetEqpt_Info";
    public static final String REPAIR_UPLOADIMAGE = "uploadImage"; //上传照片
    public static final String REPAIR_RETURNREPAIR_SUBMIT = "ReturnRepair_Submit"; //提交表单
    public static final String REPAIR_GET5TSEARCH = "Get5TSearch"; //提交表单
    public static final String REPAIR_GETTECHSEARCH = "GetTechSearch"; //提交表单
    public static final String REPAIR_GET5TSEARCHNUM = "Get5TSearchNum"; //提交表单
    public static final String REPAIR_GETTECHSEARCHNUM = "GetTechSearchNum"; //提交表单

    //公文子系统
    public static final String SERVICE_PAGE1 = "/demo/RecSerApp.asmx";
    public static final String TOKEN = "token";// 用户角色
    public static final String STEP = "Step";// 用户层级
    public static String SP_OFFICE = "office";
    public static final String USERID = "user_id";// 用户ID
    public static final int CODE = 11;
    public static final String OFFIC_LOGIN = "Login";// 得到用户
    public static final String OFFIC_GETDBDOCLIST = "GetDbDocList";// 得到用户
    public static final String OFFIC_GETDOCDETAIL = "GetDocDetail";// 得到用户
    public static final String OFFIC_AUDITRECORDDOC = "AuditRecordDoc";// 得到用户
    public static final String OFFIC_GETDETAIL = "GetDetail";// 得到用户
    public static final String OFFIC_AUDITRECORD = "AuditRecord";// 得到用户
    public static final String OFFIC_GETNOTICEDETAIL = "GetNoticeDetail";// 得到用户
    public static final String OFFIC_AUDITRECORDNOTICE = "AuditRecordNotice";// 得到用户
    public static final String OFFIC_GETDOCDATAURL = "GetDocDataUrl";// 得到用户
    public static final String OFFIC_GETDOCDATAURLEX = "GetDocDataUrlEx";// test
    public static final String OFFIC_GETATTACHSBYDOCID = "GetAttachsByDocID";// 得到用户
    public static final String OFFIC_BINDSENDDOCCTR = "BindSendDocCtr";// 得到用户
    public static final String OFFIC_BINDSELECTUSERSEX = "BindSelectUsersEx";// 得到用户
    public static final String OFFIC_GETUSERBYDEPTIDEX = "GetUserByDeptIDEx";// 得到用户
    public static final String OFFIC_GETUSERBYDEPTIDEX_CHEJIAN = "GetUserByDeptIDEx_CheJian";// 得到用户
    public static final String OFFIC_SAVESENDDOCCTR = "SaveSendDocCtr";// 得到用户
    public static final String OFFIC_GETYBDOCLIST = "GetYbDocList";// 得到用户
    public static final String OFFIC_BACKSENDDOC = "BackSendDoc";// 得到用户
    public static final String OFFIC_SAVEBACKSENDDOC = "SaveBackSendDoc";// 得到用户
    public static final String OFFIC_ADDUSERDOC = "AddUserDoc";// 得到用户
    public static final String OFFIC_SAVEADDUSERDOC = "SaveAddUserDoc";// 得到用户
    public static final String OFFIC_BINDNOTICECTR = "BindNoticeCtr";// 得到用户
    public static final String OFFIC_SAVENOTICECTR = "SaveNoticeCtr";// 得到用户
    public static final String OFFIC_ADDUSERNOTICE = "AddUserNotice";// 得到用户
    public static final String OFFIC_BACKNOTICEDOC = "BackNoticeDoc";// 得到用户
    public static final String OFFIC_SAVEBACKNOTICEDOC = "SaveBackNoticeDoc";// 得到用户
    public static final String OFFIC_BINDRECDOCCTR = "BindRecDocCtr";// 得到用户
}
