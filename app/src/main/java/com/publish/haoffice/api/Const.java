package com.publish.haoffice.api;

import android.os.Environment;

public class Const {
    //公用
    public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
    public static final String LOCAL = "local";//本地账户
    public static final String LOCALPWD = "111111";//本地密码
    public static final String SERVICE_IP = "ip";
    public static final String SERVICE_PORT = "port";
    public static final String SYSTEM_FLAG = "system_falg";//子系统标识
    public static final String USERNAME = "userName";
    public static final String ROLENAME = "roleName";// 用户ID
    public static final String PWD = "pwd";
    public static final String DB_NAME = "HaOffice";//数据库名字
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
    public static final String REPAIR_GETEQPT_INFO = "GetEqpt_Info";//得到设备信息
    public static final String REPAIR_UPLOADIMAGE = "uploadImage"; //上传照片
    public static final String REPAIR_RETURNREPAIR_SUBMIT = "ReturnRepair_Submit"; //提交表单
    public static final String REPAIR_GET5TSEARCH = "Get5TSearch"; //得到5T查询数据
    public static final String REPAIR_GETTECHSEARCH = "GetTechSearch"; //得到机械查询数据
    public static final String REPAIR_GET5TSEARCHNUM = "Get5TSearchNum"; //得到5T查询数据数量
    public static final String REPAIR_GETTECHSEARCHNUM = "GetTechSearchNum"; //得到机械查询数据数量

    //公文子系统
//    public static final String SERVICE_PAGE1 = "/gwgl/RecSerApp.asmx";
    public static final String SERVICE_PAGE1 = "/demo/RecSerApp.asmx";
    public static final String TOKEN = "token";// 用户角色
    public static final String STEP = "Step";// 用户层级
    public static String SP_OFFICE = "office";
    public static final String USERID = "user_id";// 用户ID
    public static final int CODE = 11;
    public static final int CODE1 = 12;
    public static final int CODE2 = 13;
    public static final int CODE3 = 14;
    public static final int CODE4 = 15;
    public static final int CODE5 = 16;
    public static final String OFFIC_LOGIN = "Login";// 用户登录:用户名，密码【使用】
    public static final String OFFIC_GETDBDOCLIST = "GetDbDocList";// 获得待办信息列表【使用】
    public static final String OFFIC_GETDOCDETAIL = "GetDocDetail";// 根据DocID返回段发公文详情
    public static final String OFFIC_AUDITRECORDDOC = "AuditRecordDoc";// 审批记录（段发公文）
    public static final String OFFIC_GETDETAIL = "GetDetail";// 上级文电详情
    public static final String OFFIC_AUDITRECORD = "AuditRecord";// 审批记录
    public static final String OFFIC_GETNOTICEDETAIL = "GetNoticeDetail";// 通知，详情
    public static final String OFFIC_AUDITRECORDNOTICE = "AuditRecordNotice";// 通知，审批记录
    public static final String OFFIC_GETDOCDATAURL = "GetDocDataUrl";// 根据DocID返回文档的下载地址【使用】
    public static final String OFFIC_GETDOCDATAURLEX = "GetDocDataUrlEx";// test 根据DocID返回文档的下载地址,返回HTML【使用】
    public static final String OFFIC_GETATTACHSBYDOCID = "GetAttachsByDocID";// 根据DocID返回段发公文、上级文电、生产安全通知附件列表
    public static final String OFFIC_BINDSENDDOCCTR = "BindSendDocCtr";// 段发公文审批,绑定签阅页面
    public static final String OFFIC_BINDSELECTUSERSEX = "BindSelectUsersEx";//选择人员界面初始化函数
    public static final String OFFIC_GETUSERBYDEPTIDEX = "GetUserByDeptIDEx";//绑定人员根据role_id
    public static final String OFFIC_GETUSERBYDEPTIDEX_CHEJIAN = "GetUserByDeptIDEx_CheJian";// 绑定车间人员根据role_id
    public static final String OFFIC_SAVESENDDOCCTR = "SaveSendDocCtr";// 段发公文审批,提交签阅页面,
    public static final String OFFIC_GETYBDOCLIST = "GetYbDocList";// 获得已办信息列表【使用】
    public static final String OFFIC_BACKSENDDOC = "BackSendDoc";// 段发公文绑定回退步骤列表【使用】
    public static final String OFFIC_SAVEBACKSENDDOC = "SaveBackSendDoc";// 段发公文回退,提交回退操作
    public static final String OFFIC_ADDUSERDOC = "AddUserDoc";// 增加同级签阅人员【段发公文使用】
    public static final String OFFIC_SAVEADDUSERDOC = "SaveAddUserDoc";// 增加同级签阅人员，提交操作【段发公文使用】
    public static final String OFFIC_BINDNOTICECTR = "BindNoticeCtr";// 生产安全通知审批,绑定签阅页面
    public static final String OFFIC_SAVENOTICECTR = "SaveNoticeCtr";// 生产安全通知审批,提交签阅页面,
    public static final String OFFIC_ADDUSERNOTICE = "AddUserNotice";// 增加同级签阅人员【生产安全通知使用】
    public static final String OFFIC_SAVEADDUSERNOTICE = "SaveAddUserNotice";// 增加同级签阅人员，提交操作【生产安全通知使用】
    public static final String OFFIC_BACKNOTICEDOC = "BackNoticeDoc";// 生产安全通知绑定回退步骤列表【使用】
    public static final String OFFIC_SAVEBACKNOTICEDOC = "SaveBackNoticeDoc";// 生产安全通知回退,提交回退操作,
    public static final String OFFIC_BINDRECDOCCTR = "BindRecDocCtr";// 上级文电审批,绑定签阅页面
    public static final String OFFIC_BINDCBSELECTUSERSEX = "BindCBSelectUsersEx";// 承办选择人员
    public static final String OFFIC_SAVEBINDRECDOCCTR = "SaveBindRecDocCtr";// 上级文电审批,提交签阅页面
    public static final String OFFIC_BINDBLSELECTUSERSEX = "BindBLSelectUsersEx";//上级文电选择车间办理人员
    public static final String OFFIC_BINDPYSELECTUSERSEX = "BindPYSelectUsersEx";// 上级文电选择车间批阅人员
    public static final String OFFIC_BACKRECDOC = "BackRecDoc";// 上级文电绑定回退步骤列表【使用】
    public static final String OFFIC_SAVEBACKRECDOC = "SaveBackRecDoc";//上级文电回退,提交回退操作
    public static final String OFFIC_ADDUSERRECDOC = "AddUserRecDoc";// 增加同级签阅人员【上级文电使用】
    public static final String OFFIC_SAVEADDUSERRECDOC = "SaveAddUserRecDoc";// 增加同级签阅人员，提交操作【上级文电使用】
    public static final String OFFIC_SEARCHRECDOC = "SearchRecDoc";// 上级文电查询
    public static final String OFFIC_SEARCHSENDDOC = "SearchSendDoc";// 段发公文查询
    public static final String OFFIC_SENDDOCBACKBTNSTATE = "SendDocBackBtnState";// 段发公文回退按钮状态
    public static final String OFFIC_RECDOCBACKBTNSTATE = "RecDocBackBtnState";// 上级文电回退按钮状态
    public static final String OFFIC_NOTICEBACKBTNSTATE = "NoticeBackBtnState";// 生产安全通知回退按钮状态
    public static final String OFFIC_GETALLUSERSBYPOSTILTYPE = "GetAllUsersByPostilType";// 上级文电根据选择人员返回用户ID合集
    public static final String OFFIC_GETALLREADUSERSBYPOSTILTYPE = "GetAllReadUsersByPostilType";// 上级文电根据选择人员返回用户ID合集
    public static final String OFFIC_NOTICEGETALLUSERSBYPOSTILTYPE = "NoticeGetAllUsersByPostilType";// 上级文电根据选择人员返回用户ID合集



    //施工子系统
    public static String SP_CONSTRUCT = "construct";
    public static String DATE = "DATE";
    public static final String SERVICE_PAGE2 = "/Sggl/SgglApp.asmx";
    public static final String CONSTRUCT_LOGIN = "Login";
    public static final String CONSTRUCT_GETDAYCONST = "GetDayConst";//返回今日施工项目
    public static final String CONSTRUCT_GETDAYOTHERJOB = "GetDayOtherJob";//返回今日点外项目
    public static final String CONSTRUCT_GETDAYREPAIR = "GetDayRepair";//返回今日维修项目
    public static final String CONSTRUCT_BINDSINGLECONST = "BindSingleConst";//返回施工项目详细信息
    public static final String CONSTRUCT_BINDSINGLECONSTFORM = "BindSingleConstForm";//返回施工项目作业单
    public static final String CONSTRUCT_BINDSINGLECONSTLOG = "BindSingleConstLog";//返回施工项目工作过程
    public static final String CONSTRUCT_BINDSINGLEREPAIR = "BindSingleRepair";//返回维修项目详细信息
    public static final String CONSTRUCT_BINDSINGLEREPAIRLOG = "BindSingleRepairLog";//返回维修项目工作过程
    public static final String CONSTRUCT_BINDSINGLEOTHERJOB = "BindSingleOtherJob";//返回点外项目详细信息
    public static final String CONSTRUCT_BINDSINGLEOTHERJOBLOG = "BindSingleOtherJobLog";//返回点外项目工作过程
    public static final String CONSTRUCT_BINDSGLINETYPE = "BindSgLineType";//绑定施工行别
    public static final String CONSTRUCT_BINDSGWORKLINE = "BindSgWorkLine";//绑定施工线别
    public static final String CONSTRUCT_BINDSGSTATION = "BindSgStation";//绑定站/区段
    public static final String CONSTRUCT_BINDSGREGSTATION = "BindSgRegStation";//绑定登记站
    public static final String CONSTRUCT_BINDSGWORKSPACE = "BindSgWorkSpace";//绑定作业车间

    public static final String CONSTRUCT_BINDWXLINETYPE = "BindWxLineType";//绑定施工行别
    public static final String CONSTRUCT_BINDWXWORKLINE = "BindWxWorkLine";//绑定施工线别
    public static final String CONSTRUCT_BINDWXSTATION = "BindWxStation";//绑定站/区段
    public static final String CONSTRUCT_BINDWXREGSTATION = "BindWxRegStation ";//绑定登记站
    public static final String CONSTRUCT_BINDWXWORKSPACE  = "BindWxWorkSpace ";//绑定作业车间
    public static final String CONSTRUCT_GETSGSEARCH  = "GetSgSearch ";//绑定作业车间
    public static final String CONSTRUCT_GETWXSEARCH  = "GetWxSearch ";//绑定作业车间
}
