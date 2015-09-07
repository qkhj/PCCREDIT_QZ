package com.cardpay.pccredit.intopieces.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.divisional.constant.DivisionalProgressEnum;
import com.cardpay.pccredit.divisional.constant.DivisionalTypeEnum;
import com.cardpay.pccredit.divisional.service.DivisionalService;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.IntoPiecesException;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationIntopieceWaitDao;
import com.cardpay.pccredit.intopieces.dao.IntoPiecesDao;
import com.cardpay.pccredit.intopieces.dao.JyxxDao;
import com.cardpay.pccredit.intopieces.dao.YwsqbDao;
import com.cardpay.pccredit.intopieces.dao.ZA_YWSQB_R_Dao;
import com.cardpay.pccredit.intopieces.dao.comdao.IntoPiecesComdao;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.filter.MakeCardFilter;
import com.cardpay.pccredit.intopieces.model.ApplicationDataImport;
import com.cardpay.pccredit.intopieces.model.BasicCustomerInformationS;
import com.cardpay.pccredit.intopieces.model.CustomerAccountData;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationCom;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.CustomerCareersInformationS;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.MakeCard;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.QzApplnJyxx;
import com.cardpay.pccredit.intopieces.model.QzApplnProcessResult;
import com.cardpay.pccredit.intopieces.model.QzApplnSxjc;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.cardpay.pccredit.intopieces.model.VideoAccessories;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.intopieces.web.QzApplnSxjcForm;
import com.cardpay.pccredit.intopieces.web.QzDcnrUploadForm;
import com.cardpay.pccredit.intopieces.web.QzSdhjydForm;
import com.cardpay.pccredit.intopieces.web.QzShouxinForm;
import com.cardpay.pccredit.product.model.AddressAccessories;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.workflow.dao.WfStatusResultDao;
import com.cardpay.workflow.models.WfProcessRecord;
import com.cardpay.workflow.models.WfStatusQueueRecord;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

@Service
public class YwsqbService {

	// TODO 路径使用相对路径，首先获得应用所在路径，之后建立上传文件目录，图片类型使用IMG，文件使用DOC

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IntoPiecesDao intoPiecesDao;

	@Autowired
	private IntoPiecesComdao intoPiecesComdao;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;

	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;
	
	@Autowired
	private YwsqbDao ywsqbDao;
	
	@Autowired
	private JyxxDao jyxxDao;
	
	@Autowired
	private ZA_YWSQB_R_Dao za_ywsqb_r_dao;
	
	//保存业务申请表
	public void insert_page1(QzApplnYwsqb qzApplnYwsqb, QzApplnJyxx qzApplnJyxx,HttpServletRequest request) throws Exception{
		commonDao.insertObject(qzApplnYwsqb);
		String ywsqbId = qzApplnYwsqb.getId();
		
		//添加经营信息
		QzApplnJyxx tmp = jyxxDao.findJyxx(qzApplnYwsqb.getCustomerId(), null);
		if(tmp == null){
			commonDao.insertObject(qzApplnJyxx);
		}
		else{
			qzApplnJyxx.setId(tmp.getId());
			commonDao.updateObject(qzApplnJyxx);
		}
		
		//获取动态新增的页面元素
		//主要供应商
		int zygys_cnt = Integer.parseInt(request.getParameter("zygys_cnt"));
		for(int i = 1;i<=zygys_cnt;i++){
			QzApplnYwsqbZygys obj = new QzApplnYwsqbZygys();
			obj.setYwsqbId(ywsqbId);
			obj.setLsh(i);
			obj.setName(request.getParameter("name_zygys_"+i));
			obj.setRate(request.getParameter("rate_zygys_"+i));
			obj.setCondition(request.getParameter("condition_zygys_"+i));
			commonDao.insertObject(obj);
		}
		//主要客户
		int zykh_cnt = Integer.parseInt(request.getParameter("zykh_cnt"));
		for(int i = 1;i<=zykh_cnt;i++){
			QzApplnYwsqbZykh obj = new QzApplnYwsqbZykh();
			obj.setYwsqbId(ywsqbId);
			obj.setLsh(i);
			obj.setName(request.getParameter("name_zykh_"+i));
			obj.setRate(request.getParameter("rate_zykh_"+i));
			obj.setCondition(request.getParameter("condition_zykh_"+i));
			commonDao.insertObject(obj);
		}
		//借款记录
		if(qzApplnYwsqb.getBorrowHistory().equals("1")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			int jkjl_cnt = Integer.parseInt(request.getParameter("jkjl_cnt"));
			for(int i = 1;i<=jkjl_cnt;i++){
				QzApplnYwsqbJkjl obj = new QzApplnYwsqbJkjl();
				obj.setYwsqbId(ywsqbId);
				obj.setLsh(i);
				obj.setBankOrOtherType(request.getParameter("bankOrOtherType_jkjl_"+i));
				obj.setPurpose(request.getParameter("purpose_jkjl_"+i));
				obj.setTotalAmount(request.getParameter("totalAmount_jkjl_"+i));
				obj.setLoanDate(sdf.parse(request.getParameter("loanDate_jkjl_"+i)));
				obj.setDeadline(request.getParameter("deadline_jkjl_"+i));
				obj.setRates(request.getParameter("rates_jkjl_"+i));
				obj.setRepayType(request.getParameter("repayType_jkjl_"+i));
				obj.setRemainSum(request.getParameter("remainSum_jkjl_"+i));
				commonDao.insertObject(obj);
			}
		}
	}
	
	//保存业务申请表(贷生活)
		public void insert_page1ForLife(QzApplnYwsqb qzApplnYwsqb, HttpServletRequest request) throws Exception{
			commonDao.insertObject(qzApplnYwsqb);
			String ywsqbId = qzApplnYwsqb.getId();
			
			//添加经营信息
//			QzApplnJyxx tmp = jyxxDao.findJyxx(qzApplnYwsqb.getCustomerId(), null);
//			if(tmp == null){
//				commonDao.insertObject(qzApplnJyxx);
//			}
//			else{
//				qzApplnJyxx.setId(tmp.getId());
//				commonDao.updateObject(qzApplnJyxx);
//			}
			
			//获取动态新增的页面元素
			//主要供应商
//			int zygys_cnt = Integer.parseInt(request.getParameter("zygys_cnt"));
//			for(int i = 1;i<=zygys_cnt;i++){
//				QzApplnYwsqbZygys obj = new QzApplnYwsqbZygys();
//				obj.setYwsqbId(ywsqbId);
//				obj.setLsh(i);
//				obj.setName(request.getParameter("name_zygys_"+i));
//				obj.setRate(request.getParameter("rate_zygys_"+i));
//				obj.setCondition(request.getParameter("condition_zygys_"+i));
//				commonDao.insertObject(obj);
//			}
			//主要客户
//			int zykh_cnt = Integer.parseInt(request.getParameter("zykh_cnt"));
//			for(int i = 1;i<=zykh_cnt;i++){
//				QzApplnYwsqbZykh obj = new QzApplnYwsqbZykh();
//				obj.setYwsqbId(ywsqbId);
//				obj.setLsh(i);
//				obj.setName(request.getParameter("name_zykh_"+i));
//				obj.setRate(request.getParameter("rate_zykh_"+i));
//				obj.setCondition(request.getParameter("condition_zykh_"+i));
//				commonDao.insertObject(obj);
//			}
			//借款记录
			if(qzApplnYwsqb.getBorrowHistory().equals("1")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				int jkjl_cnt = Integer.parseInt(request.getParameter("jkjl_cnt"));
				for(int i = 1;i<=jkjl_cnt;i++){
					QzApplnYwsqbJkjl obj = new QzApplnYwsqbJkjl();
					obj.setYwsqbId(ywsqbId);
					obj.setLsh(i);
					obj.setBankOrOtherType(request.getParameter("bankOrOtherType_jkjl_"+i));
					obj.setPurpose(request.getParameter("purpose_jkjl_"+i));
					obj.setTotalAmount(request.getParameter("totalAmount_jkjl_"+i));
					obj.setLoanDate(sdf.parse(request.getParameter("loanDate_jkjl_"+i)));
					obj.setDeadline(request.getParameter("deadline_jkjl_"+i));
					obj.setRates(request.getParameter("rates_jkjl_"+i));
					obj.setRepayType(request.getParameter("repayType_jkjl_"+i));
					obj.setRemainSum(request.getParameter("remainSum_jkjl_"+i));
					commonDao.insertObject(obj);
				}
			}
		}
	
	public void update_page1(QzApplnYwsqb qzApplnYwsqb,QzApplnJyxx qzApplnJyxx,HttpServletRequest request) throws Exception {
		commonDao.updateObject(qzApplnYwsqb);
		String ywsqbId = qzApplnYwsqb.getId();
		
		//添加经营信息
		QzApplnJyxx tmp = jyxxDao.findJyxx(qzApplnYwsqb.getCustomerId(), null);
		if(tmp == null){
			commonDao.insertObject(qzApplnJyxx);
		}
		else{
			qzApplnJyxx.setId(tmp.getId());
			commonDao.updateObject(qzApplnJyxx);
		}
				
		//获取动态新增的页面元素
		//主要供应商
		this.deleteYwsqbZygys(ywsqbId);
		int zygys_cnt = Integer.parseInt(request.getParameter("zygys_cnt"));
		for(int i = 1;i<=zygys_cnt;i++){
			QzApplnYwsqbZygys obj = new QzApplnYwsqbZygys();
			obj.setYwsqbId(ywsqbId);
			obj.setLsh(i);
			obj.setName(request.getParameter("name_zygys_"+i));
			obj.setRate(request.getParameter("rate_zygys_"+i));
			obj.setCondition(request.getParameter("condition_zygys_"+i));
			commonDao.insertObject(obj);
		}
		//主要客户
		this.deleteYwsqbZykh(ywsqbId);
		int zykh_cnt = Integer.parseInt(request.getParameter("zykh_cnt"));
		for(int i = 1;i<=zykh_cnt;i++){
			QzApplnYwsqbZykh obj = new QzApplnYwsqbZykh();
			obj.setYwsqbId(ywsqbId);
			obj.setLsh(i);
			obj.setName(request.getParameter("name_zykh_"+i));
			obj.setRate(request.getParameter("rate_zykh_"+i));
			obj.setCondition(request.getParameter("condition_zykh_"+i));
			commonDao.insertObject(obj);
		}
		//借款记录
		this.deleteYwsqbJkjl(ywsqbId);
		if(qzApplnYwsqb.getBorrowHistory().equals("1")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			int jkjl_cnt = Integer.parseInt(request.getParameter("jkjl_cnt"));
			for(int i = 1;i<=jkjl_cnt;i++){
				QzApplnYwsqbJkjl obj = new QzApplnYwsqbJkjl();
				obj.setYwsqbId(ywsqbId);
				obj.setLsh(i);
				obj.setBankOrOtherType(request.getParameter("bankOrOtherType_jkjl_"+i));
				obj.setPurpose(request.getParameter("purpose_jkjl_"+i));
				obj.setTotalAmount(request.getParameter("totalAmount_jkjl_"+i));
				obj.setLoanDate(sdf.parse(request.getParameter("loanDate_jkjl_"+i)));
				obj.setDeadline(request.getParameter("deadline_jkjl_"+i));
				obj.setRates(request.getParameter("rates_jkjl_"+i));
				obj.setRepayType(request.getParameter("repayType_jkjl_"+i));
				obj.setRemainSum(request.getParameter("remainSum_jkjl_"+i));
				commonDao.insertObject(obj);
			}
		}
	}
	
	public void update_page1ForLife(QzApplnYwsqb qzApplnYwsqb,HttpServletRequest request) throws Exception {
		commonDao.updateObject(qzApplnYwsqb);
		String ywsqbId = qzApplnYwsqb.getId();
		
		//添加经营信息
//		QzApplnJyxx tmp = jyxxDao.findJyxx(qzApplnYwsqb.getCustomerId(), null);
//		if(tmp == null){
//			commonDao.insertObject(qzApplnJyxx);
//		}
//		else{
//			qzApplnJyxx.setId(tmp.getId());
//			commonDao.updateObject(qzApplnJyxx);
//		}
				
		//获取动态新增的页面元素
//		//主要供应商
//		this.deleteYwsqbZygys(ywsqbId);
//		int zygys_cnt = Integer.parseInt(request.getParameter("zygys_cnt"));
//		for(int i = 1;i<=zygys_cnt;i++){
//			QzApplnYwsqbZygys obj = new QzApplnYwsqbZygys();
//			obj.setYwsqbId(ywsqbId);
//			obj.setLsh(i);
//			obj.setName(request.getParameter("name_zygys_"+i));
//			obj.setRate(request.getParameter("rate_zygys_"+i));
//			obj.setCondition(request.getParameter("condition_zygys_"+i));
//			commonDao.insertObject(obj);
//		}
//		//主要客户
//		this.deleteYwsqbZykh(ywsqbId);
//		int zykh_cnt = Integer.parseInt(request.getParameter("zykh_cnt"));
//		for(int i = 1;i<=zykh_cnt;i++){
//			QzApplnYwsqbZykh obj = new QzApplnYwsqbZykh();
//			obj.setYwsqbId(ywsqbId);
//			obj.setLsh(i);
//			obj.setName(request.getParameter("name_zykh_"+i));
//			obj.setRate(request.getParameter("rate_zykh_"+i));
//			obj.setCondition(request.getParameter("condition_zykh_"+i));
//			commonDao.insertObject(obj);
//		}
		//借款记录
		this.deleteYwsqbJkjl(ywsqbId);
		if(qzApplnYwsqb.getBorrowHistory().equals("1")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			int jkjl_cnt = Integer.parseInt(request.getParameter("jkjl_cnt"));
			for(int i = 1;i<=jkjl_cnt;i++){
				QzApplnYwsqbJkjl obj = new QzApplnYwsqbJkjl();
				obj.setYwsqbId(ywsqbId);
				obj.setLsh(i);
				obj.setBankOrOtherType(request.getParameter("bankOrOtherType_jkjl_"+i));
				obj.setPurpose(request.getParameter("purpose_jkjl_"+i));
				obj.setTotalAmount(request.getParameter("totalAmount_jkjl_"+i));
				obj.setLoanDate(sdf.parse(request.getParameter("loanDate_jkjl_"+i)));
				obj.setDeadline(request.getParameter("deadline_jkjl_"+i));
				obj.setRates(request.getParameter("rates_jkjl_"+i));
				obj.setRepayType(request.getParameter("repayType_jkjl_"+i));
				obj.setRemainSum(request.getParameter("remainSum_jkjl_"+i));
				commonDao.insertObject(obj);
			}
		}
	}
	//查找page1 ywsqb信息
	public QzApplnYwsqb findYwsqb(String customerId,String applicationId){
		return ywsqbDao.findYwsqb(customerId, applicationId);
	}
	
	public QzApplnYwsqb findYwsqbByAppId(String applicationId){
		return ywsqbDao.findYwsqbByAppId(applicationId);
	}
	
	public List<QzApplnYwsqb> findYwsqbforCustomerId(String customerId){
		return ywsqbDao.findYwsqbforCustomerId(customerId);
	}
	
	//查找page1 zygys信息
	public List<QzApplnYwsqbZygys> findYwsqbZygys(String ywsqbId){
		return ywsqbDao.findYwsqbZygys(ywsqbId);
	}
	
	//查找page1 zykh信息
	public List<QzApplnYwsqbZykh> findYwsqbZykh(String ywsqbId){
		return ywsqbDao.findYwsqbZykh(ywsqbId);
	}
	
	//查找page1 jkjl信息
	public List<QzApplnYwsqbJkjl> findYwsqbJkjl(String ywsqbId){
		return ywsqbDao.findYwsqbJkjl(ywsqbId);
	}

	//删除page1 zygys信息
	public void deleteYwsqbZygys(String ywsqbId){
		ywsqbDao.deleteYwsqbZygys(ywsqbId);
	}
	
	//删除page1 zykh信息
	public void deleteYwsqbZykh(String ywsqbId){
		ywsqbDao.deleteYwsqbZykh(ywsqbId);
	}
	
	//删除page1 jkjl信息
	public void deleteYwsqbJkjl(String ywsqbId){
		ywsqbDao.deleteYwsqbJkjl(ywsqbId);
	}

	//空值的处理
	public void dealWithNullValue(QzApplnYwsqb qzApplnYwsqb) {
		//证件类型
		if(!qzApplnYwsqb.getGlobalType().equals("026")){
			qzApplnYwsqb.setGlobalTypeOther("");
		}
		//教育水平
		if(!qzApplnYwsqb.getEducationLevel().equals("000")){
			qzApplnYwsqb.setEducationLevelOther("");
		}
		//婚姻状况
		if(!qzApplnYwsqb.getMaritalStatus().equals("20")){
			qzApplnYwsqb.setMaritalStatusOther("");
			qzApplnYwsqb.setMaritalName("");
			qzApplnYwsqb.setMaritalGlobalType("001");
			qzApplnYwsqb.setMaritalGlobalTypeOther("");
			qzApplnYwsqb.setMaritalGlobalId("");
			qzApplnYwsqb.setMaritalWorkunit("");
			qzApplnYwsqb.setMaritalPhone("");
		}
		//配偶证件类型
		if(qzApplnYwsqb.getMaritalGlobalType() == null || !qzApplnYwsqb.getMaritalGlobalType().equals("026")){
			qzApplnYwsqb.setMaritalGlobalTypeOther("");
		}
		//家庭住址类型
		if(!qzApplnYwsqb.getAddressType().equals("5")){
			qzApplnYwsqb.setAddressTypeOther("");
		}
		
		//抵押物品
		if(!qzApplnYwsqb.getHaveGunt().equals("1")){
			qzApplnYwsqb.setThing_1("");
			qzApplnYwsqb.setThing_2("");
			qzApplnYwsqb.setThing_3("");
			qzApplnYwsqb.setThing_4("");
		}
		//是否为其他人担保
		if(!qzApplnYwsqb.getGuntForOther().equals("1")){
			qzApplnYwsqb.setGuntForOtherBankname("");
			qzApplnYwsqb.setGuntForOtherClientname("");
		}
		//是否曾在泉州银行申请过贷款
		if(!qzApplnYwsqb.getHaveApplyLoan().equals("1")){
			qzApplnYwsqb.setHaveLoanTime(null);
		}
		//是否曾使用泉州银行电子银行产品
		if(!qzApplnYwsqb.getHaveElePro().equals("1")){
			qzApplnYwsqb.setHaveEleProType("");
		}
		//有关联的个人或实体是否获得过泉州银行贷款
		if(!qzApplnYwsqb.getHaveGotLoan().equals("1")){
			qzApplnYwsqb.setHaveGotLoanName("");
			qzApplnYwsqb.setHaveGotLoanRelation("");
		}
		//信息渠道
		if(!qzApplnYwsqb.getInfoType().equals("8")){
			qzApplnYwsqb.setInfoTypeOther("");
		}
	}
	
	public void dealWithNullValueJyxx(QzApplnJyxx qzApplnJyxx){
		//实际经营地址
		if(!qzApplnJyxx.getBussAddrType().equals("5")){
			qzApplnJyxx.setBussAddrTypeOther("");
		}
		//组织形式
		if(!qzApplnJyxx.getBussType().equals("3")){
			qzApplnJyxx.setBussTypeOther("");
		}
	}

	public void insert_page0(QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r) {
		commonDao.insertObject(qzappln_za_ywsqb_r);
	}
	
	public void update_page0(QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r) {
		commonDao.updateObject(qzappln_za_ywsqb_r);
	}
	
	public void updateYwsqb(QzApplnYwsqb qzApplnYwsqb) {
		commonDao.updateObject(qzApplnYwsqb);
	}
}
