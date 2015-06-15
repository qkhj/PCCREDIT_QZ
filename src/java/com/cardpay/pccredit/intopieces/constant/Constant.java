package com.cardpay.pccredit.intopieces.constant;

public class Constant {
	/* 操作失败 */
	public static String FAIL_MESSAGE = "操作失败";
	
	/* 操作成功 */
	public static String SUCCESS_MESSAGE = "操作成功";
	
	/* 操作成功 */
	public static String UPLOAD_SUCCESS_MESSAGE = "导出并上传成功";
	
	/* 文件为空 */
	public static String FILE_EMPTY = "文件不可为空";
	
	/* 结果集 */
	public static String RESULT_LIST1 = "resultList1";
	
	/* 结果集 */
	public static String RESULT_LIST2 = "resultList2";
	
	/* 影像资料上传路径 */
	public static String FILE_PATH = "/home/pccredit/pccredit_upload/";
	
	/* 保存进件*/
	public static String SAVE_INTOPICES = "save";
	
	/* 申请进件*/
	public static String APPROVE_INTOPICES = "audit";  
	
	/* 拒绝进件*/
	public static String REFUSE_INTOPICES = "refuse";
	
	/* 成功进件*/
	public static String SUCCESS_INTOPICES = "success";
	
	/* 退件*/
	public static String NOPASS_INTOPICES = "nopass";
	
	/* 退件到申请状态*/
	public static String RETURN_INTOPICES = "returnedToFirst";
	
	/* 申请已通过*/
	public static String APPROVED_INTOPICES = "approved";
	
	/*以下是上传状态*/
	public static String INITIAL_INTOPICES="initial";
	
	public static String  EXPORT_INTOPICES="export";
	
	public static String  UPLOAD_INTOPICES="upload";
	
	
	
	
	/*联系人*/
	public static String CONTACTID = "contactId";
	
	/*担保人*/
	public static String GUARANTORID = "guarantorId";
	
    /*推荐人*/
	public static String RECOMMENDID = "recommendId";
	
	/**
	 * 定时生成 默认用户
	 */
	public static String SCHEDULES_SYSTEM_USER = "system";
	
	
	/*FTP链接配置*/
	public static String FTPIP = "11.23.11.43";
	
	public static String FTPPORT = "21";
	
	public static String FTPUSERNAME = "root";
	
	public static String FTPPASSWORD = "abc,123";
	
	public static String FTPPATH = "qiankang";
	/*进件信息*/
	public static String APP_STATE_1="已申请";
	public static String APP_STATE_2="未申请";
	public static String APP_STATE_3="退回";
	public static String APP_STATE_4="审批完成";
	
	//泉州银行根机构id
	public static String QZ_ORG_ROOT_ID = "9350000000";
	
	/*默认产品*/
	public static String DEFAULT_TYPE="1";

	/*进件状态*/
	public static String status_shenqing="申请";
	public static String status_chushen="初审";
	public static String status_xingzheng1="内部审查";
	public static String status_shouxin="授信审批";
	public static String status_zhongxin="中心复核";
	public static String status_xinshen="填写合同信息";
	public static String status_xingzheng2="中心终审";
	public static String jyd_id="00001";//决议单默认ID
	public static String htd_id="00002";//合同单默认ID
	public static String status_buchong="补充上会";
	
	/*审批结果*/
	public static String APPLN_TYPE_1="审批通过";
	public static String APPLN_TYPE_2="退件";
	public static String APPLN_TYPE_3="拒件";
	
	/*审批特殊标志*/
	public static String APPROVE_EDIT_1="1";//初审退回--直接删除申请件
	public static String APPROVE_EDIT_2="3";//初审拒件--授信岗审批
	public static String APPROVE_EDIT_3="4";//授信岗补充上会--退回客户经理
	
	/*待决策资料清单列表*/
	public static String ATTACH_LIST1="贷款申请表";
	public static String ATTACH_LIST2="调查报告";
	public static String ATTACH_LIST3="征信查询授权书";
	public static String ATTACH_LIST4="工作底稿";
	public static String ATTACH_LIST5="信用报告及联网核查";
	public static String ATTACH_LIST6="贷审小组决议表";
	public static String ATTACH_LIST7="规范操作承诺书";
	public static String ATTACH_LIST8="收入证明文件";
	public static String ATTACH_LIST9="借款人资产文件、住址证明";
	public static String ATTACH_LIST10="借款人及共同借款人身份证复印件";
	public static String ATTACH_LIST11="借款人及共同借款人婚姻状况证明";
	public static String ATTACH_LIST12="担保人及配偶身份证明复印件";
	public static String ATTACH_LIST13="担保人及配偶婚姻状况说明";
	public static String ATTACH_LIST14="担保人收入证明";
	public static String ATTACH_LIST15="申请表";
	public static String ATTACH_LIST16="营业执照（副本）复印件";
	public static String ATTACH_LIST17="税务登记证（副本）复印件";
	public static String ATTACH_LIST18="组织机构代码证（副本）复印件";
	public static String ATTACH_LIST19="贷款卡及年检报告复印件";
	public static String ATTACH_LIST20="公司章程（包括验资报告复印件）";
	public static String ATTACH_LIST21="股东会或董事会决议原件";
	public static String ATTACH_LIST22="法定代表人身份证明文件复印件";
	public static String ATTACH_LIST23="股东身份证明复印件";
	public static String ATTACH_LIST24="担保人身份证明复印件";
	public static String ATTACH_LIST25="担保人贷款卡复印件";
	public static String ATTACH_LIST26="担保人收入证明";
	public static String ATTACH_LIST27="信用等级证复印件（如有）";

	//影像平台
	public static String SunIASUrl = "http://168.168.241.10:8001/SunIAS/SunIASRequestServlet.do?AppID=App1001&UID=admin1&PWD=123456&";
	/*url=url+"UserID=0902&";
	url=url+"UserName=苏晓丽&";
	url=url+"OrgID=9350501013&";
	url=url+"OrgName=泉州银行东海支行&";
	url=url+"right=1111&";
	url=url+"info1=QKJY:0000004738439009";*/
}
