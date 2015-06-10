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
import com.cardpay.pccredit.intopieces.dao.DbrxxDao;
import com.cardpay.pccredit.intopieces.dao.IntoPiecesDao;
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
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.QzApplnProcessResult;
import com.cardpay.pccredit.intopieces.model.QzApplnSxjc;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
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
public class DbrxxService {

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
	private DivisionalService divisionalService;
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;

	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;
	
	@Autowired
	private WfStatusResultDao wfStatusResultDao;
	
	@Autowired
	private DbrxxDao dbrxxDao;
	
	/* 查询担保人列表 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public List<QzApplnDbrxx> findDbrxx(String customerId,String applicationId) {
		return dbrxxDao.findDbrxx(customerId, applicationId);
	}

	public List<QzApplnDbrxx> findDbrxxByAppId(String applicationId) {
		return dbrxxDao.findDbrxxByAppId(applicationId);
	}
	
	public void insert_page4(QzApplnDbrxx qzApplnDbrxx,HttpServletRequest request) throws Exception {
		commonDao.insertObject(qzApplnDbrxx);
		String dbrxxId = qzApplnDbrxx.getId();
		
		//获取动态新增的页面元素
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//贷款记录
		int dkjl_cnt = Integer.parseInt(request.getParameter("dkjl_cnt"));
		for(int i = 1;i<=dkjl_cnt;i++){
			QzApplnDbrxxDkjl obj = new QzApplnDbrxxDkjl();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setLoanType(request.getParameter("loanType_"+i));
			obj.setAmount(request.getParameter("amount_"+i));
			obj.setDeadline(request.getParameter("deadline_"+i));
			obj.setLoanDate(sdf.parse(request.getParameter("loanDate_"+i)));
			obj.setPurpose(request.getParameter("purpose_"+i));
			obj.setRemainAmount(request.getParameter("remainAmount_"+i));
			obj.setGuntForOther(request.getParameter("guntForOther_"+i));
			obj.setGuntAmount(request.getParameter("guntAmount_"+i));
			obj.setGuntDeadline(request.getParameter("guntDeadline_"+i));
			commonDao.insertObject(obj);
		}
		//房产
		int fc_cnt = Integer.parseInt(request.getParameter("fc_cnt"));
		for(int i = 1;i<=fc_cnt;i++){
			QzApplnDbrxxFc obj = new QzApplnDbrxxFc();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setAddr(request.getParameter("addr_"+i));
			obj.setPrice(request.getParameter("price_"+i));
			obj.setUsrSituation(request.getParameter("usrSituation_"+i));
			obj.setHaveCopy(request.getParameter("haveCopy_"+i));
			commonDao.insertObject(obj);
		}
		//机动车
		int jdc_cnt = Integer.parseInt(request.getParameter("jdc_cnt"));
		for(int i = 1;i<=jdc_cnt;i++){
			QzApplnDbrxxJdc obj = new QzApplnDbrxxJdc();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setCardNo(request.getParameter("cardNo_"+i));
			obj.setPrice(request.getParameter("price_"+i));
			obj.setBuyDate(sdf.parse(request.getParameter("buyDate_"+i)));
			obj.setHaveCopy_1(request.getParameter("haveCopy_1_"+i));
			obj.setHaveCopy_2(request.getParameter("haveCopy_2_"+i));
			commonDao.insertObject(obj);
		}
	}

	public void update_page4(QzApplnDbrxx qzApplnDbrxx,HttpServletRequest request) throws Exception {
		commonDao.updateObject(qzApplnDbrxx);
		String dbrxxId = qzApplnDbrxx.getId();
		
		//获取动态新增的页面元素
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//贷款记录
		this.deleteDbrxxDkjl(dbrxxId);
		int dkjl_cnt = Integer.parseInt(request.getParameter("dkjl_cnt"));
		for(int i = 1;i<=dkjl_cnt;i++){
			QzApplnDbrxxDkjl obj = new QzApplnDbrxxDkjl();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setLoanType(request.getParameter("loanType_"+i));
			obj.setAmount(request.getParameter("amount_"+i));
			obj.setDeadline(request.getParameter("deadline_"+i));
			obj.setLoanDate(sdf.parse(request.getParameter("loanDate_"+i)));
			obj.setPurpose(request.getParameter("purpose_"+i));
			obj.setRemainAmount(request.getParameter("remainAmount_"+i));
			obj.setGuntForOther(request.getParameter("guntForOther_"+i));
			obj.setGuntAmount(request.getParameter("guntAmount_"+i));
			obj.setGuntDeadline(request.getParameter("guntDeadline_"+i));
			commonDao.insertObject(obj);
		}
		//房产
		this.deleteDbrxxFc(dbrxxId);
		int fc_cnt = Integer.parseInt(request.getParameter("fc_cnt"));
		for(int i = 1;i<=fc_cnt;i++){
			QzApplnDbrxxFc obj = new QzApplnDbrxxFc();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setAddr(request.getParameter("addr_"+i));
			obj.setPrice(request.getParameter("price_"+i));
			obj.setUsrSituation(request.getParameter("usrSituation_"+i));
			obj.setHaveCopy(request.getParameter("haveCopy_"+i));
			commonDao.insertObject(obj);
		}
		//机动车
		this.deleteDbrxxJdc(dbrxxId);
		int jdc_cnt = Integer.parseInt(request.getParameter("jdc_cnt"));
		for(int i = 1;i<=jdc_cnt;i++){
			QzApplnDbrxxJdc obj = new QzApplnDbrxxJdc();
			obj.setDbrxxId(dbrxxId);
			obj.setLsh(i);
			obj.setCardNo(request.getParameter("cardNo_"+i));
			obj.setPrice(request.getParameter("price_"+i));
			obj.setBuyDate(sdf.parse(request.getParameter("buyDate_"+i)));
			obj.setHaveCopy_1(request.getParameter("haveCopy_1_"+i));
			obj.setHaveCopy_2(request.getParameter("haveCopy_2_"+i));
			commonDao.insertObject(obj);
		}
	}
	
	public QzApplnDbrxx findDbrxxById(String id) {
		return dbrxxDao.findDbrxxById(id);
	}

	public List<QzApplnDbrxxDkjl> findDbrxxDkjl(String dbrxxId) {
		return dbrxxDao.findDbrxxDkjl(dbrxxId);
	}

	public List<QzApplnDbrxxFc> findDbrxxFc(String dbrxxId) {
		return dbrxxDao.findDbrxxFc(dbrxxId);
	}

	public List<QzApplnDbrxxJdc> findDbrxxJdc(String dbrxxId) {
		return dbrxxDao.findDbrxxJdc(dbrxxId);
	}

	//删除page4 信息
	public void deleteDbrxx(String dbrxxId){
		dbrxxDao.deleteDbrxxDkjl(dbrxxId);
		dbrxxDao.deleteDbrxxFc(dbrxxId);
		dbrxxDao.deleteDbrxxJdc(dbrxxId);
		dbrxxDao.deleteDbrxx(dbrxxId);
	}
	
	//删除page4 dkjl信息
	public void deleteDbrxxDkjl(String dbrxxId){
		dbrxxDao.deleteDbrxxDkjl(dbrxxId);
	}
	
	//删除page4 fc信息
	public void deleteDbrxxFc(String dbrxxId){
		dbrxxDao.deleteDbrxxFc(dbrxxId);
	}
	
	//删除page4 jdc信息
	public void deleteDbrxxJdc(String dbrxxId){
		dbrxxDao.deleteDbrxxJdc(dbrxxId);
	}
		
	//处理空值
	public void dealWithNullValue(QzApplnDbrxx qzApplnDbrxx) {
		//工作及收入证明文件
		if(!qzApplnDbrxx.getHaveProveFile().equals("1")){
			qzApplnDbrxx.setFileName("");
		}
	}
	
	
}
